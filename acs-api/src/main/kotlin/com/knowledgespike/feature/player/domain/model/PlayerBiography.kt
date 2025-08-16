package com.knowledgespike.feature.player.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the biographical information of a cricket player.
 *
 * This data class contains details about the player's personal and professional background,
 * including his/her full name, active career period, birth date, and other cricket-specific attributes.
 *
 * @property fullName The complete name of the player.
 * @property activeUntil The timestamp indicating the end of the player's active career.
 * @property birthDate The birth date of the player in ISO-8601 format (YYYY-MM-DD).
 * @property dateDied The date of death of the player in ISO-8601 format (YYYY-MM-DD). Can be blank if the player is alive.
 * @property battingHand The preferred batting hand of the player (e.g., Right-hand, Left-hand).
 * @property bowlingMode The bowling style of the player (e.g., Right-arm fast, Left-arm spin).
 */
@Serializable
data class PlayerBiography(
    val fullName: String,
    val expandedFullName: String,
    val activeUntil: Long,
    val birthDate: String,
    val dateDied: String,
    val battingHand: String,
    val bowlingMode: String,
)