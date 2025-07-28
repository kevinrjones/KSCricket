package com.knowledgespike.feature.bowlingrecords.presentation


import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import com.knowledgespike.core.jooq.RequestParameters
import com.knowledgespike.core.jooq.extractRequestParameters
import com.knowledgespike.core.ktor.handleValidationErrors
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.core.type.shared.PagingParameters
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.core.type.shared.SortOrder
import com.knowledgespike.core.type.values.EpochDate
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.core.type.values.TeamId
import com.knowledgespike.core.validation.validateParameters
import com.knowledgespike.feature.bowlingrecords.domain.usecase.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


/**
 * Defines the route for accessing an individual's overall bowling records
 * through the API. This route is utilized to retrieve aggregated bowling
 * performance data across all matches for a specific individual.
 */
private const val OVERALL_ROUTE = "/api/bowlingrecords/overall"
/**
 * Defines the API route for fetching bowling records categorized by series.
 * This constant is used to define the endpoint that the server will listen to
 * for incoming requests to retrieve bowling records based on an individual's
 * performance in specific series.
 *
 * Typically, it is part of the routing structure where authentication and
 * parameter validation are conducted, followed by the invocation of the
 * use case responsible for fetching the required data.
 */
private const val BY_SERIES_ROUTE = "/api/bowlingrecords/series"
/**
 * Represents the API route for accessing bowling records of an individual
 * during specific cricket seasons. This constant defines the endpoint
 * used to query season-based bowling performance data.
 *
 * Utilized within the application's routing setup to direct HTTP GET
 * requests to the appropriate handler for retrieving bowling records
 * categorized by season.
 */
private const val BY_SEASON_ROUTE = "/api/bowlingrecords/season"
/**
 * Defines the route for fetching bowling records categorized by the year matches started.
 * This route is part of an API that provides various endpoints for retrieving cricket
 * bowling performance statistics, allowing clients to query data grouped specifically
 * by the starting year of the matches.
 */
private const val BY_YEAR_ROUTE = "/api/bowlingrecords/year"
/**
 * Represents the API endpoint for retrieving an individual's bowling records
 * categorized by the grounds where the matches were played. This route is used
 * to filter and fetch bowling data specific to certain cricket grounds.
 */
private const val BY_GROUNDS_ROUTE = "/api/bowlingrecords/grounds"
/**
 * Defines the endpoint for retrieving bowling records categorized by the host country
 * where the matches were played. It provides a route for accessing data specific
 * to individual performances based on different host countries.
 */
private const val BY_HOST_ROUTE = "/api/bowlingrecords/host"
/**
 * Represents the route endpoint for fetching bowling records against specific opponents.
 * This constant is utilized in the BowlingRecords API to define the endpoint path
 * for accessing data about an individual's bowling performance when playing against
 * particular oppositions.
 */
private const val BY_OPPONENTS_ROUTE = "/api/bowlingrecords/opposition"
/**
 * Defines the route for accessing bowling records on an innings-by-innings basis.
 * This route is used in configuring the routing for API endpoints related to
 * fetching detailed bowling performance data for an individual across innings.
 */
private const val INNINGSBYINNINGS_ROUTE = "/api/bowlingrecords/inningsbyinnings"
/**
 * Represents the API route for fetching aggregate bowling totals for an individual
 * across matches. This route is part of the bowling records API and is used
 * to handle requests that aim to retrieve comprehensive bowling statistics
 * for a player at the match level. The data retrieved typically includes
 * cumulative metrics such as total wickets, runs conceded, and overs bowled
 * across multiple matches.
 */
private const val MATCH_TOTALS_ROUTE = "/api/bowlingrecords/match"
/**
 * A predefined route template used for defining API endpoints by specifying path parameters
 * to structure and extract contextual information related to match type, team, opponents, and format.
 *
 * Parameters in the route template:
 * - {matchType}: Represents the type of match (e.g., Test, ODI, T20).
 * - {teamId}: The unique identifier for the team under consideration.
 * - {opponentsId}: The unique identifier for the opposing team.
 * - {format?}: An optional parameter representing the format or subtype of the match, if applicable.
 */
