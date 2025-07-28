using Microsoft.AspNetCore.Mvc;

namespace AcsStatsWeb.Controllers;

[Route("api/[controller]")]
[ApiController]
public class ConfigController : ControllerBase
{
    private readonly IConfiguration _configuration;

    public ConfigController(IConfiguration configuration)
    {
        _configuration = configuration;
    }

    // Example action method
    [HttpGet]
    public IActionResult Get()
    {
        return Ok(
            new ConfigurationForAngular
            {
                AuthorityUrl =
                    _configuration["IdentityServer:Authority"]
            });
    }
}

class ConfigurationForAngular
{
    public String? AuthorityUrl { get; set; }
}