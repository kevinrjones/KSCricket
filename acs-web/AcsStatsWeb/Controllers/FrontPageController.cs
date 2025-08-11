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
public class FrontPageController(
    ITokenService tokenService,
    IOptions<ApiServerConfiguration> apiServerConfiguration) : ControllerBase
{
    [HttpGet("GetLatestMatches")]
    public async Task<ActionResult> GetLatestMatches()
    {
        using var client = new HttpClient();

        var token = await tokenService.GetToken("acs.api.read");

        if (token == null || token.AccessToken == null)
            throw new Exception("Unable to get token from token server");

        client.SetBearerToken(token.AccessToken);

        var result =
            await client.GetAsync($"{apiServerConfiguration.Value.BaseUrl}{apiServerConfiguration.Value.Matches}");

        if (result.IsSuccessStatusCode)
        {
            var model = await result.Content.ReadAsStringAsync();

            var data = JsonConvert.DeserializeObject<Envelope<SqlResultsEnvelope<List<MatchDetails>>>>(model);

            if (data == null)
                return BadRequest(Envelope.Error("Unable to get the front page content from the API server"));

            return Ok(data);
        }

        return BadRequest(Envelope.Error("Unable to connect to the API server"));
    }
}