private const val ROUTE_PARAMETERS = "/{matchType}/{teamId}/{opponentsId}/{format?}"
/**
 * The default limit value used when no specific limit is provided in query parameters.
 * This value represents the maximum number of items to be retrieved or processed.
 */
private const val DEFAULT_LIMIT = 100
/**
 * Represents the default start date value used when no specific start date is provided
 * in the request parameters. This is set to `0L`, which typically signifies the absence
 * of a start date or an uninitialized state.
 */
private const val DEFAULT_START_DATE = 0L
/**
 * Default value for the ground ID parameter in the request.
 *
 * This constant is used when a ground ID is not explicitly provided in the query parameters
 * of the incoming request. It serves as a fallback value to ensure proper initialization
 * and handling of the ground-related logic in the application.
 */
private const val DEFAULT_GROUND_ID = 0
/**
 * A constant representing the default host country ID used when a host country ID is not explicitly provided.
 *
 * This value serves as a fallback or default value in scenarios where no specific host country ID is supplied
 * during the request. It is commonly used in request parameter extraction or query parameter handling.
 */
private const val DEFAULT_HOST_COUNTRY_ID = 0
/**
 * The default starting row used for pagination when no explicit 'startRow' query parameter is provided
 * in the request. This helps in determining the offset for retrieving a subset of results from a dataset.
 */
private const val DEFAULT_START_ROW = 0
/**
 * Represents the default number of items to be fetched in a paginated request.
 *
 * This constant is used as the fallback value for the page size parameter when
 * no specific value is provided in a request. It determines the standard limit on
 * items per page for retrieving data in paginated responses.
 */
private const val DEFAULT_PAGE_SIZE = 50

/**
 * Defines routes for handling bowling records retrieval in a cricket application. Each route corresponds to
 * a distinct aspect of bowling performance, providing detailed metrics and records tailored to various
 * perspectives such as overall performance, series-based, season-based, year-based, ground-based, opponent-based,
 * host-country-based, innings-by-innings, and match totals.
 *
 * The routes rely on multiple use cases provided by `BowlingRecordsUseCases` to process and retrieve records
 * based on validated input parameters extracted from API requests.
 *
 * Built within an authentication layer using the `AUTH_JWT_READ` mechanism, the endpoints ensure secure
 * and restricted access. Each route validates the incoming request parameters and returns appropriate
 * responses based on the validation outcome.
 */
