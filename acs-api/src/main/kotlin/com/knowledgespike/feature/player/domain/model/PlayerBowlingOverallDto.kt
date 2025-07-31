package com.knowledgespike.feature.player.domain.model

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing the overall bowling performance of a player.
 *
 * This class encapsulates the aggregated bowling statistics of a player across matches,
 * categorized by match type and team. The statistics include various performance metrics
 * such as wickets taken, runs conceded, and extras delivered.
 *
 * @property matchType The type of match (e.g., Test, ODI, T20) for which the bowling statistics are recorded.
 * @property teamId The identifier of the team the player was associated with during these matches.
 * @property matches The total number of matches played by the player of the specified match type.
 * @property bowlingBalls The total number of balls bowled by the player across matches. Null if data is unavailable.
 * @property bowlingRuns The total number of runs conceded by the player across matches. Null if data is unavailable.
 * @property maidens The total number of maiden overs bowled by the player. Null if data is unavailable.
 * @property wickets The total number of wickets taken by the player. Null if data is unavailable.
 * @property bowlingFours The total number of fours conceded by the player while bowling. Null if data is unavailable.
 * @property bowlingSixes The total number of sixes conceded by the player while bowling. Null if data is unavailable.
 * @property wides The total number of wides bowled by the player. Null if data is unavailable.
 * @property noBalls The total number of no-balls bowled by the player. Null if data is unavailable.
 */
@Serializable
data class PlayerBowlingOverallDto(
    val matchType: String,
    val teamId: Int,
    val matches: Int,
    val bowlingBalls: Int?,
    val bowlingRuns: Int?,
    val maidens: Int?,
    val wickets: Int?,
    val bowlingFours: Int?,
    val bowlingSixes: Int?,
    val wides: Int?,
    val noBalls: Int?
)