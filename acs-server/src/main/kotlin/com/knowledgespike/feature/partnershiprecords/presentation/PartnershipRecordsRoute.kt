package com.knowledgespike.feature.partnershiprecords.presentation


import arrow.core.Either
import arrow.core.NonEmptyList
import com.knowledgespike.core.ktor.getQueryParamOrDefault
import com.knowledgespike.core.ktor.handleValidationErrors
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.core.type.shared.PagingParameters
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.core.type.shared.SortOrder
import com.knowledgespike.core.type.values.EpochDate
import com.knowledgespike.core.validation.validateParameters
import com.knowledgespike.feature.partnershiprecords.domain.usecase.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


/**
 * Defines the base route for accessing overall partnership records.
 *
 * This constant represents the endpoint for retrieving general partnership records
 * without specific filters such as series, season, year, or other granular criteria.
 *
 * The route serves as the entry point for operations related to fetching high-level
 * partnership data via the associated use case of `PartnershipRecordsUseCases`.
 */
private const val OVERALL_ROUTE = "/api/partnershiprecords/overall"
/**
 * Specifies the API route for accessing partnership records filtered by series.
 *
 * This constant is used within the routing setup to define endpoints that provide data
 * related to individual partnership records in specific series. It indicates the URL path
 * that clients can use to query series-based partnership statistics.
 *
 * The associated route is configured to extract request parameters, validate them, and
 * call the appropriate use case for fetching series-specific partnership records.
 */
private const val BY_SERIES_ROUTE = "/api/partnershiprecords/series"
/**
 * Defines the API route for fetching partnership records specific to a season.
 *
 * This constant is utilized within the routing configuration of the application to handle
 * requests that query data related to partnership records aggregated or filtered by season.
 *
 * The route is part of a broader set of APIs that provide various filtering and retrieval
 * mechanisms for partnership data, enabling season-specific insights into performances.
 */
private const val BY_SEASON_ROUTE = "/api/partnershiprecords/season"
/**
 * Represents the API route for fetching partnership records filtered by year.
 *
 * This constant is used within routing configurations to define the endpoint
 * for retrieving year-specific partnership data.
 *
 * Typically used in conjunction with the `PartnershipRecordsUseCases` to provide
 * year-wise statistics and insights on partnership records.
 */
private const val BY_YEAR_ROUTE = "/api/partnershiprecords/year"
/**
 * Defines the route for accessing partnership records data by grounds.
 *
 * This constant is used as part of the routing structure within the `routePartnershipRecords` function.
 * It identifies the endpoint where partnership records filtered by grounds are served.
 *
 * Usage:
 * The route allows authenticated users to retrieve partnership statistics specific to various playing grounds,
 * aiding in extracting data based on the location of matches.
 */
private const val BY_GROUNDS_ROUTE = "/api/partnershiprecords/grounds"
/**
 * Defines the route used for fetching partnership records specific to the host country.
 *
 * This constant is utilized within the routing configuration of the application
 * to handle API requests targeting partnership data based on the country hosting the match.
 *
 * Example usage includes GET requests to retrieve records filtered by host country criteria.
 */
private const val BY_HOST_ROUTE = "/api/partnershiprecords/host"
/**
 * Endpoint route used for fetching partnership records categorized by opponents.
 *
 * This constant defines the API route for accessing data related to partnership records
 * filtered by the opposing teams. It is utilized within the application's routing configuration
 * to handle HTTP requests for retrieving opponent-specific partnership statistics.
 */
private const val BY_OPPONENTS_ROUTE = "/api/partnershiprecords/opposition"
/**
 * Defines the API route for retrieving partnership records on an innings-by-innings basis.
 *
 * This route is used to handle HTTP requests related to fetching partnership data for each
 * innings in a cricket match. It supports query parameters for filtering data based on criteria
 * such as match type, team, opponents, and match sub-type.
 *
 * The endpoint is integrated with the `PartnershipRecordsUseCases.getIndividualInningsByInningsRecords`
 * use case, which processes the data and provides the required partnership statistics.
 *
 * Usage of this route requires authentication using the `AUTH_JWT_READ` scheme.
 */
