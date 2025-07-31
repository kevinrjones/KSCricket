package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.MATCHSUBTYPE
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record16
import org.jooq.Record18
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * An object containing methods to compute and manage cricket team career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into team performances.
 */
object JooqTeamInningsByInnings {

    /**
     * Creates a Common Table Expression (CTE) for querying cricket match results
     * based on the provided search parameters. The method constructs the necessary
     * filtering and joining conditions to retrieve relevant data related to match results,
     * innings details, and team information.
     *
     * @param searchParameters An instance of `ValidatedSearchParameters` that contains validated
     * search criteria, such as match type, teams involved, venue, timeframe, and other filtering conditions.
     * @return A `SelectConditionStep` query object that represents the constructed CTE for retrieving match results.
     * The query includes columns for team names, opposing team names, match details, innings statistics,
     * and calculated metrics like runs per over (RPO).
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
    ): SelectConditionStep<Record18<Any, Any, Int?, Int?, String?, String?, Long?, String?, String?, String?, Int?, Int?, Int?, Byte?, Byte?, Int?, Int?, Double>> {


        val searchCondition = buildSearchConditions(searchParameters)

        val (teamid, opponentsid) = if(searchParameters.isTeamBattingRecord) {
            Pair(INNINGS.TEAMID, INNINGS.OPPONENTSID)
        } else {
            Pair(INNINGS.OPPONENTSID, INNINGS.TEAMID)
        }

        val cte = select(
            field("team.name").`as`("team"),
            field("opponents.name").`as`("opponents"),
            EXTRAMATCHDETAILS.RESULT,
            MATCHES.ID.`as`("matchId"),
            MATCHES.MATCHTITLE,
            MATCHES.MATCHDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.RESULTSTRING,
            MATCHES.LOCATION.`as`("ground"),
            MATCHES.CAID,
            INNINGS.INNINGSORDER.`as`("Innings"),
            INNINGS.TOTAL.`as`("Runs"),
            INNINGS.WICKETS,
            INNINGS.ALLOUT,
            INNINGS.DECLARED,
            INNINGS.BALLSBOWLED,
            INNINGS.BALLSPEROVER,
            iif(
                INNINGS.BALLSBOWLED.eq(0),
                0.0,
                INNINGS.TOTAL.cast(Double::class.java).div(INNINGS.BALLSBOWLED).mul(6)
            ).`as`("rpo")
        ).from(MATCHES)
            .join(INNINGS).on(INNINGS.MATCHID.eq(MATCHES.ID))
            .join(TEAMS.`as`("team")).on(teamid.eq(field("team.id", Int::class.java)))
            .join(TEAMS.`as`("opponents")).on(opponentsid.eq(field("opponents.id", Int::class.java)))
            .join(EXTRAMATCHDETAILS).on(
                EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID)
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("team.id", Int::class.java)))
            )
            .where(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(MATCHES.ID.`in`(select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE).where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))))
            .and(INNINGS.TOTAL.ge(searchParameters.pagingParameters.limit))
            .and(searchCondition)
            .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))

        return cte
    }


}