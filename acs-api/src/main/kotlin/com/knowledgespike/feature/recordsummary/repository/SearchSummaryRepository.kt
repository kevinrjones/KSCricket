package com.knowledgespike.feature.recordsummary.repository

import arrow.core.Either
import com.knowledgespike.core.type.dto.Country
import com.knowledgespike.core.type.dto.Ground
import com.knowledgespike.core.type.dto.Team
import com.knowledgespike.core.type.error.CountryIdError
import com.knowledgespike.core.type.error.GroundIdError
import com.knowledgespike.core.type.error.TeamIdError
import com.knowledgespike.core.type.values.CountryId
import com.knowledgespike.core.type.values.GroundId
import com.knowledgespike.core.type.values.TeamId

/**
 * Repository interface to fetch and manage summary data related to teams, grounds, and host countries.
 * It provides mechanisms to retrieve specific entities using their unique identifiers.
 */
interface SearchSummaryRepository {
    /**
     * Retrieves the team associated with the given team ID.
     *
     * @param teamId The unique identifier of the team to retrieve.
     * @return An `Either` containing a `TeamIdError` if the team cannot be retrieved,
     *         or the corresponding `Team` if the lookup is successful.
     */
    fun getTeam(teamId: TeamId): Either<TeamIdError, Team>
    /**
     * Retrieves the ground information corresponding to the specified ground ID.
     *
     * @param groundId The unique identifier for the ground to be retrieved.
     * @return Either an error of type `GroundIdError` if the ID is invalid or the corresponding `Ground` object.
     */
    fun getGround(groundId: GroundId): Either<GroundIdError, Ground>
    /**
     * Retrieves the host country associated with the provided country identifier.
     *
     * @param countryId The unique identifier of the country to retrieve.
     * @return Either a `CountryIdError` if the country identifier is invalid or the corresponding `Country` data.
     */
    fun getHostCountry(countryId: CountryId): Either<CountryIdError, Country>
}