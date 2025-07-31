using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Http;
using Microsoft.IdentityModel.Protocols.OpenIdConnect;
using Microsoft.IdentityModel.Tokens;

namespace AcsStatsWeb.Utils;

// Use this for debugging
// app.UseMiddleware<LogHttpDataMiddleware>(Log.Logger);
public sealed class LogHttpDataMiddleware
{
    private readonly RequestDelegate _next;
    private readonly Serilog.Core.Logger _logger;
    public LogHttpDataMiddleware(RequestDelegate next, Serilog.Core.Logger logger)
    {
        _logger = logger;
        _next = next;
    }

    public async Task Invoke(HttpContext context)
    {
        await LogIdentityInformation(context);
        await _next(context);
    }

    private async Task LogIdentityInformation(HttpContext httpContext)
    {
        foreach (var keyValuePair in httpContext.Request.Headers)
        {
            _logger.Debug("LogIdentityInformation: {name}, {value}", keyValuePair.Key, keyValuePair.Value);
        }

        // "OpenIdConnect" or "Cookies"
        var identityToken = await httpContext.GetTokenAsync("OpenIdConnect", OpenIdConnectParameterNames.IdToken);
        var accessToken = await httpContext.GetTokenAsync("OpenIdConnect", OpenIdConnectParameterNames.AccessToken);
        var refreshToken = await httpContext.GetTokenAsync("OpenIdConnect", OpenIdConnectParameterNames.RefreshToken);
        
        var userClaimsStringBuilder = new StringBuilder();

        foreach (var claim in httpContext.User.Claims)
        {
            userClaimsStringBuilder.AppendLine($"Claim type: {claim.Type} - Claim value: {claim.Value}");
        }

        _logger.Information("Identity token & user claims: {path}\n{IdentityToken}\n{UserClaimsStringBuilder}", httpContext.Request.Path, identityToken,
            userClaimsStringBuilder);
        _logger.Debug("Access token: \n{AccessToken}", accessToken);
        _logger.Debug("Refresh token: \n{RefreshToken}", refreshToken);
    }

}