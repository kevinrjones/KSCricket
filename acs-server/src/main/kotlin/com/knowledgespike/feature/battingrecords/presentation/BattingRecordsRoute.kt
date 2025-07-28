package com.knowledgespike.feature.battingrecords.presentation


import arrow.core.Either
import arrow.core.NonEmptyList
import com.knowledgespike.core.jooq.RequestParameters
import com.knowledgespike.core.jooq.extractRequestParameters
import com.knowledgespike.core.ktor.handleValidationErrors
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.core.type.shared.PagingParameters
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.core.type.shared.SortOrder
import com.knowledgespike.core.type.values.EpochDate
import com.knowledgespike.core.validation.validateParameters
import com.knowledgespike.feature.battingrecords.domain.usecase.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


/**
 * Represents the API route for retrieving overall batting records.
 * Serves as the base endpoint for fetching aggregated batting metrics
 * across all matches for individual players.
 */
private const val OVERALL_ROUTE = "/api/battingrecords/overall"
/**
 * The API route constant for fetching individual batting records categorized by series.
 * This route is used to handle HTTP GET requests for retrieving batting statistics
 * specific to individual players based on series context.
 *
 * Example use case:
 * - Fetching all series details and associated batting statistics for a specific player.
 */
private const val BY_SERIES_ROUTE = "/api/battingrecords/series"
/**
 * Represents the endpoint for retrieving individual batting records filtered by season.
 * This constant is used to define the route path for handling season-specific batting record requests.
 */
private const val BY_SEASON_ROUTE = "/api/battingrecords/season"
/**
 * Defines the route used to retrieve batting records filtered by the year a match started.
 *
 * This constant is utilized in the application's routing configuration, specifically
 * under the `/api/battingrecords` endpoint, to handle requests related to filtering
 * batting records based on the year of the match start.
 *
 * Example usage includes endpoints for authenticated users to fetch batting records
 * categorized by year, based on validated search parameters, such as match type, team,
 * opponents, and other contextual filters.
 */
private const val BY_YEAR_ROUTE = "/api/battingrecords/year"
/**
 * The API route for retrieving batting records grouped by grounds.
 *
 * This constant defines the endpoint path that is used in the routing
 * configuration of the application to handle requests related to batting records
 * categorized by the venue/grounds where matches were played.
 */
private const val BY_GROUNDS_ROUTE = "/api/battingrecords/grounds"
/**
 * Defines the route for retrieving batting records filtered by host country.
 * This constant is used within the routing configuration to handle HTTP requests
 * related to batting records where data is grouped or filtered by the country
 * hosting the matches.
 */
private const val BY_HOST_ROUTE = "/api/battingrecords/host"
/**
 * Defines the route for API endpoints related to fetching batting records grouped by opponents.
 * Used in the routing configuration to handle requests for retrieving batting records
 * against specific opponents.
 */
private const val BY_OPPONENTS_ROUTE = "/api/battingrecords/opposition"
/**
 * Represents the route endpoint for retrieving individual batting records segmented by innings.
 * This route is associated with fetching detailed innings-by-innings records
 * for an individual player's performance.
 */
private const val INNINGSBYINNINGS_ROUTE = "/api/battingrecords/inningsbyinnings"
/**
 * Represents the route used for handling API requests related to match total batting records in the application.
 * This route is part of the endpoint hierarchy dedicated to retrieving various batting record statistics,
 * specifically targeting aggregated match-level totals for individuals.
 */
private const val MATCH_TOTALS_ROUTE = "/api/battingrecords/match"
/**
 * Represents a default route path containing placeholders for parameters used in URL matching.
 *
 * The path structure includes the following placeholders:
 * - `{matchType}`: Specifies the type of the match (e.g., Test, ODI, T20).
 * - `{teamId}`: Identifies the team.
 * - `{opponentsId}`: Identifies the opposing team.
 * - `{format?}`: (Optional) Specifies the format or subtype of the match.
 *
 * This constant is utilized within the routing definitions for various endpoints that
 * handle fetching batting records based on dynamic criteria.
 */
private const val ROUTE_PARAMETERS = "/{matchType}/{teamId}/{opponentsId}/{format?}"


/**
 * Configures routing for handling batting records-related API endpoints. These routes are part of
 * the application's Ktor routing setup and are protected by authentication.
 *
 * Routes:
 * - Retrieves overall batting records for an individual.
 * - Retrieves batting records grouped by series.
 * - Retrieves batting records grouped by season.
 * - Retrieves batting records grouped by the year a match started.
 * - Retrieves batting records grouped by different grounds.
 * - Retrieves batting records grouped by host country.
 * - Retrieves batting records grouped by opponents.
 * - Retrieves detailed innings-by-innings records for an individual.
 * - Retrieves match totals records for an individual.
 *
 * Each route requires request parameters to be validated before executing the corresponding use case.
 * If validation fails, appropriate errors are handled and returned to the client. If validation succeeds,
 * the specified use case will be executed, and results are returned.
 *
 * Dependencies:
 * - [BattingRecordsUseCases]: A collection of use cases for retrieving various types of batting records.
 */
