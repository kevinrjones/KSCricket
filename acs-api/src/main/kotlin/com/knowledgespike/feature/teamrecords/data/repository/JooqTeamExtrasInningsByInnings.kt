package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record17
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*
import java.time.LocalDate

/**
 * An object containing methods to compute and manage cricket team career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into team performances.
 */
object JooqTeamExtrasInningsByInnings {

    /**
     * Creates a common table expression (CTE) that queries match and innings statistics
     * based on the provided search parameters. The query applies various filters such as
     * match type, team identifiers, venue, opponents, date or season, and other match-related
     * criteria. The results are aggregated and returned as a SQL query component.
     *
     * @param searchParameters A set of validated search parameters, including match type, team IDs,
     *                          venue, date range, season, and other match-specific details.
     * @return A configured SQL query component representing the CTE, which includes selected fields
     *         such as match details, team details, extras, and calculated statistics like percentages.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
    ): SelectConditionStep<Record17<Int?, Any, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, String?, String?, Any, LocalDate?, String?, Int?>> {


        val searchCondition = buildSearchConditions(searchParameters)

        val (teamid, opponentsid) = if (searchParameters.isTeamBattingRecord) {
            Pair(INNINGS.TEAMID, INNINGS.OPPONENTSID)
        } else {
            Pair(INNINGS.OPPONENTSID, INNINGS.TEAMID)
        }

        val cte = select(
            INNINGS.MATCHID,
            field("t.name").`as`("team"),
            INNINGS.EXTRAS,
            INNINGS.BYES,
            INNINGS.LEGBYES,
            INNINGS.WIDES,
            INNINGS.NOBALLS,
            INNINGS.PENALTY.`as`("penalties"),
            INNINGS.TOTAL,
            INNINGS.WICKETS,
            INNINGS.BALLSBOWLED,
            INNINGS.OVERS,
            GROUNDS.KNOWNAS,
            field("o.name").`as`("opponents"),
            MATCHES.MATCHSTARTDATE,
            MATCHES.CAID,
            trunc(INNINGS.EXTRAS.div(INNINGS.TOTAL).mul(100), 1).`as`("percentage")
        ).from(INNINGS)
            .join(MATCHES).on(MATCHES.ID.eq(INNINGS.MATCHID))
            .join(TEAMS.`as`("t")).on(teamid.eq(field("t.id", Int::class.java)))
            .join(TEAMS.`as`("o")).on(opponentsid.eq(field("o.id", Int::class.java)))
            .join(GROUNDS).on(MATCHES.LOCATIONID.eq(GROUNDS.ID))
            .join(EXTRAMATCHDETAILS).on(MATCHES.ID.eq(EXTRAMATCHDETAILS.MATCHID))
            .and(EXTRAMATCHDETAILS.TEAMID.eq(MATCHES.HOMETEAMID))
            .where(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(INNINGS.EXTRAS.ge(searchParameters.pagingParameters.limit))
            .and(searchCondition)
            .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))

        return cte
    }


}