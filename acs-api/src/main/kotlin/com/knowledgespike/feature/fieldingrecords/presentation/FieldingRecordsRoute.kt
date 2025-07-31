package com.knowledgespike.feature.fieldingrecords.presentation


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
import com.knowledgespike.feature.fieldingrecords.domain.usecase.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


/**
 * Represents the base route path for retrieving overall fielding records.
 * Used within the routing configuration for handling API requests
 * that fetch overall fielding performance data.
 */
private const val OVERALL_ROUTE = "/api/fieldingrecords/overall"
/**
 * Defines the route for accessing fielding records based on a specific series.
 *
 * This route is part of the API for retrieving fielding statistics, allowing clients
 * to request records filtered by series for an individual or entity. It is configured
 * to handle GET requests with parameters such as match type, team ID, opponents ID, and
 * match subtype, ensuring that these filter criteria are validated and processed appropriately.
 */
private const val BY_SERIES_ROUTE = "/api/fieldingrecords/series"
/**
 * Defines the API route for retrieving fielding records based on specific seasons.
 * This route is used for handling requests related to fielding performance data filtered by season.
 */
private const val BY_SEASON_ROUTE = "/api/fieldingrecords/season"
/**
 * Represents the route for accessing fielding records filtered by the year of match start.
 * It serves as a constant path identifier used within routing definitions to handle
 * requests related to yearly fielding performance data.
 */
private const val BY_YEAR_ROUTE = "/api/fieldingrecords/year"
/**
 * Defines the route for retrieving fielding records filtered specifically by grounds.
 *
 * This route represents an endpoint in the `FieldingRecordsUseCases` API that allows
 * fetching individual fielding performance data based on the grounds where matches
 * were played. It is used in conjunction with other parameters like match type, team ID,
 * opponents, and match subtype to validate and process the request.
 */
private const val BY_GROUNDS_ROUTE = "/api/fieldingrecords/grounds"
/**
 * Defines the route used for accessing fielding records filtered by the host country.
 * This constant is a string representing the endpoint path for retrieving fielding statistics
 * specific to matches hosted in particular countries.
 */
private const val BY_HOST_ROUTE = "/api/fieldingrecords/host"
/**
 * Constant representing the API route for retrieving fielding records filtered by opponents.
 * This route is used to handle requests to fetch an individual's fielding performance data
 * specific to matches against particular opponents.
 */
private const val BY_OPPONENTS_ROUTE = "/api/fieldingrecords/opposition"
/**
 * Represents the API route used to retrieve fielding performance records on an innings-by-innings basis.
 *
 * This constant is used to define the routing path for fetching detailed records of an individual's
 * fielding performance broken down by individual innings during a match.
 *
 * Associated with the `getIndividualInningsByInningsRecords` use case in `FieldingRecordsUseCases`.
 */
private const val INNINGSBYINNINGS_ROUTE = "/api/fieldingrecords/inningsbyinnings"
/**
 * Specifies the route for accessing match total fielding records.
 *
 * This route is used to retrieve aggregated data on fielding performance for an individual across matches.
 * It is part of the routing configuration inside `routeFieldingRecords` and is authenticated using `AUTH_JWT_READ`.
 *
 * Endpoint: "/api/fieldingrecords/match"
 */
private const val MATCH_TOTALS_ROUTE = "/api/fieldingrecords/match"
/**
 * Represents the parameterized route path for fielding records APIs.
 *
 * The route contains placeholders for the following parameters:
 * - `matchType`: Specifies the type of match (e.g., test, ODI, etc.).
 * - `teamId`: Identifies the team for which the data is being queried.
 * - `opponentsId`: Identifies the opposing team for filtering data.
 * - `format` (optional): Defines the match format or additional sub-type.
 */
private const val ROUTE_PARAMETERS = "/{matchType}/{teamId}/{opponentsId}/{format?}"
/**
 * Represents the default limit for the number of records or items to be fetched
 * when no specific limit is provided in the request parameters.
 */