private const val INNINGSBYINNINGS_ROUTE = "/api/partnershiprecords/inningsbyinnings"
/**
 * The route for accessing partnership records in an innings-by-innings format for specific wickets.
 *
 * This constant defines the API endpoint used for retrieving detailed partnership data
 * at the granularity of wickets within individual innings. It is used within the routing
 * configuration to map HTTP requests to the appropriate use case that processes
 * partnership record data for specific wickets across innings of a match.
 */
private const val INNINGSBYINNINGS_FORWICKET_ROUTE = "/api/partnershiprecords/inningsbyinningsforwicket"
/**
 * Endpoint route for accessing partnership match totals records.
 *
 * This constant specifies the path used in the API routing configuration
 * to handle requests related to retrieving the aggregated match totals data
 * within the partnership records feature. The route is associated with
 * functionality aimed at fetching match-level totals for partnerships,
 * leveraging the use case `gatIndividualMatchTotalsRecords` provided within
 * the `PartnershipRecordsUseCases`.
 */
private const val MATCH_TOTALS_ROUTE = "/api/partnershiprecords/match"
/**
 * Defines the route parameters for API endpoints related to partnership records.
 *
 * The parameters include:
 * - `{matchType}`: Represents the type of the match (e.g., Test, ODI, T20).
 * - `{teamId}`: Identifier for the team.
 * - `{opponentsId}`: Identifier for the opposing team.
 * - `{format?}`: An optional parameter specifying the match format or subtype.
 *
 * This route structure facilitates dynamic endpoint handling for various partnership
 * record use cases by extracting these parameters from the request URL.
 */
private const val ROUTE_PARAMETERS = "/{matchType}/{teamId}/{opponentsId}/{format?}"
/**
 * The default value for the `limit` parameter used in request handling.
 * Specifies the maximum number of items to return when no explicit limit is provided
 * in query parameters.
 */
private const val DEFAULT_LIMIT = 100
/**
 * The default value for the start date used in queries where no specific start date is provided.
 * This value is represented as a UNIX timestamp in milliseconds, defaulting to `0L`.
 */
private const val DEFAULT_START_DATE = 0L
/**
 * Default ground ID used as a fallback value in case a ground ID is not provided
 * within the query parameters of the incoming request.
 */
private const val DEFAULT_GROUND_ID = 0
/**
 * Represents the default identifier for the host country.
 * This constant is used as a fallback value when the `hostCountryId`
 * query parameter is not provided in the request.
 */
private const val DEFAULT_HOST_COUNTRY_ID = 0
/**
 * The default value for the starting row index in paginated data queries.
 * Used as the fallback value when a "startRow" query parameter is not provided in the request.
 */
private const val DEFAULT_START_ROW = 0
/**
 * Default number of items to return per page in a paginated API response.
 *
 * This constant is used as a fallback value for the `pageSize` parameter
 * when it is not provided in the incoming request's query parameters.
 */
private const val DEFAULT_PAGE_SIZE = 50
/**
 * Default value for partnership wicket used when no specific value is provided
 * in the query parameters of a request.
 */
private const val DEFAULT_WICKET =50

/**
 * Registers routing for partnership record-related endpoints in the application.
 *
 * This method defines multiple routes to handle various types of partnership record queries.
 * Each route uses JWT authentication for securing access and processes client requests to
 * fetch specific data related to partnership records based on the given parameters.
 *
 * Routes:
 * - OVERALL_ROUTE: Retrieves overall partnership records.
 * - BY_SERIES_ROUTE: Fetches partnership data filtered by series.
 * - BY_SEASON_ROUTE: Retrieves partnership records for specific seasons.
 * - BY_YEAR_ROUTE: Accesses partnership data filtered by the start year of matches.
 * - BY_GROUNDS_ROUTE: Fetches partnership records for specific grounds.
 * - BY_HOST_ROUTE: Retrieves partnership data based on host countries.
 * - BY_OPPONENTS_ROUTE: Obtains partnership records categorized by opponents.
 * - INNINGSBYINNINGS_ROUTE: Fetches innings-by-innings partnership records.
 * - INNINGSBYINNINGS_FORWICKET_ROUTE: Retrieves wicket-specific innings-by-innings partnership data.
 * - MATCH_TOTALS_ROUTE: Accesses partnership match totals records.
 *
 * Each route validates incoming request parameters before passing them to the respective use case
 * from `PartnershipRecordsUseCases`. Errors are handled gracefully, and the validated parameters are
 * used to call the appropriate use case to process the request and generate a response.
 */
