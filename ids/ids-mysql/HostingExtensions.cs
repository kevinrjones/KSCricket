using Duende.IdentityServer;
using ids_mysql;
using ids.Database;
using Ids.EMail;
using Microsoft.AspNetCore.HttpOverrides;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.UI.Services;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using Serilog;

namespace ids_mysql;

internal static class HostingExtensions
{
    public static WebApplication ConfigureServices(this WebApplicationBuilder builder)
    {
        var connectionString = builder.Configuration.GetConnectionString("identity");
        var migrationsAssembly = typeof(Config).Assembly.GetName().Name;
        var serverVersion = ServerVersion.AutoDetect(connectionString);

        builder.Services.AddDbContext<ApplicationDbContext>(options =>
        {
            options.UseMySql(connectionString, serverVersion,
                sqlOptions =>
                {
                    sqlOptions.MigrationsAssembly(migrationsAssembly);
                    // sqlOptions.TranslateParameterizedCollectionsToConstants();
                });
        });

        builder.Services
            .AddIdentity<ApplicationUser, IdentityRole>(options => options.SignIn.RequireConfirmedAccount = true)
            .AddEntityFrameworkStores<ApplicationDbContext>()
            .AddDefaultTokenProviders();


        builder.Services.AddRazorPages();
        builder.Services.Configure<ForwardedHeadersOptions>(options =>
        {
            options.ForwardedHeaders =
                ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto;
        });

        builder.Services.AddTransient<IEmailSender, SendGridEmailSender>();


        var isBuilder = builder.Services.AddIdentityServer(options =>
            {
                options.Events.RaiseErrorEvents = true;
                options.Events.RaiseInformationEvents = true;
                options.Events.RaiseFailureEvents = true;
                options.Events.RaiseSuccessEvents = true;

                // see https://docs.duendesoftware.com/identityserver/v6/fundamentals/resources/
                options.EmitStaticAudienceClaim = true;
            })
            .AddConfigurationStore(options => options.ConfigureDbContext = b => b.UseMySql(connectionString,
                    serverVersion,
                    opt =>
                    {
                        opt.MigrationsAssembly(migrationsAssembly);
                        // opt.TranslateParameterizedCollectionsToConstants();
                    }
                )
            )
            .AddOperationalStore(options => options.ConfigureDbContext = b => b.UseMySql(connectionString,
                serverVersion,
                opt =>
                {
                    opt.MigrationsAssembly(migrationsAssembly);
                    // opt.TranslateParameterizedCollectionsToConstants();
                }
            ))
            .AddAspNetIdentity<ApplicationUser>();


        // if you want to use server-side sessions: https://blog.duendesoftware.com/posts/20220406_session_management/
        // then enable it
        //isBuilder.AddServerSideSessions();
        //
        // and put some authorization on the admin/management pages
        //builder.Services.AddAuthorization(options =>
        //       options.AddPolicy("admin",
        //           policy => policy.RequireClaim("sub", "1"))
        //   );
        //builder.Services.Configure<RazorPagesOptions>(options =>
        //    options.Conventions.AuthorizeFolder("/ServerSideSessions", "admin"));


        // see the README for details on these values
        // for development these are in ~/.microsoft/usersecrets
        var cricketClientId = builder.Configuration["Authentication:Google:ClientId"];
        var cricketClientSecret = builder.Configuration["Authentication:Google:ClientSecret"];

        if (cricketClientId == null || cricketClientSecret == null)
            throw new Exception("Google secrets should be set in the environment variables or on the command line");

        builder.Services.AddAuthentication()
            .AddGoogle("Google", options =>
            {
                options.ClientId = cricketClientId;
                options.ClientSecret = cricketClientSecret;
                options.SignInScheme = IdentityServerConstants.ExternalCookieAuthenticationScheme;
            });

        return builder.Build();
    }

    public static WebApplication ConfigurePipeline(this WebApplication app)
    {
        var forwardingOptions = new ForwardedHeadersOptions()
        {
            ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto
        };
        forwardingOptions.KnownNetworks.Clear(); // Loopback by default, this should be temporary
        forwardingOptions.KnownProxies.Clear(); // Update to include

        app.UseForwardedHeaders(forwardingOptions);

        app.UseSerilogRequestLogging();

        if (app.Environment.IsDevelopment())
        {
            app.UseDeveloperExceptionPage();
        }

        app.UseStaticFiles();
        app.UseRouting();
        app.UseIdentityServer();
        app.UseAuthorization();

        app.MapRazorPages()
            .RequireAuthorization();

        return app;
    }
}