package com.knowledgespike.feature.recordsearch.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a type of match entity with its associated metadata.
 *
 * This data class is used to define a match type with a unique identifier,
 * its type, and a description providing additional context or details about the match type.
 *
 * @property id A unique identifier for the match type.
 * @property type The type or classification of the match (e.g., test, ODI, T20).
 * @property description A detailed description of the match type.
 */
@Serializable

data class MatchTypeEntity(val id: Int, val type: String, val description: String)
/**
 * Represents the properties of a competition entity.
 *
 * This class provides the structure to store essential details about a competition,
 * including its unique identifier, type, and name. It can be serialized for usage
 * in data transfer or storage.
 *
 * @property id The unique identifier of the competition.
 * @property type The primary type or classification of the competition.
 * @property subType A more specific category or subtype of the competition.
 * @property competition The name or title of the competition.
 */
@Serializable
data class Competition(val id: Int, val type: String, val subType: String, val competition: String)

/**
 * Represents a team with a unique identifier and a name.
 *
 * @property id The unique identifier for the team.
 * @property name The name of the team.
 */
data class Team(val id: Int, val name: String)

/**
 * Represents a Ground entity with details about a specific sports ground.
 *
 * @property id The unique identifier for the ground.
 * @property matchType The type of matches played at the ground, such as Test, ODI, or T20.
 * @property code A short code or abbreviation associated with the ground.
 * @property countryName The name of the country where the ground is located.
 * @property groundId Another identifier for the ground, which may be used internally or in association with external systems.
 * @property knownAs An alternate name or nickname for the ground.
 */
data class Ground(val id: Int, val matchType:String, val code:String, val countryName: String, val groundId: Int, val knownAs: String)
/**
 * Represents a country with a unique identifier and a name.
 *
 * This class is primarily used to model a country entity within the application,
 * providing the necessary attributes for identifying and describing a country.
 *
 * @property id A unique identifier for the country.
 * @property name The name of the country.
 */
@Serializable
data class Country(val id: Int, val name: String) {
    /**
     * Companion object for the Country class providing utility methods.
     *
     * This object contains methods that offer convenience functionalities
     * related to the Country data class. These include operations for retrieving
     * default values or performing common actions associated with the Country class.
     */
    companion object {
        /**
         * Provides the default country configuration.
         *
         * This method returns a Country object representing the default state for country data,
         * typically used when no specific country is selected or required.
         *
         * @return A Country object with an ID of 0 and the name "All Countries".
         */
        fun defaultCountry(): Country =
            Country(id = 0, name = "All Countries")

    }
}

/**
 * Data class representing search data and criteria for querying records.
 *
 * This class encapsulates various data points and filters used to perform
 * a search across multiple domains such as matches, teams, competitions, and
 * geographic locations. The attributes encompass both high-level entities
 * and granular configurations to refine the search results.
 *
 * @property matchTypes A list of match types, represented as `MatchTypeEntity`, that specify the types of matches to filter.
 * @property pageSizes A list of integers specifying page size options for paginated search results.
 * @property competitions A list of competitions, represented as `Competition`, indicating specific tournaments or contests.
 * @property teams A list of teams, represented as `Team`, to include in the search criteria.
 * @property grounds A list of grounds, represented as `Ground`, specifying venue options for matches.
 * @property countries A list of countries, represented as `Country`, to restrict or filter geographical scope.
 * @property seriesDates A list of date strings representing series dates for filtering records.
 * @property startAndEndDate A `StartEndDate` object defining the start and end date range for the search.
 */
data class SearchData(
    val matchTypes: List<MatchTypeEntity> = listOf(),
    val pageSizes: List<Int> = listOf(),
    val competitions: List<Competition> = listOf(),
    val teams: List<Team> = listOf(),
    val grounds: List<Ground> = listOf(),
    val countries: List<Country> = listOf(),
    val seriesDates: List<String> = listOf(),
    val startAndEndDate: StartEndDate = StartEndDate(),
)
