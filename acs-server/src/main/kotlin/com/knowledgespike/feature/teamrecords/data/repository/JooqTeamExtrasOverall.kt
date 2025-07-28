package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record12
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * An object containing methods to compute and manage cricket team career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into team performances.
 */
object JooqTeamExtrasOverall {

    /**
     * Constructs and returns a common table expression (CTE) representing filtered and aggregated match statistics
     * based on the provided search parameters. The resulting CTE contains data such as team statistics, match results,
     * and aggregated metrics like runs, extras, and wickets, depending on the criteria specified.
     *
     * The method applies various filters like match type, date range, venue, teams, and other contextual data
     * to dynamically generate the required SQL query and returns it as a structured query object.
     *
     * @param searchParameters The validated parameters specifying filters and options for match statistics queries,
     *                         such as match type, date range, team identifiers, venue, and sorting preferences.
     * @return A structured SQL query step that represents the filtered and aggregated match statistics, ready to
     *         be executed or further processed.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
    ): SelectConditionStep<Record12<Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Int>> {


        val searchCondition = buildSearchConditions(searchParameters)

        val teamid = if (searchParameters.isTeamBattingRecord) {
            field("i.TEAMID")
        } else {
            field("i.OPPONENTSID")
        }

        val cte = select(
            field("extras.name").`as`("team"),
            field("games.played").`as`("played"),
            field("extras.runs").`as`("runs"),
            field("extras.extras").`as`("extras"),
            field("extras.byes").`as`("byes"),
            field("extras.legbyes").`as`("legByes"),
            field("extras.wides").`as`("wides"),
            field("extras.noballs").`as`("noBalls"),
            field("extras.penalties").`as`("penalties"),
            field("extras.wickets").`as`("wickets"),
            field("extras.balls").`as`("balls"),
            trunc((field("extras.extras", Int::class.java).div(field("extras.runs", Int::class.java))).mul(100), 1).`as`("percentage")
        ).from(
            select(field("teamid"), count().`as`("played"))
                .from(MATCHES).join(EXTRAMATCHDETAILS).on(MATCHES.ID.eq(EXTRAMATCHDETAILS.MATCHID))
                .where(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
                .and(searchCondition)
                .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))
                .groupBy(field("teamid")).asTable("games")
        )
            .leftJoin(
                select(
                    field("team.id"),
                    field("team.name"),
                    sum(field("i.total", Int::class.java)).`as`("runs"),
                    sum(field("i.extras", Int::class.java)).`as`("extras"),
                    sum(field("i.byes", Int::class.java)).`as`("byes"),
                    sum(field("i.legbyes", Int::class.java)).`as`("legByes"),
                    sum(field("i.wides", Int::class.java)).`as`("wides"),
                    sum(field("i.noballs", Int::class.java)).`as`("noBalls"),
                    sum(field("i.penalty", Int::class.java)).`as`("penalties"),
                    sum(field("i.wickets", Int::class.java)).`as`("wickets"),
                    sum(field("i.ballsbowled", Int::class.java)).`as`("balls"),
                ).from(MATCHES)

                    .join(INNINGS.`as`("i")).on(field("i.MATCHID").eq(MATCHES.ID))
                    .join(TEAMS.`as`("team")).on(teamid.eq(field("team.id", Int::class.java)))
                    .join(EXTRAMATCHDETAILS).on(
                        EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID)
                            .and(EXTRAMATCHDETAILS.TEAMID.eq(field("team.id", Int::class.java)))
                            .and(searchCondition)
                            .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))
                    )
                    .where(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
                    .groupBy(teamid).asTable("extras")
            ).on(field("games.teamid", Int::class.java).eq(field("extras.id", Int::class.java)))
            .where(field("extras").ge(searchParameters.pagingParameters.limit))

        return cte
    }


}