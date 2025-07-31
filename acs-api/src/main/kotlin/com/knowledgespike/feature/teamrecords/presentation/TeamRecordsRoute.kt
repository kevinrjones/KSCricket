package com.knowledgespike.feature.teamrecords.presentation


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
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.feature.teamrecords.domain.usecase.*
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


/**
 * Represents the API route for fetching overall team records.
 *
 * This constant defines the base endpoint path used to retrieve
 * comprehensive team statistics, encompassing all relevant data
 * without filtering by specific criteria such as series, season, or other dimensions.
 *
 * It is used within the routing configuration to serve HTTP GET requests
 * aimed at querying overall team records based on validated search parameters.
 */
private const val OVERALL_ROUTE = "/api/teamrecords/overall"
/**
 * Represents the endpoint route for retrieving team records by series.
 *
 * This constant is utilized within the routing layer to handle incoming HTTP requests targeting
 * series-specific team records. It serves as a key identifier for accessing the API functionality
 * related to fetching team statistics grouped or filtered by series.
 *
 * Example use cases of this route may include fetching player team records for specific test series,
 * ODI series, or T20 series based on query parameters like match type, team ID, opponents,
 * and other relevant filters.
 */
private const val BY_SERIES_ROUTE = "/api/teamrecords/series"
/**
 * Represents the API route for accessing team records filtered by season.
 * This constant is used to define the endpoint within the routing configuration where data
 * related to team performance filtered by specific seasons can be retrieved.
 */
private const val BY_SEASON_ROUTE = "/api/teamrecords/season"
/**
 * Represents an API route for fetching team records by year.
 * This constant is utilized in the routing configuration to define an endpoint
 * for retrieving team records based on the year of the match start date.
 */
private const val BY_YEAR_ROUTE = "/api/teamrecords/year"
/**
 * Represents the API route for fetching team records categorized by grounds.
 * This constant is used to define the endpoint for the corresponding feature in the routing configuration.
 */
private const val BY_GROUNDS_ROUTE = "/api/teamrecords/grounds"
/**
 * Defines the API route for fetching team records by host country.
 * This route is used to retrieve team statistics filtered by the host country
 * in which the matches were played.
 */
private const val BY_HOST_ROUTE = "/api/teamrecords/host"
/**
 * A constant representing the route for fetching team records based on opposition details.
 * This route is used within the application's routing configuration to define the API endpoint
 * for data retrieval specific to opponents.
 */
private const val BY_OPPONENTS_ROUTE = "/api/teamrecords/opposition"
/**
 * Represents the route for handling API requests related to team records on an innings-by-innings basis.
 *
 * The associated endpoint provides detailed cricket team statistics for individual players or teams,
 * divided by each innings. It processes incoming HTTP requests, performs parameter validation, and fetches
 * the relevant data using the `getIndividualInningsByInningsRecords` use case within the `TeamRecordsUseCases`.
 *
 * This constant is used in the routing configuration to define the path for accessing these specific records.
 */
private const val INNINGSBYINNINGS_ROUTE = "/api/teamrecords/inningsbyinnings"
/**
 * Defines the route for fetching match totals related to team records.
 *
 * This constant is used in the routing setup to handle API requests targeting
 * match totals statistics. It corresponds to the path "/api/teamrecords/match".
 */
private const val MATCH_TOTALS_ROUTE = "/api/teamrecords/highesttotals"
/**
 * Defines the route for fetching match totals related to team records.
 *
 * This constant is used in the routing setup to handle API requests targeting
 * match totals statistics. It corresponds to the path "/api/teamrecords/match".
 */
private const val MATCH_RESULTS_ROUTE = "/api/teamrecords/matchresults"
private const val EXTRAS_OVERALL_ROUTE = "/api/teamrecords/extras/overall"
private const val EXTRAS_BYINNINGS_ROUTE = "/api/teamrecords/extras/innings"
private const val TARGET_HIGHEST_ROUTE = "/api/teamrecords/target/highest"
private const val TARGET_LOWEST_ROUTE = "/api/teamrecords/target/lowest"
private const val TARGET_LOWEST_UNREDUCEDROUTE = "/api/teamrecords/target/lowestunreduced"

