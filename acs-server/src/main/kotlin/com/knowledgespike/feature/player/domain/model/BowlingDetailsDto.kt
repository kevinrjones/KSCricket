package com.knowledgespike.feature.player.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Data transfer object representing the bowling details of a player in a specific match.
 *
 * This class encapsulates various attributes related to a player's bowling performance,
 * including match details, player-specific statistics, and match-related metadata.
 *
 * @property playerId The unique identifier of the player.
 * @property matchId The unique identifier of the match.
 * @property matchType The type of the match (e.g., Test, ODI, T20).
 * @property matchTitle The title or name of the match.
 * @property team The name of the player's team.
 * @property opponents The name of the opposing team.
 * @property ground The venue where the match was held.
 * @property matchStartDate The start date of the match.
 * @property inningsNumber The number representing the innings in which the player bowled.
 * @property runs The total number of runs conceded by the player.
 * @property balls The total number of balls bowled by the player.
 * @property wickets The number of wickets taken by the player.
 * @property maidens The number of maiden overs bowled by the player.
 * @property dots The number of dot balls delivered by the player.
 * @property noBalls The total number of no-balls bowled by the player.
 * @property wides The total number of wides bowled by the player.
 * @property fours The number of fours conceded by the player.
 * @property sixes The number of sixes conceded by the player.
 * @property captain Indicates whether the player was the captain during the match.
 * @property ballsPerOver The number of balls per over as per the match format.
 */
@Serializable
data class BowlingDetailsDto(
    val playerId: Int,
    val matchId: String,
    val matchType: String,
    val matchTitle: String,
    val team: String,
    val opponents: String,
    val ground: String,
    val matchStartDate: LocalDate,
    val inningsNumber: Int,
    val runs: Int,
    val balls: Int,
    val wickets: Int,
    val maidens: Int,
    val dots: Int,
    val noBalls: Int,
    val wides: Int,
    val fours: Int,
    val sixes: Int,
    val captain: Boolean,
    val ballsPerOver: Int
)