package com.knowledgespike.feature.recordsearch.domain.model

import com.knowledgespike.core.type.shared.PagingParameters
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.core.type.shared.SortOrder
import com.knowledgespike.core.type.values.EpochDate
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.core.type.values.TeamId

/**
 * This data class represents validated search parameters for batting statistics queries.
 *
 * It includes various parameters like match type, teams involved, venue, date range, season,
 * sorting preferences, and paging configurations. The parameters are validated to ensure
 * correct and consistent representation of the search criteria.
 *
 * @property matchType The type of match (e.g., test, one-day, T20, etc.).
 * @property matchSubType The subtype of the match that further categorizes the match type.
 * @property teamId The identifier for the team whose statistics are being queried.
 * @property opponentsId The identifier for the opposing team.
 * @property groundId The identifier for the specific ground. Defaults to 0.
 * @property hostCountryId The identifier for the host country. Defaults to 0.
 * @property venue The identifier for the specific venue. Defaults to 0.
 * @property startDate The start date for the search range in epoch seconds. Defaults to the minimum value.
 * @property endDate The end date for the search range in epoch seconds. Defaults to the minimum value.
 * @property season The season name or identifier for filtering matches. Defaults to an empty string.
 * @property sortOrder The criteria used to sort the results (e.g., Runs, Matches, etc.). Defaults to `SortOrder.Runs`.
 * @property sortDirection The direction of sorting (ascending or descending). Defaults to ascending.
 * @property result An optional filter based on the result of the match. Defaults to 0.
 * @property pagingParameters Specifies paging configuration like starting row, page size, and limit.
 * @property isTeamBattingRecord For the team trecords, is this for the batting or bowling team
 * @property partnershipWicket which wicket are we generating records for
 */
data class ValidatedSearchParameters(
    val matchType: MatchType,
    val matchSubType: MatchType,
    val teamId: TeamId,
    val opponentsId: TeamId,
    val groundId: Int = 0,
    val hostCountryId: Int = 0,
    val venue: Int = 0,
    val startDate: EpochDate = EpochDate.minimum(),
    val endDate: EpochDate = EpochDate.minimum(),
    val season: String = "",
    val sortOrder: SortOrder = SortOrder.Runs,
    val sortDirection: SortDirection = SortDirection.ASC,
    val result: Int = 0,
    val pagingParameters: PagingParameters = PagingParameters(0, 50, 50),
    val fivesLimit: Int = 5,
    val isTeamBattingRecord: Boolean = true,
    val partnershipWicket: Int = 0
)