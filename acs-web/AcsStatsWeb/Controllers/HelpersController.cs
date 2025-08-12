using System.Runtime.InteropServices.JavaScript;
using AcsStatsWeb.Config;
using AcsStatsWeb.Json;
using AcsStatsWeb.Models;
using AcsStatsWeb.Services;
using Duende.IdentityModel.Client;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;

namespace AcsStatsWeb.Controllers;

[ApiController]
[Route("api/[controller]")]
public class HelpersController(
    ITokenService tokenService,
    IOptions<ApiServerConfiguration> apiServerConfiguration) : ControllerBase
{
    [HttpGet("GetLastDateMatchesAdded")]
    public async Task<ActionResult> GetLastDateMatchesAdded()
    {
        using var client = new HttpClient();

        var token = await tokenService.GetToken("acs.api.read");

        if (token == null || token.AccessToken == null)
            throw new Exception("Unable to get token from token server");

        client.SetBearerToken(token.AccessToken);

        var result =
            await client.GetAsync($"{apiServerConfiguration.Value.BaseUrl}{apiServerConfiguration.Value.Date}");

        if (result.IsSuccessStatusCode)
        {
            var dateString = await result.Content.ReadAsStringAsync();

            var data = JsonConvert.DeserializeObject<Envelope<string>>(dateString);

            if (data == null)
                return BadRequest(Envelope.Error("Unable to get the last updated date from API server"));

            return Ok(data);
        }

        return BadRequest(Envelope.Error("Unable to connect to the API server"));
    }
}