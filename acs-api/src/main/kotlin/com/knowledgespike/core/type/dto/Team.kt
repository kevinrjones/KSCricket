package com.knowledgespike.core.type.dto

import kotlinx.serialization.Serializable

/**
 * Represents a team with an identifier, name, and optional match type.
 *
 * This data class is used to model the basic details of a team, such as its unique ID,
 * name, and type of match it is associated with, if specified. It can be serialized
 * using Kotlin serialization.
 *
 * @property id The unique identifier of the team.
 * @property name The name of the team.
 * @property matchType The type of match the team participates in, defaulting to an empty string if not specified.
 */
@Serializable
data class Team(val id: Int, val name: String, val matchType: String = "")

