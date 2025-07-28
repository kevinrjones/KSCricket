package com.knowledgespike.core.jooq

import com.knowledgespike.core.ktor.getQueryParamOrDefault
import io.ktor.server.routing.*

/**
 * Represents the collection of parameters required to process a request for retrieving match-related data.
 *
 * The data includes filtering and sorting options such as match type, team, opponents, date range, etc.
 * These parameters are typically parsed from incoming API requests and are used to construct the search criteria.
 *
 * @property matchType The type of match (e.g., Test, ODI, T20).
 * @property teamId The unique ID of the team for which data is requested.
 * @property opponentsId The unique ID of the opposing team.
 * @property format The format of the match (optional).
 * @property matchSubType Specifies a subtype of the match if applicable.
 * @property groundId The ID of the ground where the match was played.
 * @property hostCountryId The ID of the host country.
 * @property venue The venue ID for the match.
 * @property matchResult Result ID representing the outcome of the match.
 * @property limit The limit of records for pagination.
 * @property startDate The start date of the match search window, represented in epoch seconds.
 * @property endDate The end date of the match search window, represented in epoch seconds.
 * @property season The season (e.g., year or specific competition) associated with the matches.
 * @property sortOrder The sorting priority based on a specific key.
 * @property sortDirection Specifies the direction of sorting, e.g., ascending or descending.
 * @property startRow The starting position for pagination.
 * @property pageSize The size of the page for pagination.
 */
data class RequestParameters(
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
    // used with team records
    val isTeamBattingRecord: Boolean = true,
    // used with partnershipo records
    val partnershipWicket: Int = 0,
)


/**
 * Represents the default limit value used for pagination or data retrieval operations
 * when no specific limit is provided in the query parameters. Defaults to `100`.
 */
private const val DEFAULT_LIMIT = 100
/**
 * Represents the default start date used in queries or operations that require a start date,
 * but none is explicitly provided. The value is set to 0, which typically represents
 * the epoch time in Unix timestamp format (1970-01-01T00:00:00Z).
 */
private const val DEFAULT_START_DATE = 0L
/**
 * Represents the default ground ID used when no ground ID is explicitly specified in the request.
 * Acts as a fallback for parameters such as `groundId`, `venue`, and `matchResult`
 * in cases where a value is not provided in the request query or parameters.
 */
private const val DEFAULT_GROUND_ID = 0
/**
 * Represents the default identifier for the host country used when no specific host country ID
 * is provided in the query parameters. This constant is used as a fallback value for the
 * `hostCountryId` parameter in various operations.
 *
 * The value is set to `0`, typically indicating a neutral or undefined state for the host country.
 */
private const val DEFAULT_HOST_COUNTRY_ID = 0
/**
 * Represents the default starting row index for paginated queries.
 * Used when a starting row is not explicitly provided in the request parameters.
 */
private const val DEFAULT_START_ROW = 0
/**
 * The default number of items to display per page when paginating data.
 * Used as a fallback value for query parameters or method logic
 * where the number of items per page is not specified.
 */
private const val DEFAULT_PAGE_SIZE = 50
/**
 * Extracts and constructs request parameters from the provided routing call to be used for various operations.
 *
 * @param call The RoutingCall object representing the current HTTP request context.
 * @return A populated RequestParameters object with extracted and processed parameter values.
 */
fun extractRequestParameters(call: RoutingCall): RequestParameters {
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
    )
}