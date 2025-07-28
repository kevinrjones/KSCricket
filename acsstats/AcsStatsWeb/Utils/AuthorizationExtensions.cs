using Microsoft.AspNetCore.Authorization;

namespace AcsStatsWeb.Utils;

public static class AuthorizationExtensions
{
    public static TBuilder RequireAuthorization<TBuilder>(this TBuilder builder, string? roles,
        string? authenticationSchemes) where TBuilder : IEndpointConventionBuilder
    {
        if (builder == null)
        {
            throw new ArgumentNullException(nameof(builder));
        }

        return builder.RequireAuthorization(new AuthorizeAttribute
        {
            Roles = roles,
            AuthenticationSchemes = authenticationSchemes
        });
    }
}