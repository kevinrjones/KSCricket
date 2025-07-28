package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.MATCHSUBTYPE
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record11
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * An object containing methods to compute and manage cricket team career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into team performances.
 */
object JooqTeamLowestTargetSuccessfullyDefended {

    /**
     * Creates a Common Table Expression (CTE) query that retrieves match results based on the given validated
     * search parameters. The CTE includes filtering by match type, team identifiers, venue, date or season,
     * and other criteria provided in the search parameters. The query is designed to retrieve complex
     * aggregated data about matches with detailed conditions.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing filters such as match type,
     *                          team IDs, ground ID, venue, date range, season, and additional match-specific details
     *                          to constrain the results.
     * @return A SelectConditionStep that represents the constructed query, which retrieves fields such as
     *         winning team name, losing team name, match details, location, and calculated target scores
     *         from relevant tables and joins.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
    ): SelectConditionStep<Record11<Any, Any, Int?, String?, String?, String?, Long?, String?, String?, String?, Any>> {


        val searchCondition = buildSearchConditions(searchParameters)

        val (teamid, opponentsid) = if (searchParameters.isTeamBattingRecord) {
            Pair(MATCHES.WHOWONID, MATCHES.WHOLOSTID)
        } else {
            Pair(MATCHES.WHOLOSTID, MATCHES.WHOWONID)
        }

        val cte = select(
            field("T.name").`as`("WinningTeam"),
            field("O.Name").`as`("LosingTeam"),
            MATCHES.ID.`as`("matchid"),
            MATCHES.MATCHTITLE,
            MATCHES.MATCHDATE,
            MATCHES.SERIESDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.LOCATION.`as`("ground"),
            MATCHES.RESULTSTRING,
            MATCHES.CAID,
            field("I.Total").add(MATCHES.HOWMUCH).`as`("target"),
        ).from(
            EXTRAMATCHDETAILS.join(MATCHES).on(EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID))
                .join(INNINGS.`as`("I")).on(field("I.MATCHID").eq(EXTRAMATCHDETAILS.MATCHID))
                .and(EXTRAMATCHDETAILS.TEAMID.eq(field("I.TEAMID", Int::class.java)))
                .join(TEAMS.`as`("T")).on(teamid.eq(field("T.ID", Int::class.java)))
                .join(TEAMS.`as`("O")).on(opponentsid.eq(field("O.ID", Int::class.java)))
        )
            .where(EXTRAMATCHDETAILS.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(
                EXTRAMATCHDETAILS.RESULT.eq(inline(2))
                    .and(MATCHES.VICTORYTYPE.eq(2))
                    .and(searchCondition)
                    .and(field("total").add(MATCHES.HOWMUCH).le(searchParameters.pagingParameters.limit))
                    .and(
                        MATCHES.ID.`in`(
                            select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                                .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                        )
                    )
            )
        return cte
    }
}