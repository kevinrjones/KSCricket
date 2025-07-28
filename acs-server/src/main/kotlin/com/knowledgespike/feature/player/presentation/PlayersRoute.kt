package com.knowledgespike.feature.partnershiprecords.presentation


import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.fold
import arrow.core.raise.zipOrAccumulate
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.error.IntegerError
import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.core.type.shared.SortOrder
import com.knowledgespike.core.type.values.EpochDate
import com.knowledgespike.core.type.values.PlayerId
import com.knowledgespike.core.type.values.ThreeCharacterSearchTerm
import com.knowledgespike.feature.player.domain.usecase.PlayerUseCases
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedPlayerSearchRequestParameters
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


/**
 * Represents the API route for accessing a player's biography details.
 *
 * This constant defines the path used in routing to retrieve biographical
 * information about a specific player based on their unique identifier.
 *
 * Endpoint is typically protected by authentication and responds to HTTP GET requests.
 */
private const val BIGRAPHY_ROUTE = "/api/player/biography"
/**
 * Represents the route for accessing a player's overall performance statistics.
 *
 * This constant is used to define the endpoint for retrieving aggregated performance data
 * of a player, such as combined batting and bowling metrics. The route is associated with
 * the `/api/player/overall` path and is integrated into the application's routing logic.
 *
 * The route typically expects a player ID as a parameter, allowing the server to fetch and
 * respond with the requested player's overall statistics.
 */
private const val OVERALL_ROUTE = "/api/player/overall"
/**
 * Represents the API route for retrieving detailed batting statistics of a player.
 *
 * This constant is used to define the endpoint in the application routing configuration
 * where a player's batting details can be accessed. The expected route is `/api/player/battingdetails`.
 * The corresponding functionality includes fetching and responding with the player's batting performance
 * across various match types based on the player's unique identifier.
 */
private const val BATTING_DETAILS_ROUTE = "/api/player/battingdetails"
/**
 * Defines the URI route for accessing a player's bowling statistics.
 *
 * This route is used to fetch detailed bowling performance data for a specific player.
 * It is part of the application routing system and is typically mapped to the
 * `getBowlingDetailsUseCase` function of the `PlayerUseCases` class to retrieve the data.
 *
 * The endpoint handles requests with player identifiers to return bowling statistics,
 * categorized by match types (e.g., Test, ODI, T20).
 */
private const val BOWLING_DETAILS_ROUTE = "/api/player/bowlingdetails"
/**
 * Defines the API endpoint for finding players based on specific criteria.
 *
 * This constant is used in the routing configuration for handling search requests related
 * to players. It enables clients to query players by various parameters through a GET request.
 */
private const val FIND_PLAYER_ROUTE = "/api/player/findplayers"

/**
 * Represents a placeholder for route path parameters in a URL.
 *
 * ROUTE_PARAMETERS is a constant that holds the segment "/{id}" indicating a variable
 * path parameter expected in the route. The "id" parameter is typically used to dynamically
 * capture resource identifiers (e.g., player IDs) from the URL, enabling retrieval or processing
 * of specific entities based on their unique identifiers.
 */
private const val ROUTE_PARAMETERS = "/{id}"

/**
 * Configures routing for player-related operations within the application.
 *
 * This function establishes routes to handle various HTTP requests regarding cricket player data.
 * It enables endpoints for fetching detailed player information such as biographies, overall
 * performance statistics, batting details, and bowling details. Additionally, it provides functionality
 * for searching players based on customizable criteria.
 *
 * Routes:
 * - `/biography`: Fetches a player's biography details based on their unique ID.
 * - `/overall`: Retrieves overall performance statistics for a player, categorized by match type or other criteria.
 * - `/batting`: Retrieves detailed batting statistics for a player, grouped by match type.
 * - `/bowling`: Retrieves detailed bowling statistics for a player, grouped by match type.
 * - `/find`: Searches for players based on various parameters such as name, team, and date ranges.
 *
 * Authentication:
 * All routes under this configuration require JWT authentication using the `AUTH_JWT_READ` credential to ensure security.
 *
 * Flow:
 * - Each route validates the input parameters, specifically the player ID, and logs any errors encountered.
 * - If validation succeeds, the corresponding use case is invoked to fetch or process the requested data.
 * - Responses are sent back to the client wrapped in an `Envelope` object, indicating success or failure status
 *   alongside the resulting data or error description.
 *
 * Dependencies:
 * - `PlayerUseCases`: A collection of use case classes for handling player-related operations.
 * - `Envelope`: A wrapper class used to standardize HTTP responses.
 *
 * Errors:
 * - Returns `HttpStatusCode.BadRequest` for invalid or missing input parameters, or if any backend operation fails.
 * - Logs all encountered errors for debugging/troubleshooting purposes.
 */