/**
 * Configures the routing for handling HTTP requests related to fielding records.
 *
 * This method sets up authentication and defines routes for various endpoints for fetching fielding records,
 * including overall statistics, series-based records, season-based records, year-based records, records by grounds,
 * host country, opponents, innings-by-innings, and match totals.
 *
 * Features:
 * - Uses JWT authentication to ensure secured access to the endpoints.
 * - Validates request parameters before processing.
 * - Delegates the request processing to corresponding use cases in `FieldingRecordsUseCases`.
 * - Handles different routes, each targeting a specific fielding statistics requirement:
 *   - Overall statistics (`getIndividualOverallFieldingRecords` use case).
 *   - Series-based records (`getIndividualSeriesUseCase` use case).
 *   - Year-based records (`getIndividualByYearOfMatchStartUseCase` use case).
 *   - Season-specific records (`getIndividualSeasonUseCase` use case).
 *   - Records by grounds (`getIndividualGroundsUseCase` use case).
 *   - Records by host country (`getByHostCountryUseCase` use case).
 *   - Records by opponents (`getByOpponentsUseCase` use case).
 *   - Innings-by-innings performance (`getIndividualInningsByInningsRecords` use case).
 *   - Aggregated match totals (`gatIndividualMatchTotalsRecords` use case).
 *
 * Requests are validated to ensure correct parameter formats, and errors are handled gracefully.
 *
 * Dependencies:
 * - Injects `FieldingRecordsUseCases` to access the respective use cases.
 * - Uses helper functions like `extractRequestParameters`, `validateParameters`, and processing handlers
 *   to execute the actual logic through respective use cases.
 */
