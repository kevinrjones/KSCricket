package com.knowledgespike.feature.player.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Data transfer object representing the batting details of a player in a specific match.
 *
 * This class encapsulates various attributes related to a player's batting performance,
 * including match details, player-specific statistics, and dismissal information.
 *
 * @property playerId The unique identifier of the player.
 * @property matchId The unique identifier of the match.
 * @property matchType The type of the match (e.g., Test, ODI, T20).
 * @property matchTitle The title or name of the match.
 * @property team The name of the player's team.
 * @property opponents The name of the opposing team.
 * @property ground The venue where the match was held.
 * @property matchStartDate The start date of the match.
 * @property inningsNumber The number representing the innings in which the player batted.
 * @property dismissal The dismissal description (e.g., "Caught", "Bowled").
 * @property dismissalType The numeric code representing the type of dismissal, if available.
 * @property fielderId The unique identifier of the fielder involved in the dismissal, if applicable.
 * @property fielderName The name of the fielder involved in the dismissal.
 * @property bowlerId The unique identifier of the bowler involved in the dismissal, if applicable.
 * @property bowlerName The name of the bowler involved in the dismissal.
 * @property score The number of runs scored by the player.
 * @property position The batting order position of the player.
 * @property notOut Indicates if the player remained not out during the innings.
 * @property balls The number of balls faced by the player, if available.
 * @property minutes The number of minutes spent by the player at the crease, if available.
 * @property fours The number of boundaries (fours) scored by the player, if available.
 * @property sixes The number of sixes scored by the player, if available.
 * @property captain Indicates if the player was the captain during the match.
 * @property wicketKeeper Indicates if the player was the wicket-keeper during the match.
 * @property sr The strike rate of the player, if available.
 */
@Serializable
data class BattingDetailsDto(
    val playerId: Int,
    val matchId: String,
    val matchType: String,
    val matchTitle: String,
    val team: String,
    val opponents: String,
    val ground: String,
    val matchStartDate: LocalDate,
    val inningsNumber: Int,
    val dismissal: String,
    val dismissalType: Int?,
    val fielderId: Int?,
    val fielderName: String,
    val bowlerId: Int?,
    val bowlerName: String,
    val score: Int,
    val position: Int,
    val notOut: Boolean,
    val balls: Int?,
    val minutes: Int?,
    val fours: Int?,
    val sixes: Int?,
    val captain: Boolean,
    val wicketKeeper: Boolean,
    val sr: Double?
)