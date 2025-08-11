const {env} = require('process');

const target = env.ASPNETCORE_HTTPS_PORT ? `https://localhost:${env.ASPNETCORE_HTTPS_PORT}` :
  env.ASPNETCORE_URLS ? env.ASPNETCORE_URLS.split(';')[0] : 'http://localhost:46198';

const PROXY_CONFIG = [
  {
    context: [
      "/api/frontpage",
      "/api/config",
      "/api/batting",
      "/api/bowling",
      "/api/countries",
      "/api/fielding",
      "/api/countries",
      "/api/grounds",
      "/api/matches",
      "/api/records",
      "/api/recordssummary",
      "/api/partnershiprecords",
      "/api/player",
      "/api/scorecard",
      "/api/teamrecords",
      "/api/teams",
      "/swagger/index.html",
      "/swagger/swagger-ui-standalone-preset.js",
      "/swagger/swagger-ui-bundle.js",
      "/swagger/v1/swagger.json",
      "/swagger/swagger-ui.css",
      "/bff",
      "/signin-oidc",
      "/signout-callback-oidc",
    ],
    target: target,
    secure: false,
    headers: {
      Connection: 'Keep-Alive'
    }
  }
]

module.exports = PROXY_CONFIG;
