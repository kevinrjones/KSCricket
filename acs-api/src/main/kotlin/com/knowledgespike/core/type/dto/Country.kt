package com.knowledgespike.core.type.dto

import kotlinx.serialization.Serializable

/**
 * Represents a country with its associated details.
 *
 * This data class encapsulates the information related to a country, including its
 * unique identifier, name, and the match type associated with it. The class is useful
 * for scenarios where countries are used as part of a search or data query, such as
 * retrieving host country information or related match types.
 *
 * @property id The unique identifier for the country.
 * @property name The name of the country.
 * @property matchType The type of match associated with the country, defaulting to an empty string.
 */
@Serializable
data class Country(val id: Int, val name: String, val matchType: String = "")