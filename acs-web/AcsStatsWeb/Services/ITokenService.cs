using IdentityModel.Client;

namespace AcsStatsWeb.Services
{
  public interface ITokenService
  {
    Task<TokenResponse> GetToken(string scope);
  }
}