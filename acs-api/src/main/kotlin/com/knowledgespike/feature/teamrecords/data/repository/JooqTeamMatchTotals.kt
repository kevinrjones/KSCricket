package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record16
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * An object containing methods to compute and manage cricket team career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into team performances.
 */
object JooqTeamMatchTotals {

    /**
     * Creates a Common Table Expression (CTE) to query and extract detailed match results based
     * on the provided validated search parameters. The CTE includes specified conditions such
     * as team information, opponents, match venue, date or season range, and other filters to
     * customize the query results.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing filters and
     *                          constraints to determine the match result data, including match type,
     *                          teams, venue, date range, and optional sorting preferences.
     * @return A SelectConditionStep with 16 fields representing the filtered match data,
     *         including team details, opponents, match date, results, and statistics such as
     *         total runs, wickets, and run rate per over (rpo).
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
    ): SelectConditionStep<Record16<Any, Any, Int?, Int?, String?, String?, Long?, String?, String?, String?, Int, Any, Any, Any, Any, Double>> {


        val searchCondition = buildSearchConditions(searchParameters)

        val (teamid, opponentsid) = if(searchParameters.isTeamBattingRecord) {
            Pair(field("i1.teamid"), field("i1.opponentsId"))
        } else {
            Pair(field("i1.opponentsId"), field("i1.teamid"))
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
            inline(0).`as`("Innings"),
            field("i1.TOTAL").add(coalesce(field("i2.total"), 0)).`as`("Runs"),
            field("i1.wickets").add(coalesce(field("i2.wickets"), 0)).`as`("Wickets"),
            coalesce(field("i1.ballsbowled"), 0).add(coalesce(field("i2.ballsbowled"), 0)).`as`("BallsBowled"),
            field("i1.BallsPerOver").`as`("BallsPerOver"),
            iif(
                coalesce(field("i1.ballsbowled"), 0).add(coalesce(field("i2.ballsbowled"), 0)).eq(0),
                0.0,
                coalesce(field("i1.total", Int::class.java), 0).add(coalesce(field("i2.total"), 0))
                    .cast(Double::class.java).div(
                        coalesce(field("i1.ballsbowled", Int::class.java), 0).add(coalesce(field("i2.ballsbowled"), 0))
                    ).mul(6)
            ).`as`("rpo")
        ).from(MATCHES)
            .join(INNINGS.`as`("i1")).on(field("i1.MATCHID").eq(MATCHES.ID))
            .and(field("i1.inningsNumber").eq(inline(1)))
            .leftJoin(INNINGS.`as`("i2"))
            .on(field("i2.matchid").eq(MATCHES.ID))
            .and(field("i2.teamid").eq(field("i1.teamid")))
            .and(field("i2.inningsNumber").eq(inline(2)))
            .join(TEAMS.`as`("team")).on(teamid.eq(field("team.id", Int::class.java)))
            .join(TEAMS.`as`("opponents")).on(opponentsid.eq(field("opponents.id", Int::class.java)))
            .join(EXTRAMATCHDETAILS).on(
                EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID)
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("team.id", Int::class.java)))
            )
            .where(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(searchCondition)
            .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))
            .and(field("i1.total").add(coalesce(field("i2.total"), 0)).ge(searchParameters.pagingParameters.limit))

        return cte
    }


}