fun Application.routePartnershipRecords() {

    val partnershipRecordsUseCases by inject<PartnershipRecordsUseCases>()

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
                            handleGetOverallPartnershipUseCase(
                                requestParameters,
                                validatedParams,
                                partnershipRecordsUseCases.getIndividualOverallPartnershipRecords,
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
                                partnershipRecordsUseCases.getIndividualSeriesUseCase,
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
                                partnershipRecordsUseCases.getIndividualSeasonUseCase,
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
                                partnershipRecordsUseCases.getIndividualByYearOfMatchStartUseCase,
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
                                partnershipRecordsUseCases.getIndividualGroundsUseCase,
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
                                partnershipRecordsUseCases.getByHostCountryUseCase,
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
                                partnershipRecordsUseCases.getByOpponentsUseCase,
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
                                partnershipRecordsUseCases.getIndividualInningsByInningsRecords,
                                call
                            )
                        }
                    )
                }
            }
            route(INNINGSBYINNINGS_FORWICKET_ROUTE) {
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
                            handleGetInningsByInningForWicketUseCase(
                                requestParameters,
                                validatedParams,
                                partnershipRecordsUseCases.getIndividualInningsByInningsForWicketsRecords,
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
                                partnershipRecordsUseCases.gatIndividualMatchTotalsRecords,
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
 * Handles the retrieval of overall partnership statistics based on request parameters and search criteria.
 *
 * This function processes the incoming parameters, validates the search criteria,
 * invokes the specified use case to fetch the partnership data, and responds to the client with the results.
 *
 * @param parameters The raw request parameters provided by the client.
 * @param validatedParams The pre-validated search parameters for performing the query.
 * @param getOverallPartnershipUseCase The use case that retrieves overall partnership statistics data.
 * @param call The application call used for responding to the client.
 */
private suspend fun handleGetOverallPartnershipUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getOverallPartnershipUseCase: GetIndividualOverallPartnershipUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getOverallPartnershipUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles a use case to retrieve individual series data based on the provided parameters and responds with the result.
 *
 * @param parameters The raw request parameters containing data such as match type, team IDs, date range, and paging configurations.
 * @param validatedParams The validated search parameters with consistent and properly formatted input values.
 * @param getIndividualSeriesUseCase The use case instance responsible for retrieving series data based on search parameters.
 * @param call The application call context used to send the HTTP response back to the client.
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
 * Handles a use case for fetching and returning partnership statistics filtered by cricket seasons.
 *
 * The method processes request parameters, validates them, invokes the use case to fetch data,
 * and sends a successful HTTP response with the retrieved data.
 *
 * @param parameters The raw request parameters from the client for filtering and sorting.
 * @param validatedParams The validated search parameters used to construct the query.
 * @param getIndividualSeasonUseCase The use case instance responsible for fetching the partnership statistics by season.
 * @param call The application call instance, used to send the response back to the client.
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
 * Handles the use case of retrieving partnerships that start in a specific year based on the provided parameters.
 *
 * Sends the retrieved data as a response with an HTTP status code.
 *
 * @param parameters Represents the raw request parameters provided by the client.
 * @param validatedParams The validated search parameters derived from the request.
 * @param getIndividualByYearOfMatchStartUseCase A use case responsible for fetching partnership data filtered by year of match start.
 * @param call The application call object used to send the HTTP response.
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
 * Handles the use case for retrieving data related to individual grounds based on the provided parameters.
 * This method orchestrates the process by preparing search parameters, invoking the `GetIndividualGroundsUseCase`,
 * and responding to the client with the retrieved data.
 *
 * @param parameters The raw request parameters containing filtering, sorting, and paging data.
 * @param validatedParams The pre-validated search parameters ensuring consistency and correctness.
 * @param getIndividualGroundsUseCase The use case responsible for fetching data related to individual grounds.
 * @param call The application call used to handle the HTTP response.
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
 * Handles requests to retrieve data related to partnerships filtered by host country.
 * Constructs search parameters, executes the use case to fetch data, and responds with the result.
 *
 * @param parameters Raw request parameters provided by the user, containing filtering and sorting criteria.
 * @param validatedParams Validated search parameters that ensure correctness and consistency in the query.
 * @param getByHostCountryUseCase Use case responsible for fetching partnership data based on the host country.
 * @param call Application call instance used for responding to the client with the result.
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
 * Handles the request to retrieve partnership data filtered by opponent-specific parameters.
 *
 * The method processes incoming request parameters, validates them, modifies and maps
 * as needed, and executes the specified use case to fetch the relevant data. The data is
 * then wrapped into a successful response and sent back to the client.
 *
 * @param parameters The raw request parameters provided in the HTTP request.
 * @param validatedParams The pre-validated search parameters used for constructing the final query.
 * @param getByOpponentsUseCase The use case responsible for fetching the partnership data based on search criteria.
 * @param call The application call context used to send the HTTP response back to the client.
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
 * Handles the execution of the `GetIndividualInningsByInningsUseCase` to retrieve partnership
 * individual record details based on an innings-by-innings search, responding with the resulting data.
 *
 * @param parameters Raw request parameters defining criteria like match type, teams, venue, date range,
 * sorting preferences, and paging configurations.
 * @param validatedParams Validated search parameters constructed to meet the requirements for
 * querying detailed partnership records.
 * @param getInningsByInningsUseCase The use case responsible for fetching innings-by-innings
 * partnership individual records from the repository.
 * @param call The application call instance used to respond to the client with the corresponding
 * HTTP status and data.
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
 * Handles the retrieval of individual innings partnership records for a specific wicket.
 *
 * This method coordinates the processing of incoming request parameters, validates them,
 * retrieves the relevant partnership data via the use case, and sends the response back
 * to the client.
 *
 * @param parameters Incoming request parameters containing details like match type, team IDs,
 *                   venue, date range, sorting preferences, and paging configurations.
 * @param validatedParams The previously validated parameters ensuring valid and consistent representation
 *                        of search criteria for partnership records.
 * @param getInningsByInningsUseCase The use case responsible for fetching partnership details
 *                                   on an innings-by-innings basis for a specific wicket.
 * @param call The ApplicationCall representing the HTTP request and response for the server-client interaction.
 */
private suspend fun handleGetInningsByInningForWicketUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getInningsByInningsUseCase: GetIndividualInningsByInningsForWicketUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getInningsByInningsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the process of retrieving and responding with match total records based on the provided request parameters.
 *
 * @param parameters The raw request parameters containing filtering and pagination details.
 * @param validatedParams The validated search parameters containing sanitized and structured query criteria.
 * @param getMatchTotalsUseCase The use case responsible for fetching match total records from the repository.
 * @param call The application call object used to respond to the HTTP request.
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
 * Generates a new instance of `ValidatedSearchParameters` by copying and overriding specific fields
 * using the provided base `validatedParams` and the raw `parameters` from a request.
 *
 * @param validatedParams The existing validated search parameters that serve as the base for the new instance.
 * @param parameters The raw input request parameters used to override specific values in the validated parameters.
 * @return A new `ValidatedSearchParameters` object with updated values based on the provided `parameters`.
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
        sortDirection = SortDirection.valueOf(parameters.sortDirection),
        partnershipWicket = parameters.partnershipWicket
    )
}


