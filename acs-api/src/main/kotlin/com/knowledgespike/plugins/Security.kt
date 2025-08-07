package com.knowledgespike.plugins

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.net.URI
import java.util.concurrent.TimeUnit

const val AUTH_JWT_READ = "auth-jwt"
private const val AUTH_AUDIENCE = "acs.api"
private const val AUTH_LEEWAY_SECONDS = 3L

/**
 * Configures security authentication using JWT for the application.
 *
 * This method sets up a JWT authentication provider with the specified parameters
 * to validate and authorize requests. It uses the provided JWKs URL and issuer
 * to configure a JWK provider for token verification. Additional settings like
 * realm, audience, and leeway are configured to define the authentication behavior.
 * Validation logic ensures the user has appropriate roles and scopes to access protected resources.
 *
 * @param jwksUrl URL to the JSON Web Key Set (JWKs) used for verifying JWTs.
 * @param issuer Issuer of the JWT, used to validate the token's origin.
 */
fun Application.configureSecurity(jwksUrl: String, issuer: String, realm: String) {
    authentication {
        jwt(AUTH_JWT_READ) {
            this.realm = realm
            val jwkProvider = JwkProviderBuilder(URI.create(jwksUrl).toURL())
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build()

            verifier(jwkProvider, issuer) {
                withAudience(AUTH_AUDIENCE)
                acceptLeeway(AUTH_LEEWAY_SECONDS)
            }

            validate { credential ->
                val scopes = getClaim(credential, "scope")
                val roles = getClaim(credential, "role")

                if (scopes != null && roles != null) {
                    validateUserScopesAndRoles(scopes, roles, credential)
                } else {
                    if(scopes == null)
                        this@configureSecurity.log.error("No scopes found in JWT")
                    if(roles == null)
                        this@configureSecurity.log.error("No roles found in JWT")
                    null
                }

            }

        }
    }
}

private fun getClaim(credential: JWTCredential, claimName: String): Array<String>? {
    var claims = credential.payload.getClaim(claimName).asArray(String::class.java)
    if (claims == null) {
        val claim = credential.payload.getClaim(claimName).asString()
        if (claim != null) {
            claims = arrayOf(claim)
        }
    }
    return claims
}

private val VALID_SCOPES = setOf("acs.api.read", "acs.api.write")
private val VALID_ROLES = setOf("ACS.Stats.Reader", "ACS.Stats.Writer", "ACS.Stats.Admin")

/**
 * Checks if the given scopes contain at least one valid scope.
 */
private fun hasValidScope(userScopes: Array<String>) = userScopes.any { it in VALID_SCOPES }

/**
 * Checks if the given roles contain at least one valid role.
 */
private fun hasValidRole(userRoles: Array<String>) = userRoles.any { it in VALID_ROLES }

/**
 * Validates the user's scopes and roles to determine if they have the necessary permissions.
 *
 * @param userScopes Array of scopes associated with the user's credential.
 * @param userRoles Array of roles associated with the user's credential.
 * @param credential The `JWTCredential` containing the user's JWT information.
 * @return A `JWTPrincipal` if the user has valid scopes and roles, or `null` if they do not meet the criteria.
 */
private fun validateUserScopesAndRoles(
    userScopes: Array<String>,
    userRoles: Array<String>,
    credential: JWTCredential
): JWTPrincipal? {
    return if (hasValidScope(userScopes) && hasValidRole(userRoles)) {
        JWTPrincipal(credential.payload)
    } else {
        null
    }
}


