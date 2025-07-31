package com.knowledgespike.feature.player.domain.model

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing the overall performance of a player.
 *
 * This class provides comprehensive statistics about a player's performance in cricket,
 * including batting and optional bowling details across different match types or teams.
 *
 * @property team The name of the team the player represented.
 * @property matchType The type of matches considered (e.g., Test, ODI, T20).
 * @property teamId The unique identifier of the player's team.
 * @property matches The total number of matches played.
 * @property runs The total number of runs scored by the player.
 * @property innings The total number of innings batted by the player.
 * @property notouts The number of times the player was not out.
 * @property balls The total number of balls faced by the player.
 * @property fours The number of boundaries (fours) scored by the player.
 * @property sixes The number of sixes scored by the player.
 * @property hundreds The total number of centuries scored by the player.
 * @property fifties The total number of half-centuries scored by the player.
 * @property highestScore The highest score achieved by the player.
 * @property bowlingBalls The total number of balls bowled by the player, if applicable.
 * @property bowlingRuns The total number of runs conceded by the player while bowling, if applicable.
 * @property maidens The total number of maiden overs bowled by the player, if applicable.
 * @property wickets The total number of wickets taken by the player, if applicable.
 * @property bowlingFours The number of fours conceded by the player while bowling, if applicable.
 * @property bowlingSixes The number of sixes conceded by the player while bowling, if applicable.
 * @property wides The total number of wides bowled by the player, if applicable.
 * @property noBalls The total number of no-balls bowled by the player, if applicable.
 */
@Serializable
data class PlayerOverallDto(
    val team: String,
    val matchType: String,
    val teamId: Int,
    val matches: Int,
    val runs: Int,
    val innings: Int,
    val notouts: Int,
    val balls: Int,
    val fours: Int,
    val sixes: Int,
    val hundreds: Int,
    val fifties: Int,
    val highestScore: Double,
    val bowlingBalls: Int?,
    val bowlingRuns: Int?,
    val maidens: Int?,
    val wickets: Int?,
    val bowlingFours: Int?,
    val bowlingSixes: Int?,
    val wides: Int?,
    val noBalls: Int?
)
