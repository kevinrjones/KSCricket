package com.knowledgespike.core.type.dto

import kotlinx.serialization.Serializable

/**
 * Represents a cricket ground with details such as its unique identifier,
 * type of matches it supports, associated country, and other descriptive information.
 *
 * @property id The unique identifier for the ground, typically assigned by the database or system.
 * @property matchType The type of matches hosted at this ground (e.g., Test, One Day, T20).
 * @property code A short code or abbreviation for the ground name.
 * @property countryName The name of the country where the ground is located.
 * @property groundId A secondary identifier specific to the ground, often used for domain-related operations.
 * @property knownAs Commonly used name or nickname for the ground.
 */
@Serializable
data class Ground(val id: Int, val matchType:String, val code:String, val countryName: String, val groundId: Int, val knownAs: String)