/**
 * Defines the route parameters used in the endpoints for retrieving team records.
 *
 * The parameters in this route include:
 * - `{matchType}`: Specifies the type of match (e.g., test, ODI, T20).
 * - `{teamId}`: The unique identifier of the team.
 * - `{opponentsId}`: The unique identifier of the opposing team.
 * - `{format?}` (optional): Specifies the format of the match (e.g., day/night).
 *
 * These parameters are used to extract query information from the client request
 * and validate the input to fetch relevant data for a variety of endpoints related
 * to team records.
 */
private const val ROUTE_PARAMETERS = "/{matchType}/{teamId}/{opponentsId}/{format?}"

/**
 * Configures routing for the team records endpoints. This function sets up routes for various
 * categories of team records, such as overall records, records by series, season, year,
 * grounds, host countries, opponents, innings-by-innings, and match totals.
 *
 * The routes are authenticated using JWT and ensure that requests are validated for required
 * parameters before processing. Validation errors are appropriately handled, while successful
 * validations are passed on to the respective use cases for processing team records.
 *
 * The main categories handled by these routes include:
 * - Overall team statistics
 * - Team statistics by series
 * - Team statistics grouped by season
 * - Team statistics grouped by the year of the match start
 * - Team statistics based on the ground where the matches were played
 * - Team statistics categorized by the host country of the matches
 * - Team statistics segmented by opposing teams
 * - Team statistics on an innings-by-innings basis
 * - Match total statistics for individual players
 *
 * Dependency injection is used to provide an instance of `TeamRecordsUseCases`, which contains
 * the various use cases necessary for processing these routes.
 *
 * Validation of request parameters is done using `validateParameters`, which checks for issues
 * and returns either a list of errors or validated parameters. In case of validation errors,
 * errors are returned to the client. If validation passes, the respective use case is invoked
 * to process the request and provide a response.
 */
