namespace AcsStatsWeb.Config;

public class IdentityServerConfiguration
{
    public string? Authority { get; set; }
    public required string ClientId { get; set; }
    public required string ClientSecret { get; set; }
    public List<string>? ApiScopes { get; set; }
    public string? ApiResourceName { get; set; }
    public string? IdentityScope { get; set; }
}