/**
 * Extracts and returns request parameters from the provided RoutingCall.
 *
 * @param call The RoutingCall object containing the request parameters and query parameters
 *             to be extracted.
 * @return A RequestParameters object containing extracted values from the request.
 */
private fun extractRequestParameters(call: RoutingCall): RequestParameters {
    val pw = call.queryParameters["partnershipWicket"]
    println(pw)
    return RequestParameters(
        matchType = call.parameters["matchType"]!!,
        teamId = call.parameters["teamId"]!!.toInt(),
        opponentsId = call.parameters["opponentsId"]!!.toInt(),
        format = call.parameters["format"],
        matchSubType = call.queryParameters["matchSubType"] ?: call.parameters["matchType"]!!,
        groundId = call.getQueryParamOrDefault("groundId", DEFAULT_GROUND_ID),
        hostCountryId = call.getQueryParamOrDefault("hostCountryId", DEFAULT_HOST_COUNTRY_ID),
        venue = call.getQueryParamOrDefault("venue", DEFAULT_GROUND_ID),
        matchResult = call.getQueryParamOrDefault("matchResult", DEFAULT_GROUND_ID),
        limit = call.getQueryParamOrDefault("limit", DEFAULT_LIMIT),
        startDate = call.request.queryParameters["startDate"]?.toLongOrNull() ?: DEFAULT_START_DATE,
        endDate = call.request.queryParameters["endDate"]?.toLongOrNull() ?: DEFAULT_START_DATE,
        season = call.queryParameters["season"] ?: "",
        sortOrder = call.getQueryParamOrDefault("sortOrder", DEFAULT_LIMIT),
        sortDirection = call.queryParameters["sortDirection"] ?: "DESC",
        startRow = call.getQueryParamOrDefault("startRow", DEFAULT_START_ROW),
        pageSize = call.getQueryParamOrDefault("pageSize", DEFAULT_PAGE_SIZE),
        partnershipWicket = call.getQueryParamOrDefault("partnershipWicket", DEFAULT_WICKET),
    )
}