fun Application.routePlayers() {

    val playerUseCases by inject<PlayerUseCases>()


    routing {
        authenticate(AUTH_JWT_READ) {
            route(BIGRAPHY_ROUTE) {
                get(ROUTE_PARAMETERS) {

                    val maybePlayerId = either { PlayerId(call.parameters["id"]?.toIntOrNull()) }

                    maybePlayerId.fold(
                        ifLeft = { errors ->
                            // todo: log all failures
                            val env = Envelope.failure(errors.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        ifRight = { id ->
                            val data = playerUseCases.getPlayerBiographyUseCase(id)
                            data.fold(
                                ifLeft = {
                                    call.respond(HttpStatusCode.BadRequest, it.message)
                                },
                                ifRight = {
                                    call.respond(HttpStatusCode.OK, Envelope.success(it))
                                }
                            )
                        }
                    )
                }
            }
            route(OVERALL_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val maybePlayerId = either { PlayerId(call.parameters["id"]?.toIntOrNull()) }

                    maybePlayerId.fold(
                        ifLeft = { errors ->
                            // todo: log all failures
                            val env = Envelope.failure(errors.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        ifRight = { id ->
                            val data = playerUseCases.getPlayerOverallUseCase(id)
                            data.fold(
                                ifLeft = {
                                    call.respond(HttpStatusCode.BadRequest, it.message)
                                },
                                ifRight = {
                                    call.respond(HttpStatusCode.OK, Envelope.success(it))
                                }
                            )
                        }
                    )

                }
            }
            route(BATTING_DETAILS_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val maybePlayerId = either { PlayerId(call.parameters["id"]?.toIntOrNull()) }

                    maybePlayerId.fold(
                        ifLeft = { errors ->
                            // todo: log all failures
                            val env = Envelope.failure(errors.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        ifRight = { id ->
                            val data = playerUseCases.getBattingDetailsUseCase(id)
                            data.fold(
                                ifLeft = {
                                    call.respond(HttpStatusCode.BadRequest, it.message)
                                },
                                ifRight = {
                                    call.respond(HttpStatusCode.OK, Envelope.success(it))
                                }
                            )
                        }
                    )
                }
            }
            route(BOWLING_DETAILS_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val maybePlayerId = either { PlayerId(call.parameters["id"]?.toIntOrNull()) }

                    maybePlayerId.fold(
                        ifLeft = { errors ->
                            // todo: log all failures
                            val env = Envelope.failure(errors.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        ifRight = { id ->
                            val data = playerUseCases.getBowlingDetailsUseCase(id)
                            data.fold(
                                ifLeft = {
                                    call.respond(HttpStatusCode.BadRequest, it.message)
                                },
                                ifRight = {
                                    call.respond(HttpStatusCode.OK, Envelope.success(it))
                                }
                            )
                        }
                    )
                }
            }
            route(FIND_PLAYER_ROUTE) {
                get {
                    fold(
                        block = {
                            val requestParameters =
                                extractRequestParameters(call)
                            validateParameters(requestParameters)
                                .fold(
                                    ifLeft = {
                                        raise(it)
                                    },
                                    ifRight = {
                                        it
                                    }
                                )
                        },
                        transform = { requestParameters ->
                            val result = playerUseCases.findPlayer(requestParameters)

                            result.fold(
                                ifLeft = {
                                    val env = Envelope.failure(it.message)
                                    call.respond(HttpStatusCode.BadRequest, env)
                                },
                                ifRight = {
                                    val env = Envelope.success(it)
                                    call.respond(HttpStatusCode.OK, env)

                                }
                            )
                        },
                        recover = {
                            val env = Envelope.failure(it.map { error -> error.message }.joinToString(","))
                            call.respond(HttpStatusCode.BadRequest, env)
                        })
                }
            }
        }
    }
}

/**
 * Extracts and maps the query parameters from the given routing call to a `PlayerSearchRequestParameters` instance.
 *
 * @param call The `RoutingCall` object containing the HTTP request from which query parameters are extracted.
 * @return A `PlayerSearchRequestParameters` object populated with query parameters from the HTTP request.
 */
private fun extractRequestParameters(call: RoutingCall): PlayerSearchRequestParameters {
    return PlayerSearchRequestParameters(
        name = call.request.queryParameters["name"] ?: "",
        team = call.request.queryParameters["team"] ?: "",
        exactMatch = call.request.queryParameters["exactMatch"]?.toBoolean(),
        startDate = call.request.queryParameters["startDate"] ?: "0001-01-01",
        endDate = call.request.queryParameters["endDate"] ?: "9999-12-31",
        sortOrder = call.request.queryParameters["sortOrder"],
        sortDirection = call.request.queryParameters["sortDirection"],
    )

}

/**
 * Validates and transforms the provided `PlayerSearchRequestParameters` into a valid `ValidatedPlayerSearchRequestParameters` object.
 * The method performs validation for each parameter, ensuring compliance with expected formats and constraints.
 *
 * @param requestParameters The input parameters provided for a player search request.
 * @return Either a non-empty list of errors if validation fails, or a validated `ValidatedPlayerSearchRequestParameters` object if successful.
 */
private fun validateParameters(requestParameters: PlayerSearchRequestParameters): Either<NonEmptyList<Error>, ValidatedPlayerSearchRequestParameters> =
    either {
        val format = "yyyy-MM-dd"
        val earliestDate = "1772-06-01"
        val latestDate = "9999-12-31"
        zipOrAccumulate(
            {
                ThreeCharacterSearchTerm(requestParameters.name)
            },
            {
                if (requestParameters.startDate == null || requestParameters.startDate.isEmpty())
                    EpochDate(earliestDate, format)
                else
                    EpochDate(requestParameters.startDate, format)
            },
            {
                if (requestParameters.endDate == null || requestParameters.endDate.isEmpty())
                    EpochDate(latestDate, format)
                else
                    EpochDate(requestParameters.endDate, format)
            },
            {
                if (requestParameters.sortOrder == null || requestParameters.sortOrder.toIntOrNull() == null)
                    raise(IntegerError("The value of the sort order ${requestParameters.sortOrder} should be an integer" ))

                requestParameters.sortOrder.toInt()
            }
        ) { name, startDate, endDate, sortOrder ->
            ValidatedPlayerSearchRequestParameters(
                name = name.value,
                team = requestParameters.team,
                exactMatch = requestParameters.exactMatch ?: true,
                startDate = startDate,
                endDate = endDate,
                sortOrder =  SortOrder[sortOrder],
                sortDirection = SortDirection.valueOf(requestParameters.sortDirection ?: "DESC"),

            )
        }
    }

/**
 * Represents the parameters required for performing a player search operation.
 *
 * This data class encapsulates attributes that specify the search criteria, including
 * player name, team name, match settings, date range, and sorting preferences.
 *
 * @property name The name of the player to search for. This is a required field.
 * @property team The name of the team associated with the player. This is a required field.
 * @property exactMatch A flag indicating whether the name search should be an exact match. Defaults to null.
 * @property startDate The start date of the search range in "yyyy-MM-dd" format. Defaults to null.
 * @property endDate The end date of the search range in "yyyy-MM-dd" format. Defaults to null.
 * @property sortOrder The sorting criterion for ordering results, represented as a string. Defaults to null.
 * @property sortDirection The direction of sorting (e.g., ASC or DESC). Defaults to null.
 */
private data class PlayerSearchRequestParameters(
    val name: String,
    val team: String,
    val exactMatch: Boolean?,
    val startDate: String? = null,
    val endDate: String? = null,
    val sortOrder: String? = null,
    val sortDirection: String? = null,
)


