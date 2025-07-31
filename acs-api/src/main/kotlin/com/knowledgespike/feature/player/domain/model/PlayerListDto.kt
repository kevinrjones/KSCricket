package com.knowledgespike.feature.player.domain.model

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing a simplified list of players.
 *
 * This class provides a minimal representation of a player's core details, intended
 * for use cases where only summary information about players is required.
 *
 * @property id The unique identifier of the player.
 * @property fullName The complete name of the player.
 * @property teams A string representation of the teams the player has been associated with.
 * @property debut The date of the player's debut in ISO-8601 format (YYYY-MM-DD).
 * @property activeUntil The date until which the player was active in their career, in ISO-8601 format (YYYY-MM-DD).
 */
@Serializable
data class PlayerListDto(
    val id: Int,
    val fullName: String,
    val teams: String,
    val debut: String,
    val activeUntil: String
)