/**
 * Represents the parameters required to fetch match-related data and statistics.
 *
 * This data class is used as a common structure to encapsulate various filters and constraints
 * for querying match records. The parameters include details about teams, matches, venues,
 * date ranges, sorting preferences, and paging settings.
 *
 * @property matchType The type of the match (e.g., test, one-day, T20).
 * @property teamId The identifier of the team for which the data is being queried.
 * @property opponentsId The identifier of the opposing team.
 * @property format Represents the format of the match (optional).
 * @property matchSubType The subtype of the match for a more detailed categorization.
 * @property groundId The identifier for the ground where the match was held.
 * @property hostCountryId The identifier for the country hosting the match.
 * @property venue The identifier for the venue of the match.
 * @property matchResult The result of the match (e.g., win, loss, draw) used as a filter.
 * @property limit The maximum number of records to fetch.
 * @property startDate The starting date for the query range in epoch seconds.
 * @property endDate The ending date for the query range in epoch seconds.
 * @property season The season identifier or name for filtering match data.
 * @property sortOrder The parameter by which the results are sorted (e.g., runs, matches).
 * @property sortDirection The direction of sorting (e.g., ascending or descending).
 * @property startRow The starting row for paging the results.
 * @property pageSize The number of records to fetch per page.
 * @property partnershipWicket Specifies the wicket for which partnership data is being queried.
 */
private data class RequestParameters(
    val matchType: String,
    val teamId: Int,
    val opponentsId: Int,
    val format: String?,
    val matchSubType: String,
    val groundId: Int,
    val hostCountryId: Int,
    val venue: Int,
    val matchResult: Int,
    val limit: Int,
    val startDate: Long,
    val endDate: Long,
    val season: String,
    val sortOrder: Int,
    val sortDirection: String,
    val startRow: Int,
    val pageSize: Int,
    val partnershipWicket: Int
)