fun Application.routeBattingRecords() {

    val battingRecordsUseCases by inject<BattingRecordsUseCases>()

    routing {
        authenticate(AUTH_JWT_READ) {
            route(OVERALL_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleGetOverallBattingUseCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.getIndividualOverallBattingRecords,
                                call
                            )
                        }
                    )
                }
            }
            route(BY_SERIES_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleBySeriesUseCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.getIndividualSeriesUseCase,
                                call
                            )
                        }
                    )
                }
            }
            route(BY_SEASON_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleBySeasonUseCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.getIndividualSeasonUseCase,
                                call
                            )
                        }
                    )
                }
            }
            route(BY_YEAR_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleByYearOfMatchStartUseCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.getIndividualByYearOfMatchStartUseCase,
                                call
                            )
                        }
                    )
                }
            }
            route(BY_GROUNDS_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleByGroundsUseCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.getIndividualGroundsUseCase,
                                call
                            )
                        }
                    )
                }
            }
            route(BY_HOST_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleByHostCountryUseCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.getByHostCountryUseCase,
                                call
                            )
                        }
                    )
                }
            }
            route(BY_OPPONENTS_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleByOpponentsUseCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.getByOpponentsUseCase,
                                call
                            )
                        }
                    )
                }
            }
            route(INNINGSBYINNINGS_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleGetInningsByInningsUseCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.getIndividualInningsByInningsRecords,
                                call
                            )
                        }
                    )
                }
            }
            route(MATCH_TOTALS_ROUTE) {
                get(ROUTE_PARAMETERS) {
                    val requestParameters = extractRequestParameters(call)
                    val validationResult: Either<NonEmptyList<Error>, ValidatedSearchParameters> =
                        validateParameters(
                            requestParameters.matchType,
                            requestParameters.matchSubType,
                            requestParameters.teamId,
                            requestParameters.opponentsId,
                        )
                    validationResult.fold(
                        ifLeft = { errors -> handleValidationErrors(errors, call) },
                        ifRight = { validatedParams ->
                            handleGetMatchTotalsCase(
                                requestParameters,
                                validatedParams,
                                battingRecordsUseCases.gatIndividualMatchTotalsRecords,
                                call
                            )
                        }
                    )
                }
            }
        }
    }
}

/**
 * Handles the retrieval of overall batting statistics by processing the request parameters
 * and invoking the use case. The result is sent as an HTTP response.
 *
 * @param parameters The raw request parameters received from the client.
 * @param validatedParams The validated and processed search parameters used for querying data.
 * @param getOverallBattingUseCase The use case responsible for fetching overall batting statistics.
 * @param call The `ApplicationCall` instance for interacting with the HTTP pipeline.
 */
private suspend fun handleGetOverallBattingUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getOverallBattingUseCase: GetIndividualOverallBattingUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getOverallBattingUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles a request to retrieve individual series data based on the provided search parameters.
 *
 * This function processes the incoming request by extracting search parameters, invoking the
 * `GetIndividualSeriesUseCase`, and responding with the resulting data. The process includes
 * mapping the request parameters into validated search parameters, querying the use case, and
 * formatting the successful response.
 *
 * @param parameters The incoming request parameters containing raw data for filtering.
 * @param validatedParams The pre-validated search parameters that ensure consistency and correctness.
 * @param getIndividualSeriesUseCase The use case responsible for retrieving individual series data.
 * @param call The application call instance used to send the HTTP response.
 */
private suspend fun handleBySeriesUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getIndividualSeriesUseCase: GetIndividualSeriesUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getIndividualSeriesUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the use case to retrieve and respond with data for individual season statistics
 * based on the provided request and validated search parameters.
 *
 * @param parameters Raw request parameters provided by the client for the query.
 * @param validatedParams Pre-validated search parameters ensuring data consistency.
 * @param getIndividualSeasonUseCase Use case to fetch season-specific data from the repository.
 * @param call Represents the HTTP client call to respond with the retrieved data.
 */
private suspend fun handleBySeasonUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getIndividualSeasonUseCase: GetIndividualSeasonUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getIndividualSeasonUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Processes a request to retrieve individual batting records based on the year of match start.
 *
 * @param parameters Raw request parameters provided for the query.
 * @param validatedParams Validated search parameters ensuring consistency and correctness of the query.
 * @param getIndividualByYearOfMatchStartUseCase Use case instance to fetch records by the year of match start.
 * @param call The ApplicationCall instance to handle request and response interactions.
 */
private suspend fun handleByYearOfMatchStartUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getIndividualByYearOfMatchStartUseCase: GetIndividualByYearOfMatchStartUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getIndividualByYearOfMatchStartUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles processing of grounds-based use case parameters, retrieves the
 * data from the use case, and sends the response back to the application call.
 *
 * @param parameters The raw request parameters provided by the caller, containing filtering
 *        and paging details such as ground ID, host country ID, venue, date range, and sorting options.
 * @param validatedParams The prevalidated search parameters that serve as a base for further parameter adjustments
 *        required for querying the use case.
 * @param getIndividualGroundsUseCase The use case responsible for retrieving data filtered by specific grounds,
 *        using the adjusted and validated search parameters.
 * @param call The application call used to send the HTTP response to the caller.
 */
private suspend fun handleByGroundsUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getIndividualGroundsUseCase: GetIndividualGroundsUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getIndividualGroundsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the process of retrieving batting records filtered by the host country
 * and sends the result as an HTTP response.
 *
 * This function combines various steps, including the transformation of search parameters,
 * execution of the use case to fetch the data, and responding with the result.
 *
 * @param parameters The raw request parameters containing search and filter information.
 * @param validatedParams Pre-validated search parameters ready for query execution.
 * @param getByHostCountryUseCase The use case responsible for retrieving the batting records
 * filtered by the host country.
 * @param call The HTTP application call used to send the response back to the client.
 */
private suspend fun handleByHostCountryUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getByHostCountryUseCase: GetByHostCountryUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getByHostCountryUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles operations by utilizing the provided `GetByOpponentsUseCase` with the specified request and validated search parameters.
 *
 * This method constructs the effective search parameters by blending the `RequestParameters` and `ValidatedSearchParameters`,
 * invokes the `GetByOpponentsUseCase` with the resulting parameters, and sends the outcome as an HTTP response.
 *
 * @param parameters An object containing unvalidated raw HTTP request parameters.
 * @param validatedParams An object containing validated search parameters with defaults and constraints applied.
 * @param getByOpponentsUseCase The use case responsible for retrieving data based on the provided parameters.
 * @param call The `ApplicationCall` instance to manage HTTP responses.
 */
private suspend fun handleByOpponentsUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getByOpponentsUseCase: GetByOpponentsUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getByOpponentsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the processing of fetching data for individual batting innings records based on innings-by-innings use case.
 *
 * This function prepares the appropriate search parameters, invokes the use case to retrieve the required data,
 * and sends the response back to the client in a standardized format.
 *
 * @param parameters Raw request parameters containing unvalidated query details like match type, team IDs, date range, etc.
 * @param validatedParams Pre-validated search parameters that ensure consistent and correct query criteria.
 * @param getInningsByInningsUseCase Use case responsible for fetching individual batting innings-by-innings records.
 * @param call The HTTP application call context used for responding to the client.
 */
private suspend fun handleGetInningsByInningsUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getInningsByInningsUseCase: GetIndividualInningsByInningsUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getInningsByInningsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the case for retrieving match totals based on the provided parameters.
 * This method processes the request, validates and transforms the parameters, executes the use case
 * to fetch match total data, and sends the response to the client.
 *
 * @param parameters The raw request parameters provided by the client.
 * @param validatedParams The validated search parameters used for data query construction.
 * @param getMatchTotalsUseCase The use case for retrieving individual match totals from the repository.
 * @param call The application call object used to send responses back to the client.
 */
private suspend fun handleGetMatchTotalsCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getMatchTotalsUseCase: GetIndividualMatchTotalsUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getMatchTotalsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Generates updated search parameters based on the provided validated parameters and request parameters.
 *
 * This method creates a new instance of `ValidatedSearchParameters` by copying values from the provided
 * `validatedParams` and updating specific fields with the corresponding values from `parameters`. It ensures
 * that the updated search parameters include criteria such as ground information, venue, date range, season,
 * sorting preferences, and paging configurations, all tailored for the search query.
 *
 * @param validatedParams The initial validated search parameters to be updated.
 * @param parameters The incoming request parameters containing additional or updated values.
 * @return A new instance of `ValidatedSearchParameters` containing the merged and updated search criteria.
 */
private fun getSearchParameters(
    validatedParams: ValidatedSearchParameters,
    parameters: RequestParameters
): ValidatedSearchParameters {
    return validatedParams.copy(
        groundId = parameters.groundId,
        hostCountryId = parameters.hostCountryId,
        venue = parameters.venue,
        pagingParameters = PagingParameters(
            limit = parameters.limit,
            pageSize = parameters.pageSize,
            startRow = parameters.startRow
        ),
        result = parameters.matchResult,
        startDate = EpochDate(parameters.startDate),
        endDate = EpochDate(parameters.endDate),
        season = parameters.season,
        sortOrder = SortOrder[parameters.sortOrder],
        sortDirection = SortDirection.valueOf(parameters.sortDirection)
    )
}