fun Application.routeFieldingRecords() {

    val fieldingRecordsUseCases by inject<FieldingRecordsUseCases>()

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
                            handleGetOverallFieldingUseCase(
                                requestParameters,
                                validatedParams,
                                fieldingRecordsUseCases.getIndividualOverallFieldingRecords,
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
                                fieldingRecordsUseCases.getIndividualSeriesUseCase,
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
                                fieldingRecordsUseCases.getIndividualSeasonUseCase,
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
                                fieldingRecordsUseCases.getIndividualByYearOfMatchStartUseCase,
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
                                fieldingRecordsUseCases.getIndividualGroundsUseCase,
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
                                fieldingRecordsUseCases.getByHostCountryUseCase,
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
                                fieldingRecordsUseCases.getByOpponentsUseCase,
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
                                fieldingRecordsUseCases.getIndividualInningsByInningsRecords,
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
                                fieldingRecordsUseCases.gatIndividualMatchTotalsRecords,
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
 * Handles the "Get Overall Fielding" use case by processing the given parameters,
 * invoking the use case to fetch fielding data, and sending the response back to the client.
 *
 * @param parameters The request parameters containing raw input data, such as match details, team IDs, date ranges, and sorting preferences.
 * @param validatedParams The validated and structured search parameters to ensure consistency and correctness for the query.
 * @param getOverallFieldingUseCase The use case instance responsible for retrieving overall fielding statistics for an individual player.
 * @param call The `ApplicationCall` object representing the request-reply lifecycle to send the response back to the client.
 */
private suspend fun handleGetOverallFieldingUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getOverallFieldingUseCase: GetIndividualOverallFieldingUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getOverallFieldingUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the use case to retrieve individual series data based on the provided validated parameters
 * and request parameters, then responds with the resulting data in a successful HTTP response.
 *
 * @param parameters The original request parameters containing filters and pagination details.
 * @param validatedParams The validated set of search parameters ensuring consistent and correct representation of query criteria.
 * @param getIndividualSeriesUseCase The use case used to fetch series data for an individual based on the search parameters.
 * @param call The `ApplicationCall` instance used to respond with the data fetched from the use case.
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
 * Handles the use case for retrieving data by season based on the provided parameters.
 *
 * This method processes the incoming request parameters, validates and adapts them into a search format,
 * invokes the use case to retrieve the required data, and sends the response back to the client.
 *
 * @param parameters The raw request parameters submitted by the client.
 * @param validatedParams The pre-validated search parameters ensuring correct query structure.
 * @param getIndividualSeasonUseCase The use case responsible for fetching data by season.
 * @param call The application call object for responding to the client's request.
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
 * Handles the use case for retrieving fielding statistics grouped by the year of match start.
 *
 * This method processes the request by interpreting the given parameters, transforming them
 * into validated search parameters, invoking the appropriate use case to fetch the data, and
 * responding with the resulting data in a structured format.
 *
 * @param parameters The raw request parameters containing various search criteria.
 * @param validatedParams The validated search parameters that include sanitized and converted criteria.
 * @param getIndividualByYearOfMatchStartUseCase The use case to fetch fielding statistics grouped by the year of match start.
 * @param call The application call instance for interacting with the HTTP request and response.
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
 * Handles the use case for retrieving individual grounds data based on validated search
 * parameters and request parameters, responding with the results in a success envelope.
 *
 * @param parameters Raw request parameters provided by the client, containing match, team,
 * venue, and pagination details.
 * @param validatedParams Pre-validated search parameters used to ensure proper execution
 * of the query.
 * @param getIndividualGroundsUseCase Use case for retrieving individual fielding records
 * filtered by specific grounds.
 * @param call Application call object used to respond to the HTTP request.
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
 * Handles the use case of retrieving data based on a host country by utilizing the GetByHostCountryUseCase
 * and responding with the results.
 *
 * @param parameters The request parameters containing various query criteria such as IDs, date range,
 * match type, and paging configurations.
 * @param validatedParams The validated search parameters ensuring consistency and correctness of the query inputs.
 * @param getByHostCountryUseCase The use case for fetching data filtered by host country, utilizing validated search parameters.
 * @param call The application's HTTP call object to construct and send the response.
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
 * Handles fielding statistics retrieval filtered by opponents using the provided use case, request parameters,
 * and validated search parameters. The resulting data is sent as an HTTP response.
 *
 * @param parameters The raw request parameters containing details such as team IDs, match type, venue,
 * date range, and sorting information.
 * @param validatedParams The pre-validated search parameters used as a base for constructing search criteria.
 * @param getByOpponentsUseCase The use case responsible for executing the opponent-specific fielding statistics query.
 * @param call The application call instance used to send the HTTP response back to the client.
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
 * Handles the retrieval of innings-by-innings fielding statistics for an individual player.
 *
 * This function coordinates the process of handling the `ApplicationCall`, extracting required
 * parameters, invoking the use case with validated search parameters, and responding with the results.
 *
 * @param parameters The initial request parameters including match type, teams, venue, date range, sorting, and paging criteria.
 * @param validatedParams The pre-validated search parameters ensuring consistency and integrity of the input data.
 * @param getInningsByInningsUseCase The use case responsible for fetching fielding statistics on an innings-by-innings basis.
 * @param call The `ApplicationCall` object representing the HTTP request and response lifecycle.
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
 * Handles the process of retrieving individual match totals data using the provided parameters and use case.
 * This method parses and transforms the input request parameters, executes the use case to retrieve the data,
 * and responds to the application call with the results.
 *
 * @param parameters The request parameters obtained from the API call, including various filters such as match type,
 *                   team identifiers, match results, date range, and pagination details.
 * @param validatedParams Already validated search parameters containing default or initial values for querying match totals.
 * @param getMatchTotalsUseCase The use case responsible for fetching match totals data from the repository based on search parameters.
 * @param call The application call instance used to send the response back to the client.
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
 * Updates and retrieves a modified instance of `ValidatedSearchParameters` based on the provided
 * `RequestParameters`.
 *
 * @param validatedParams The existing validated search parameters to be updated.
 * @param parameters The request parameters containing new data to modify the validated parameters.
 * @return A new instance of `ValidatedSearchParameters` with fields updated based on the given request parameters.
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






