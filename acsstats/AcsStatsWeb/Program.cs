using System;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AcsStatsWeb.Config;
using AcsStatsWeb.Utils;
using Duende.Bff.Yarp;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.HttpOverrides;
using Microsoft.AspNetCore.Server.Kestrel.Core;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.IdentityModel.Protocols.OpenIdConnect;
using Microsoft.IdentityModel.Tokens;
using Serilog;

namespace AcsStatsWeb;

public static class Program
{
    public static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);

        builder.Host.UseSerilog();
        
        Log.Logger = new LoggerConfiguration()
            .ReadFrom.Configuration(builder.Configuration)
            .CreateLogger();

        Log.Information("App starting");

        ConfigureServices(builder.Services, builder.Configuration);

        var app = builder.Build();
        
        var forwardingOptions = new ForwardedHeadersOptions()
        {
            ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto
        };
        forwardingOptions.KnownNetworks.Clear(); // Loopback by default, this should be temporary
        forwardingOptions.KnownProxies.Clear(); // Update to include
        app.UseForwardedHeaders(forwardingOptions);

        ConfigureSecurityHeaders(app, app.Environment);

        Configure(app);

        app.Run();
    }

    private static void ConfigureServices(IServiceCollection services, ConfigurationManager configuration)
    {
        services.Configure<ForwardedHeadersOptions>(options =>
        {
            options.ForwardedHeaders =
                ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto;
        });



        services.AddLogging();

        services.AddAuthorization();

        services.AddControllersWithViews();
        services.AddBff(options =>
            {
                // default value
                options.ManagementBasePath = "/bff";
            })
            .AddServerSideSessions()
            .AddRemoteApis();
        
        services.Configure<KestrelServerOptions>(options => { options.AllowSynchronousIO = true; });

        ConfigureIdentityServer(services, configuration);
    }

    private static void Configure(WebApplication app)
    {
        app.UseSerilogRequestLogging();
        if (!app.Environment.IsDevelopment())
        {
            // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
            app.UseHsts();
        }

        app.UseHttpsRedirection();
        app.UseStaticFiles();
        app.UseRouting();
        app.UseAuthentication();
        app.UseBff();
        app.UseAuthorization();
        app.MapBffManagementEndpoints();

        BffConfiguration bffConfig = new();
        app.Configuration.Bind("BFF", bffConfig);
        if (bffConfig.Apis.Any())
        {
            foreach (var api in bffConfig.Apis)
            {
                app.MapRemoteBffApiEndpoint(api.LocalPath, api.RemoteUrl!)
                    .RequireAccessToken(api.RequiredToken);
            }
        }

        app.UseMiddleware<ExceptionHandler>(Log.Logger);
        app.UseMiddleware<FailureHandler>(Log.Logger);
        // app.UseMiddleware<LogHttpDataMiddleware>(Log.Logger);
        
        // Ensure API calls bypass SPA fallback
        app.UseEndpoints(endpoints =>
        {
            endpoints.MapControllers(); // Map API controllers
         
            // Fallback to Angular's index.html for non-API requests
            endpoints.MapFallbackToFile("index.html");
        });

    }

    private static void ConfigureIdentityServer(IServiceCollection services, ConfigurationManager configuration)
    {
        var configSection = configuration.GetSection("IdentityServer");

        services.Configure<IdentityServerConfiguration>(configSection);

        IdentityServerConfiguration? idsConfig = configSection.Get<IdentityServerConfiguration>();

        services.AddAuthentication(options =>
            {
                options.DefaultScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                options.DefaultChallengeScheme = OpenIdConnectDefaults.AuthenticationScheme;
                options.DefaultSignOutScheme = OpenIdConnectDefaults.AuthenticationScheme;
            })
            .AddOpenIdConnect(OpenIdConnectDefaults.AuthenticationScheme, options =>
            {
                if (idsConfig?.Authority == null)
                    throw new ArgumentException("Invalid property in configuration");
                
                options.SignInScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                
                options.Authority = idsConfig.Authority;
                

                options.ClientId = idsConfig.ClientId;
                options.ClientSecret = idsConfig.ClientSecret;
                options.UsePkce = true;
                options.ResponseType = OpenIdConnectResponseType.Code;
                options.ResponseMode = OpenIdConnectResponseMode.Query;

                options.MapInboundClaims = false;
                options.GetClaimsFromUserInfoEndpoint = true;

                options.SaveTokens = true;
                options.Scope.Clear();

                options.Scope.Add("openid");
                options.Scope.Add("profile");
                options.Scope.Add("offline_access");
                if (idsConfig.IdentityScope == null || idsConfig.ApiScopes == null)
                    throw new ArgumentException("Invalid property in configuration");
                options.Scope.Add(idsConfig.IdentityScope);
                // todo: collection of scopes
                foreach(var scope in idsConfig.ApiScopes)
                {
                    options.Scope.Add(scope);
                }

                // and refresh token
                options.Scope.Add("offline_access");

                options.TokenValidationParameters = new TokenValidationParameters
                {
                    NameClaimType = "name",
                    RoleClaimType = "role"
                };

                options.Events.OnRedirectToIdentityProvider = context =>
                {
                    if (context.Request.Path.StartsWithSegments("/api"))
                    {
                        context.Response.StatusCode = 401;
                        context.HandleResponse();
                    }

                    return Task.CompletedTask;
                };


                options.ClaimActions.MapAll();
                options.ClaimActions.MapJsonKey("role", "role");
            })
            .AddCookie(CookieAuthenticationDefaults.AuthenticationScheme, options =>
            {
                options.ExpireTimeSpan = TimeSpan.FromHours(8);
                options.SlidingExpiration = false;
                options.Cookie.Name = "__Host-acsstats";
                options.Cookie.SameSite = SameSiteMode.Strict;

                options.Cookie.HttpOnly = true;
                options.Cookie.SecurePolicy = CookieSecurePolicy.SameAsRequest;

                options.LoginPath = "/Auth/Login";
                options.LogoutPath = "/Auth/Logout";

                options.Events.OnRedirectToAccessDenied = context =>
                {
                    if (context.Request.Path.StartsWithSegments("/api"))
                    {
                        context.Response.StatusCode = 403;
                    }

                    return Task.CompletedTask;
                };

                options.Events.OnRedirectToLogin = context =>
                {
                    if (!context.Request.Path.StartsWithSegments("/api"))
                    {
                        context.Response.Redirect(context.RedirectUri);
                    }

                    return Task.CompletedTask;
                };
            });
    }

    private static void ConfigureSecurityHeaders(IApplicationBuilder app, IWebHostEnvironment env)
    {
        StringBuilder cspBuilder = new StringBuilder();
        
        string scriptSrcElem = "script-src-elem * data: blob: 'unsafe-inline' 'unsafe-eval';";
        string mediaSrc =  "media-src * data: blob: 'unsafe-inline';"; 
        string frameSrc =  "frame-src * data: blob: ;"; 
        string frameAncestors =  "frame-ancestors * data: blob:;";
        
        string defaultSrc = "default-src 'self'";
        string scriptSrc = "script-src 'self'";
        // string scriptSrc = "script-src 'self' 'unsafe-inline' ";
        string styleSrc = "style-src 'self' 'unsafe-inline' fonts.googleapis.com";
        string fontSrc = "font-src 'self' data: fonts.gstatic.com";
        string imgSrc = "img-src 'self' data:";
        string connectSrc = "";

        string objectSrc = "object-src 'none'";
        // Angular trusted types not yet supported with lazy loading modules
        // Once available, can prepend trustedTypes with "trusted-types angular; "
        string trustedTypes = "require-trusted-types-for 'script'";
        // Angular uses web sockets for hot reload
        // Angular requires 'unsafe-inline' during dev. The offending function is removed during prod build
        if (env.IsDevelopment())
        {
            scriptSrc += " 'unsafe-inline'";
            connectSrc = "connect-src 'self' ws: wss:";
        }

        cspBuilder.AppendJoin("; ", defaultSrc, scriptSrc, styleSrc, fontSrc, imgSrc, objectSrc, connectSrc,
            trustedTypes
            ,frameAncestors, scriptSrcElem, mediaSrc, frameSrc
            );
        string cspRule = cspBuilder.ToString();
        app.Use(async (context, next) =>
        {
            //XSS
            context.Response.Headers.Append("Content-Security-Policy", cspRule);
            //Man in the middle
            context.Response.Headers.Append("Strict-Transport-Security", "max-age=63072000; preload");
            //Clickjacking
            context.Response.Headers.Append("X-Frame-Options", "SAMEORIGIN");
            //MIME-sniff
            context.Response.Headers.Append("X-Content-Type-Options", "nosniff");
            //Referrer data leak
            context.Response.Headers.Append("Referrer-Policy", "same-origin");
            await next();
        });
    }
}

