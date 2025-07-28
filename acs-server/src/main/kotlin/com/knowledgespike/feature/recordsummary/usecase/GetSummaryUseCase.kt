package com.knowledgespike.feature.recordsummary.usecase

import arrow.core.NonEmptyList
import arrow.core.raise.Raise
import arrow.core.raise.zipOrAccumulate
import arrow.core.right
import com.knowledgespike.core.type.dto.Country
import com.knowledgespike.core.type.dto.Ground
import com.knowledgespike.core.type.dto.Team
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.values.CountryId
import com.knowledgespike.core.type.values.GroundId
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.core.type.values.TeamId
import com.knowledgespike.feature.recordsummary.domain.model.SearchSummary
import com.knowledgespike.feature.recordsummary.repository.SearchSummaryRepository

/**
 * Use case to retrieve match summaries based on provided criteria such as match type, teams involved,
 * ground, and host country. This class interacts with a `SearchSummaryRepository` to gather the necessary
 * data and orchestrates the creation of a `SearchSummary`.
 *
 * @param repository An instance of `SearchSummaryRepository` to fetch data required for constructing summaries.
 */
class GetSummaryUseCase(val repository: SearchSummaryRepository) {

    /**
     * A lookup map associating match type codes to their full descriptive names.
     * This data structure is used to map concise match type identifiers, such as "wt" or "t",
     * to their corresponding human-readable descriptions, such as "Women's Tests" or "Men's Tests".
     *
     * Key details:
     * - The keys represent abbreviated match type codes.
     * - The values provide the full descriptions for those codes, categorized by gender and format.
     * - This allows functional retrieval of match type names based on their code.
     */
    private val matchTypeLookup = mapOf(
        "wt" to "Women's Tests",
        "wo" to "Women's One Day Internationals",
        "witt" to "Women's International T20",
        "wf" to "Women's First Class",
        "wa" to "Women's ListA",
        "wtt" to "Women's T20",
        "wmisc" to "Women's Miscellaneous",
        "t" to "Men's Tests",
        "o" to "Men's One Day Internationals",
        "itt" to "Men's International T20",
        "f" to "Men's First Class",
        "a" to "Men's ListA",
        "tt" to "Men's T20",
        "sec" to "Men's Second XI Championship",
        "minc" to "Men's Minor Counties Championship",
        "mint" to "Men's Minor Counties Trophy",
        "mintt" to "Men's Minor Counties T20",
    )

    /**
     * Executes a summary search for a match based on the specified criteria.
     *
     * This function combines multiple inputs including match type, team IDs, ground ID,
     * and host country ID to generate a summarized representation of the match details.
     * It encapsulates error handling through the Raise context for potential issues during data retrieval.
     *
     * @param matchType The type of match to be searched (e.g., Test, One Day, T20).
     * @param teamId The identifier of the team involved in the match.
     * @param opponentsId The identifier of the opposing team in the match.
     * @param groundId The identifier of the ground/venue where the match took place.
     * @param hostCountryId The identifier of the country hosting the match.
     *
     * @return A [SearchSummary] object encapsulating the details of the match including team names,
     *         match type, ground, and host country. If errors occur during any data retrieval step,
     *         an accumulated error from [NonEmptyList] is raised instead.
     */
    context(Raise<NonEmptyList<Error>>)
    operator fun invoke(
        matchType: MatchType,
        teamId: TeamId,
        opponentsId: TeamId,
        groundId: GroundId,
        hostCountryId: CountryId
    ): SearchSummary =

        zipOrAccumulate(
            {
                (matchTypeLookup[matchType.value] ?: "Unknown").right().bind()
            },
            { (if (teamId.id == 0) Team(0, "All Teams").right() else repository.getTeam(teamId)).bind() },
            { (if (opponentsId.id == 0) Team(0, "All Teams").right() else repository.getTeam(opponentsId)).bind() },
            {
                (if (hostCountryId.id == 0) Country(0, "All Countries").right() else repository.getHostCountry(
                    hostCountryId
                )).bind()
            },
            {
                (if (groundId.id == 0) Ground(
                    0,
                    "",
                    code = "",
                    knownAs = "All Grounds",
                    groundId = 0,
                    countryName = ""
                ).right()
                else repository.getGround(groundId)).bind()
            }
        ) { matchTypeName, team, opponents, country, ground ->
            SearchSummary(team.name, opponents.name, matchTypeName, ground.knownAs, country.name)
        }
}

