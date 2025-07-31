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
object JooqTeamLowestTargetSuccessfullyDefendedInUnreducedMatch {

    /**
     * Creates a Common Table Expression (CTE) query for retrieving match results based on validated search parameters.
     * The query incorporates various conditions derived from the search parameters, such as team IDs, match venues,
     * match outcomes, dates, seasons, and other criteria. The results are filtered using these conditions and
     * comprise details about the match, including winning and losing teams, match location, result strings, and targets.
     *
     * @param searchParameters The validated search parameters containing the filters and criteria for the match results query.
     *                         Includes fields such as match type, team IDs, venue details, date ranges, and more.
     * @return A JOOQ SelectConditionStep instance that represents a query returning match results with fields such as
     *         winning team name, losing team name, match ID, match title, match date, series date, location, result string,
     *         and target scores.
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
                    .and(not(MATCHES.OVERSREDUCED.eq(1)))
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