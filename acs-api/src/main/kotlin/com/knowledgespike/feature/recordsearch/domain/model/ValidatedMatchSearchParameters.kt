package com.knowledgespike.feature.recordsearch.domain.model

import com.knowledgespike.core.type.values.EpochDate

/**
 * Represents the set of validated search parameters for a match query.
 *
 * This class contains the parameters required to search for matches based
 * on details like the teams involved, the date range of the match, venue,
 * match results, and the type of match.
 *
 * @property homeTeam The name of the home team participating in the match.
 * @property awayTeam The name of the away team participating in the match.
 * @property homeTeamExactMatch Indicates if the search for the home team should be an exact match.
 * @property awayTeamExactMatch Indicates if the search for the away team should be an exact match.
 * @property startDate The start date of the matches to search for, in epoch time format.
 * @property endDate The end date of the matches to search for, in epoch time format.
 * @property venue The optional identifier for the venue where the match took place.
 * @property matchResult The optional filter for match results (e.g., win, loss, draw).
 * @property matchType The optional type of match (e.g., test, ODI, T20).
 */
data class ValidatedMatchSearchParameters(
    val homeTeam: String,
    val awayTeam: String,
    val homeTeamExactMatch: Boolean = true,
    val awayTeamExactMatch: Boolean = true,
    val startDate: EpochDate,
    val endDate: EpochDate,
    val venue: List<Int>? = null,
    val matchResult: Int? = null,
    val matchType: String,
)