fun Application.routeBowlingRecords() {

    val BowlingRecordsUseCases by inject<BowlingRecordsUseCases>()

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
                            handleGetOverallBowlingUseCase(
                                requestParameters,
                                validatedParams,
                                BowlingRecordsUseCases.getIndividualOverallBowlingRecords,
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
                                BowlingRecordsUseCases.getIndividualSeriesUseCase,
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
                                BowlingRecordsUseCases.getIndividualSeasonUseCase,
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
                                BowlingRecordsUseCases.getIndividualByYearOfMatchStartUseCase,
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
                                BowlingRecordsUseCases.getIndividualGroundsUseCase,
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
                                BowlingRecordsUseCases.getByHostCountryUseCase,
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
                                BowlingRecordsUseCases.getByOpponentsUseCase,
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
                                BowlingRecordsUseCases.getIndividualInningsByInningsRecords,
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
                                BowlingRecordsUseCases.gatIndividualMatchTotalsRecords,
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
 * Handles the process of retrieving overall bowling statistics by interacting with the provided use case,
 * transforming the request parameters into validated search parameters and responding to the HTTP call with the results.
 *
 * @param parameters The unvalidated request parameters extracted from the incoming HTTP request.
 * @param validatedParams The validated and structured search parameters to be customized for the query execution.
 * @param getOverallBowlingUseCase The use case responsible for fetching the overall bowling statistics based on search parameters.
 * @param call The application-level HTTP call object used for managing responses.
 */
private suspend fun handleGetOverallBowlingUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getOverallBowlingUseCase: GetIndividualOverallBowlingUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getOverallBowlingUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles a request to fetch individual series data based on provided search parameters.
 * This function prepares the search parameters, invokes a use case to fetch data, and
 * responds with the resulting data.
 *
 * @param parameters The request parameters containing raw input data for the search.
 * @param validatedParams The validated search parameters to ensure proper formatting and consistency.
 * @param getIndividualSeriesUseCase The use case responsible for fetching individual series data.
 * @param call The application call instance used to respond with the resulting data.
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
 * Handles the use case for retrieving and responding with data filtered by a specific season.
 *
 * This method processes the request by extracting relevant search parameters from the provided objects,
 * invoking a use case to fetch the data, and then sending the response back to the caller.
 *
 * @param parameters The original set of request parameters provided by the user.
 * @param validatedParams The validated search parameters ensuring consistent and valid data representation.
 * @param getIndividualSeasonUseCase Use case instance to retrieve data based on the specified season.
 * @param call The ApplicationCall to respond back with the fetched result.
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
 * Handles the use case for fetching data grouped by the individual year of the match start.
 *
 * This method validates and maps the incoming request parameters into a set of search
 * criteria, executes the query using the provided use case, and responds with the resulting data.
 *
 * @param parameters The raw request parameters provided by the API call.
 * @param validatedParams The validated search parameters ensuring consistency and validity in the query.
 * @param getIndividualByYearOfMatchStartUseCase Use case to handle fetching data grouped by the year of a match's start.
 * @param call The application call that handles the request and response lifecycle.
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
 * Handles the use case for fetching individual ground statistics based on the provided request and validated parameters.
 *
 * This method generates the search parameters from the input `validatedParams` and `parameters`, invokes the use case
 * to fetch data specific to individual grounds, and sends the results in the HTTP response with a status of `200 OK`.
 *
 * @param parameters The raw request parameters received from the client.
 * @param validatedParams The validated search parameters that ensure consistent and correct criteria.
 * @param getIndividualGroundsUseCase The use case for retrieving statistics filtered by individual grounds.
 * @param call The application call representing the HTTP request/response cycle.
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
 * Handles the use case for retrieving data filtered by the host country.
 *
 * This method processes the request parameters, constructs appropriate search parameters,
 * invokes the use case to fetch data, and responds to the API call with the results.
 *
 * @param parameters Contains request-specific parameters provided in the API call.
 * @param validatedParams Pre-validated search parameters to ensure accurate query execution.
 * @param getByHostCountryUseCase The use case responsible for querying data based on the host country.
 * @param call The `ApplicationCall` instance representing the HTTP request and response for the current API call.
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
 * Handles the operation of retrieving data filtered by opponents and sending a successful HTTP response.
 *
 * @param parameters The raw request parameters containing filters like team IDs, date range, sorting preferences, and more.
 * @param validatedParams The validated search parameters that ensure proper structure and value constraints for the query.
 * @param getByOpponentsUseCase The use case responsible for retrieving bowling statistics based on the provided search parameters.
 * @param call The `ApplicationCall` instance used to send the response back to the client.
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
 * Handles the processing of fetching innings-by-innings data using the appropriate use case
 * and parameters, then responds with the result.
 *
 * This method combines the provided request parameters and validated search parameters to
 * construct the exact search criteria, invokes the use case to fetch the data, and sends
 * the response with the successful result.
 *
 * @param parameters The raw request parameters received from the API request.
 * @param validatedParams The validated version of search parameters used to filter the data.
 * @param getInningsByInningsUseCase The use case responsible for fetching the bowling data by innings.
 * @param call The `ApplicationCall` object used to send HTTP responses.
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
 * Handles the logic to retrieve and respond with match totals data based on the provided parameters.
 * This function utilizes a use case to fetch bowling statistics aggregated per match for an individual player.
 * The search parameters are extracted and adapted from the request parameters and validated inputs before being passed to the use case.
 *
 * @param parameters The incoming request parameters containing raw data like match type, team IDs, opponents, and additional filters.
 * @param validatedParams The pre-processed and validated search parameters ensuring consistency and correctness of input values.
 * @param getMatchTotalsUseCase The use case responsible for fetching the aggregated match totals data from the repository.
 * @param call The ApplicationCall instance used to send the response with the fetched data.
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
 * Updates and constructs a validated search parameter based on the input request parameters.
 *
 * @param validatedParams The existing validated search parameters that will be updated.
 * @param parameters The raw request parameters containing new values to update the search parameters.
 * @return A new instance of `ValidatedSearchParameters` with updated values from the input request parameters.
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

