package com.knowledgespike.feature.recordsearch.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Data class representing details of a local match.
 *
 * This class contains information about the teams involved, the venue,
 * the date when the match took place, and other metadata related to the match.
 *
 * @property caId Unique identifier for the match.
 * @property homeTeamName Name of the home team participating in the match.
 * @property awayTeamName Name of the away team participating in the match.
 * @property location The venue or location of the match.
 * @property locationId Numeric identifier for the location.
 * @property matchStartDate The date when the match started.
 * @property matchTitle The title or designation of the match.
 * @property tournament The tournament or competition associated with the match.
 * @property resultString The result or outcome of the match in string format.
 */
data class LocalMatchListDto(
    val caId: String,
    val homeTeamName: String,
    val awayTeamName: String,
    val location: String,
    val locationId: Int,
    val matchStartDate: LocalDate,
    val matchTitle: String,
    val tournament: String,
    val resultString: String
)

/**
 * Data transfer object representing a list of matches along with their details.
 *
 * This class contains attributes that define various properties of a match, such as
 * the teams participating, the match location, date, title, and tournament information.
 * It also specifies other details like the result of the match and a unique key
 * for identification purposes.
 *
 * @property caId A unique identifier for the competition or match.
 * @property homeTeamName The name of the home team participating in the match.
 * @property awayTeamName The name of the away team participating in the match.
 * @property location The location or stadium where the match is played.
 * @property locationId The numerical identifier for the match location.
 * @property matchDate The date on which the match is scheduled or took place, as a string.
 * @property matchTitle A title or description associated with the match.
 * @property tournament The name of the tournament in which the match is being played.
 * @property resultString A string representation of the match result, e.g., "Win", "Draw".
 * @property key A unique identifier or key for the match.
 */
@Serializable
data class MatchListDto(
    val caId: String,
    val homeTeamName: String,
    val awayTeamName: String,
    val location: String,
    val locationId: Int,
    val matchDate: String,
    val matchTitle: String,
    val tournament: String,
    val resultString: String,
    val key: String,
)
