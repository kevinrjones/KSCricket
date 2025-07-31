package com.knowledgespike.feature.player.data.repository

import arrow.core.Either
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.db.tables.Playersteams
import com.knowledgespike.db.tables.Teams.Companion.TEAMS
import com.knowledgespike.db.tables.references.PLAYERS
import com.knowledgespike.db.tables.references.PLAYERSTEAMS
import com.knowledgespike.feature.player.domain.model.PlayerListDto
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedPlayerSearchRequestParameters
import com.knowledgespike.feature.recordsearch.domain.model.formatFullMonths
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import org.jooq.DSLContext
import org.jooq.impl.DSL.*

/**
 * Utility object for querying player information from a database using jOOQ.
 * Provides methods for finding players based on specified search parameters.
 */
object JooqFindPlayer {
    /**
     * Searches for players based on provided search parameters and returns a list of players matching the criteria.
     *
     * @param context The DSLContext to be used for executing database queries.
     * @param parameters The validated parameters used to filter the player search results.
     * @return Either an error if the operation fails, or a list of PlayerListDto objects representing the players found.
     */
    fun findPlayer(
        context: DSLContext,
        parameters: ValidatedPlayerSearchRequestParameters
    ): Either<Error, List<PlayerListDto>> {

        val playersTeams = select(
            Playersteams.PLAYERSTEAMS.PLAYERID,
            aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
        ).from(PLAYERSTEAMS).join(TEAMS).on(PLAYERSTEAMS.TEAMID.eq(TEAMS.ID))
            .groupBy(PLAYERSTEAMS.PLAYERID).asTable("Teams")

        val sortNamePartCondition = if (parameters.sortNamePart.isEmpty()) {
            trueCondition()
        } else {
            PLAYERS.SORTNAMEPART.eq(parameters.sortNamePart)
        }
        val otherNamePartCondition = if (parameters.otherNamePart.isEmpty()) {
            trueCondition()
        } else {
            PLAYERS.OTHERNAMEPART.eq(parameters.otherNamePart)
        }
        val fullNameCondition = if (parameters.name.isEmpty()) {
            trueCondition()
        } else {
            PLAYERS.SORTNAMEPART.eq(parameters.name)
        }
        val teamNameCondition = if (parameters.team.isEmpty()) {
            trueCondition()
        } else {
            field("Teams.Teams").like("%${parameters.team}%")
        }

        val sortSpecification = when (parameters.sortDirection) {
            SortDirection.ASC -> field(parameters.sortOrder.name).asc()
            SortDirection.DESC -> field(parameters.sortOrder.name).desc()
        }

        val records = context.select(
            PLAYERS.ID,
            PLAYERS.FULLNAME,
            PLAYERS.DEBUT,
            PLAYERS.ACTIVEUNTIL,
            field("Teams.Teams").`as`("Teams")
        ).from(PLAYERS)
            .join(playersTeams).on(playersTeams.field("PlayerId", Int::class.java)?.eq(PLAYERS.ID))
            .where(
                (sortNamePartCondition)
                    .and(otherNamePartCondition)
                    .or(fullNameCondition)
            )
            .and(teamNameCondition)
            .and(PLAYERS.DEBUTASOFFSET.ge(parameters.startDate.value))
            .and(PLAYERS.ACTIVEUNTILASOFFSET.le(parameters.endDate.value))
            .orderBy(sortSpecification, PLAYERS.DEBUTASOFFSET)
            .fetch()

        val players = mutableListOf<PlayerListDto>()

        records.forEach { record ->
            players.add(
                PlayerListDto(
                    id = record.get("Id", Int::class.java),
                    fullName = record.get("FullName", String::class.java),
                    teams = record.get("Teams", String::class.java),
                    debut = LocalDate.parse(record.get("Debut", String::class.java))
                        .format(formatFullMonths),
                    activeUntil = LocalDate.parse(record.get("ActiveUntil", String::class.java))
                        .format(formatFullMonths),
                )
            )
        }

        return Either.Right(players)
    }
}