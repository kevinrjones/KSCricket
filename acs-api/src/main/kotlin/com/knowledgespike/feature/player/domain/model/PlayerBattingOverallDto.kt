package com.knowledgespike.feature.player.domain.model

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing a player's overall batting statistics.
 *
 * This class encapsulates various attributes related to a player's cumulative batting performance
 * across multiple matches, grouped by a specific match type and team.
 *
 * @property matchType The type of matches for which the statistics are aggregated (e.g., Test, ODI, T20).
 * @property teamId The unique identifier for the team associated with the player's statistics.
 * @property team The name of the team associated with the player's statistics.
 * @property matches The total number of matches played by the player.
 * @property runs The total number of runs scored by the player.
 * @property innings The total number of innings batted by the player.
 * @property notouts The total number of times the player remained not out.
 * @property balls The total number of balls faced by the player.
 * @property fours The total number of boundaries (fours) scored by the player.
 * @property sixes The total number of sixes scored by the player.
 * @property hundreds The total number of centuries scored by the player.
 * @property fifties The total number of half-centuries scored by the player.
 * @property highestScore The highest individual score achieved by the player.
 */
@Serializable
data class PlayerBattingOverallDto(
    val matchType: String,
    val teamId: Int,
    val team: String,
    val matches: Int,
    val runs: Int,
    val innings: Int,
    val notouts: Int,
    val balls: Int,
    val fours: Int,
    val sixes: Int,
    val hundreds: Int,
    val fifties: Int,
    val highestScore: Double
)