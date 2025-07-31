package com.knowledgespike.feature.recordsummary.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a summary of a search in the context of match records.
 *
 * This data class encapsulates the details of a match summary, including the team, opponents,
 * type of match, venue (ground), and the country hosting the match. It serves as a lightweight
 * representation of the result of a summary query executed in a system handling cricket records.
 *
 * @property team The name of the team involved in the match.
 * @property opponents The name of the opposing team in the match.
 * @property matchType The type of match, such as "Test", "One Day", or "T20".
 * @property grounds The name of the ground or venue where the match was played.
 * @property country The name of the country where the match took place.
 */
@Serializable
data class SearchSummary(
    val team: String,
    val opponents: String,
    val matchType: String,
    val grounds: String,
    val country: String,
)