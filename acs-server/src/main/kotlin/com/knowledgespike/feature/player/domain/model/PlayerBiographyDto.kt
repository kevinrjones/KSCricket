package com.knowledgespike.feature.player.domain.model

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing a player's biography information.
 *
 * This class encapsulates the biographical details of a cricket player by aggregating
 * the properties and attributes contained within the `PlayerBiography` data class.
 * It serves as a transfer object for accessing detailed player information across various layers or modules.
 *
 * @property playerDetails A `PlayerBiography` object containing the personal and professional details of the player.
 */
@Serializable
data class PlayerBiographyDto(val playerDetails: PlayerBiography)