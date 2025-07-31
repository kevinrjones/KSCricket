using System.Collections.Generic;
using Duende.Bff;

namespace AcsStatsWeb.Config;

/// <summary>
/// Configuration section
/// </summary>
public class BffConfiguration
{
    public string? Authority { get; set; }
    
    public string? ClientId { get; set; }
    
    /// <summary>
    /// should be supplied as a command line argument or environment variable, e.g.
    /// ./GenericBFF --BFF:ClientSecret=secret
    /// </summary>
    public string? ClientSecret { get; set; }

    public List<string> Scopes { get; set; } = new();
    public List<BffApi> Apis { get; set; } = new();
}

public class BffApi
{
    public string? LocalPath { get; set; }
    public string? RemoteUrl { get; set; }
    public TokenType RequiredToken { get; set; }
}

internal class IdentityServerConfiguration
{
    public string? Authority { get; set; }
    public string? ClientId { get; set; }
    public string? ClientSecret { get; set; }
    public List<string>? ApiScopes { get; set; }
    public string? ApiResourceName { get; set; }
    public string? IdentityScope { get; set; }
}