package com.knowledgespike.feature.recordsummary.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.knowledgespike.DIALECT
import com.knowledgespike.core.type.dto.Country
import com.knowledgespike.core.type.dto.Ground
import com.knowledgespike.core.type.dto.Team
import com.knowledgespike.core.type.error.CountryIdError
import com.knowledgespike.core.type.error.GroundIdError
import com.knowledgespike.core.type.error.TeamIdError
import com.knowledgespike.core.type.values.CountryId
import com.knowledgespike.core.type.values.GroundId
import com.knowledgespike.core.type.values.TeamId
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.GROUNDSMATCHTYPES
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsummary.repository.SearchSummaryRepository
import com.knowledgespike.plugins.DataSource
import org.jooq.impl.DSL

/**
 * Implementation of the SearchSummaryRepository interface,
 * providing database operations using jOOQ for accessing teams, grounds, and host countries.
 *
 * The repository uses a provided DataSource for database access. It performs queries specific
 * to fetching details about teams, grounds, and countries by their respective identifiers.
 */
class JooqSearchSummaryRepository(private val dataSource: DataSource) : SearchSummaryRepository {
    /**
     * Retrieves the team information based on the provided team ID.
     *
     * This method queries the database using the given `teamId` and returns
     * either a `Team` object if the team is found or a `TeamIdError` if the team cannot be located.
     *
     * @param teamId The identifier of the team to retrieve, encapsulated in a `TeamId` value class.
     * @return An `Either` containing either a `TeamIdError` if an error occurs or the `Team` object if successful.
     */
    override fun getTeam(teamId: TeamId): Either<TeamIdError, Team> {

        val result = DSL.using(
            dataSource.dataSource,
            DIALECT
        ).select(
            TEAMS.ID,
            TEAMS.NAME
        ).from(TEAMS)
            .where(TEAMS.ID.eq(teamId.id))
            .limit(1)
            .fetchOne()


        return if (result != null) {
            Team(result[TEAMS.ID]!!, result[TEAMS.NAME]!!).right()
        } else {
            TeamIdError("The 'teamId' ${teamId.id} is found in the database", teamId.id).left()
        }
    }

    /**
     * Retrieves a ground by its unique identifier.
     *
     * This method queries the database to fetch details about a ground
     * using the provided `groundId`. If a corresponding ground is found in the database,
     * it returns the ground information wrapped in an `Either.Right`. If the `groundId`
     * is invalid or not found in the database, it returns an error wrapped in an `Either.Left`.
     *
     * @param groundId The unique identifier of the ground to be retrieved.
     * @return Either a `GroundIdError` indicating an issue with the `groundId`, or
     * a `Ground` object containing the details of the requested ground.
     */
    override fun getGround(groundId: GroundId): Either<GroundIdError, Ground> {
        val result = DSL.using(
            dataSource.dataSource,
            DIALECT
        ).select(
            GROUNDS.ID,
            GROUNDS.GROUNDID,
            GROUNDS.KNOWNAS,
            GROUNDS.COUNTRYNAME
        ).from(GROUNDS)
            .where(GROUNDS.ID.eq(groundId.id))
            .limit(1)
            .fetchOne()


        return if (result != null) {
            Ground(
                id = result[GROUNDS.ID]!!,
                matchType = "",
                knownAs = result[GROUNDS.KNOWNAS]!!,
                code = "",
                countryName = result[GROUNDS.COUNTRYNAME]!!,
                groundId = result[GROUNDS.GROUNDID]!!,
            ).right()
        } else {
            GroundIdError("The 'groundId' ${groundId.id} is found in the database", groundId.id).left()

        }
    }

    /**
     * Retrieves the host country associated with a given country ID.
     *
     * This method queries the database to find the corresponding country information
     * for the provided country ID. It returns either the country details if found
     * or an error if the country ID is not present in the database.
     *
     * @param countryId The identifier of the country for which the host country details are requested.
     * @return Either a `CountryIdError` if the country ID is not found, or a `Country` object containing the country details.
     */
    override fun getHostCountry(countryId: CountryId): Either<CountryIdError, Country> {
        val result = DSL.using(dataSource.dataSource, DIALECT)
            .select(
                GROUNDS.ID,
                GROUNDS.COUNTRYNAME,
                GROUNDSMATCHTYPES.MATCHTYPE
            ).from(GROUNDS).join(GROUNDSMATCHTYPES).on(GROUNDS.ID.eq(GROUNDSMATCHTYPES.GROUNDID))
            .where(GROUNDS.COUNTRYID.eq(countryId.id))
            .limit(1).fetchOne()

        return if (result != null) {
            Country(id = result[GROUNDS.ID]!!, name = result[GROUNDS.COUNTRYNAME]!!).right()
        } else {
            CountryIdError("The 'countryId' ${countryId.id} is found in the database", countryId.id).left()
        }
    }
}