fun Application.routeTeamRecords() {

    val teamRecordsUseCases by inject<TeamRecordsUseCases>()

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
                            handleGetOverallTeamUseCase(
                                requestParameters,
                                validatedParams,
                                teamRecordsUseCases.getTeamOverallTeamRecords,
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
                                teamRecordsUseCases.getTeamSeriesUseCase,
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
                                teamRecordsUseCases.getTeamSeasonUseCase,
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
                                teamRecordsUseCases.getTeamByYearOfMatchStartUseCase,
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
                                teamRecordsUseCases.getTeamGroundsUseCase,
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
                                teamRecordsUseCases.getTeamByHostCountryUseCase,
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
                                teamRecordsUseCases.getTeamByOpponentsUseCase,
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
                                teamRecordsUseCases.getTeamInningsByInningsRecords,
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
                                teamRecordsUseCases.getTeamMatchTotalsRecords,
                                call
                            )
                        }
                    )
                }
            }
            route(MATCH_RESULTS_ROUTE) {
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
                            handleGetMatchResultsCase(
                                requestParameters,
                                validatedParams,
                                teamRecordsUseCases.getTeamMatchResultsRecords,
                                call
                            )
                        }
                    )
                }
            }
            route(EXTRAS_OVERALL_ROUTE) {
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
                            handleGetOverallExtras(
                                requestParameters,
                                validatedParams,
                                teamRecordsUseCases.getTeamOverallExtras,
                                call
                            )
                        }
                    )
                }
            }
            route(EXTRAS_BYINNINGS_ROUTE) {
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
                            handleGetInningsExtras(
                                requestParameters,
                                validatedParams,
                                teamRecordsUseCases.getTeamExtrasByInnings,
                                call
                            )
                        }
                    )
                }
            }
            route(TARGET_HIGHEST_ROUTE) {
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
                            handleGetHighestTotals(
                                requestParameters,
                                validatedParams,
                                teamRecordsUseCases.getTeamHighestTotalChased,
                                call
                            )
                        }
                    )
                }
            }
            route(TARGET_LOWEST_ROUTE) {
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
                            handleGetLowestTargetDefended(
                                requestParameters,
                                validatedParams,
                                teamRecordsUseCases.getTeamLowestTargetDefended,
                                call
                            )
                        }
                    )
                }
            }
            route(TARGET_LOWEST_UNREDUCEDROUTE) {
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
                            handleGetLowestTargetDefendedUnreduced(
                                requestParameters,
                                validatedParams,
                                teamRecordsUseCases.getTeamLowestTargetDefendedInUnreducedMatch,
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
 * Handles the overall team use case by processing the input parameters, performing the use case operation,
 * and responding with the appropriate result.
 *
 * @param parameters The raw request parameters provided in the API call.
 * @param validatedParams The pre-validated team search parameters.
 * @param getOverallTeamUseCase The use case that retrieves overall team statistics using the search parameters.
 * @param call The Ktor `ApplicationCall` object for managing the HTTP request and response.
 */
private suspend fun handleGetOverallTeamUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getOverallTeamUseCase: GetOverallTeamUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getOverallTeamUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles a use case to fetch team data by individual series using the provided parameters.
 *
 * This function processes the request by converting raw request parameters into validated search parameters,
 * invokes the use case to fetch data for individual series, and responds with the results in a success envelope.
 *
 * @param parameters The raw request parameters containing filtering, sorting, and paging details.
 * @param validatedParams The validated team search parameters to ensure consistency and correctness.
 * @param getIndividualSeriesUseCase The use case responsible for fetching individual series data.
 * @param call The ApplicationCall instance used to send the response back to the caller.
 */
private suspend fun handleBySeriesUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getIndividualSeriesUseCase: GetTeamSeriesUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getIndividualSeriesUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the use case for searching team statistics by individual season.
 * This method integrates parameters and processes the request to respond with the relevant data.
 *
 * @param parameters The raw set of request parameters provided by the client.
 * @param validatedParams The validated team search parameters ensuring the consistency of query data.
 * @param getIndividualSeasonUseCase The use case responsible for fetching team data for a particular season.
 * @param call The application call used for responding to the client.
 */
private suspend fun handleBySeasonUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getIndividualSeasonUseCase: GetTeamSeasonUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getIndividualSeasonUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the use case for retrieving team data filtered by the year of the match start.
 * This method processes incoming request parameters and transforms them into validated
 * search parameters for querying the appropriate team data. The method then invokes the
 * use case logic and sends the result as an HTTP response.
 *
 * @param parameters Contains raw request parameters from the client, including details about
 *                   match filters such as type, teams, venue, date range, and paging settings.
 * @param validatedParams Represents the structured and validated search criteria derived from
 *                        the initial request parameters, ensuring consistency with domain rules.
 * @param getIndividualByYearOfMatchStartUseCase The use case handling the execution logic for
 *                                               retrieving team data filtered by the year in
 *                                               which matches started.
 * @param call Represents the HTTP application call, used to send back responses to the client.
 */
private suspend fun handleByYearOfMatchStartUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getIndividualByYearOfMatchStartUseCase: GetTeamByYearOfMatchStartUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getIndividualByYearOfMatchStartUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the use case of fetching team statistics for individual grounds based on provided parameters.
 *
 * This method processes the request by extracting and transforming search parameters,
 * invokes the use case to fetch the relevant data, and responds with the results.
 *
 * @param parameters The request parameters containing details like match type, team IDs,
 *                   venue, date range, sorting preferences, and paging configurations.
 * @param validatedParams Pre-validated search parameters for team statistics queries,
 *                        used as a base for constructing the final query parameters.
 * @param getIndividualGroundsUseCase The use case responsible for fetching statistics
 *                                    by individual grounds from the repository.
 * @param call The application call context, used to send the response with data or status.
 */
private suspend fun handleByGroundsUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getIndividualGroundsUseCase: GetTeamGroundsUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getIndividualGroundsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles fetching team data filtered by host country and responds with the results.
 *
 * This method prepares the search parameters by combining the input parameters and validated parameters,
 * invokes the use case to fetch data filtered by host country, and then responds with the resulting data.
 *
 * @param parameters The request parameters provided for the search.
 * @param validatedParams The validated search parameters specific to team criteria.
 * @param getByHostCountryUseCase The use case responsible for fetching team data filtered by host country.
 * @param call The application call object for managing the HTTP request and response.
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
 * Handles the use case for retrieving team data filtered by opponents and returns the results
 * in a success response to the application call.
 *
 * This method prepares the search parameters by merging the provided request parameters with
 * pre-validated team search parameters, executes the use case to fetch filtered data, and
 * responds to the HTTP call with the results encapsulated in an envelope of success.
 *
 * @param parameters The raw request parameters containing filter criteria such as match type,
 * team details, venue, date range, sorting preferences, and pagination details.
 * @param validatedParams The pre-validated team search parameters ensuring correctness and consistency
 * for the search criteria.
 * @param getByOpponentsUseCase The use case instance responsible for executing the data fetching by opponents.
 * @param call The HTTP application call used to send the response back to the client.
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
 * Handles the retrieval of innings-by-innings team statistics for individual players.
 *
 * This method processes the input parameters, validates and constructs search parameters,
 * invokes the use case to fetch the data, and responds with the result.
 *
 * @param parameters The raw request parameters that provide filtering, sorting, and paging information.
 * @param validatedParams The pre-validated and structured team search parameters.
 * @param getInningsByInningsUseCase The use case responsible for fetching the innings-by-innings data.
 * @param call The HTTP application call that is used to send the response.
 */
private suspend fun handleGetInningsByInningsUseCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getInningsByInningsUseCase: GetTeamInningsByInningsUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getInningsByInningsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Handles the process of fetching match totals information based on input parameters and responding back to the client.
 *
 * This function combines the input parameters with validated parameters to construct
 * the final search parameters. It then invokes the use case to fetch data, and sends
 * a response with the retrieved data.
 *
 * @param parameters The raw request parameters provided by the API client.
 * @param validatedParams Validated search parameters that ensure consistency and correctness.
 * @param getMatchTotalsUseCase The use case responsible for retrieving individual match totals based on the search parameters.
 * @param call The application call object used for sending the response back to the client.
 */
private suspend fun handleGetMatchTotalsCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getMatchTotalsUseCase: GetTeamMatchTotalsUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getMatchTotalsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

private suspend fun handleGetMatchResultsCase(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    getMatchTotalsUseCase: GetTeamMatchResultsUseCase,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = getMatchTotalsUseCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

private suspend fun handleGetOverallExtras(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    useCase: GetTeamOverallExtras,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = useCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

private suspend fun handleGetInningsExtras(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    useCase: GetTeamExtrasByInnings,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = useCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

private suspend fun handleGetHighestTotals(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    useCase: GetTeamHighestTotalChased,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = useCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

private suspend fun handleGetLowestTargetDefended(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    useCase: GetTeamLowestTargetDefended,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = useCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

private suspend fun handleGetLowestTargetDefendedUnreduced(
    parameters: RequestParameters,
    validatedParams: ValidatedSearchParameters,
    useCase: GetTeamLowestTargetDefendedInUnreducedMatch,
    call: ApplicationCall
) {
    val searchParameters = getSearchParameters(validatedParams, parameters)
    val data = useCase(searchParameters)
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}

/**
 * Creates and returns an updated instance of `ValidatedTeamSearchParameters` by copying
 * the input `validatedParams` and applying modifications based on the provided `parameters`.
 *
 * @param validatedParams The validated team search parameters that need to be updated.
 * @param parameters The request parameters containing the new values to update the validated parameters.
 * @return A new instance of `ValidatedTeamSearchParameters` with updates from the request parameters.
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
        isTeamBattingRecord = parameters.isTeamBattingRecord
    )
}

