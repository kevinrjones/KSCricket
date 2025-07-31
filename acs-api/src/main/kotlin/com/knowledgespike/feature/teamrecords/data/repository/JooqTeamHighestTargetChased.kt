package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.MATCHSUBTYPE
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record10
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * An object containing methods to compute and manage cricket team career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into team performances.
 */
object JooqTeamHighestTargetChased {

    /**
     * Generates a Common Table Expression (CTE) query for retrieving match results based on the
     * given search parameters. The CTE includes fields for the winning team, losing team, match
     * title, match date, series date, match start date, location, result string, match ID, and target runs.
     *
     * This method performs various conditional checks based on the search parameters to filter
     * the match data, including team ID, opponent ID, venue, season, date range, ground, match type, results, and more.
     *
     * @param searchParameters Represents the validated search parameters input by the user. These parameters
     * are used to filter the match results based on various criteria such as teams, venue, dates, match type, and more.
     * @return A `SelectConditionStep` instance that represents the query to fetch filtered match results from the database.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
    ): SelectConditionStep<Record10<Any, Any, Any, Any, Any, Any, Any, Any, Any, Any>> {


        val searchCondition = buildSearchConditions(searchParameters)

        val cte = select(
            field("T.name").`as`("WinningTeam"),
            field("O.Name").`as`("LosingTeam"),
            field("M.MatchTitle").`as`("MatchTitle"),
            field("M.MatchDate").`as`("MatchDate"),
            field("M.SeriesDate").`as`("SeriesDate"),
            field("M.MatchStartDateAsOffset").`as`("MatchStartDateAsOffset"),
            field("M.Location").`as`("ground"),
            field("M.ResultString").`as`("ResultString"),
            field("M.CaId").`as`("matchid"),
            field("I.Total").add(inline(1)).`as`("target"),
        ).from(
            EXTRAMATCHDETAILS.join(MATCHES).on(EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID))
                .join(INNINGS.`as`("I")).on(field("I.MATCHID").eq(EXTRAMATCHDETAILS.MATCHID))
                .and(EXTRAMATCHDETAILS.TEAMID.eq(field("I.TEAMID", Int::class.java)))
                .join(MATCHES.`as`("M")).on(EXTRAMATCHDETAILS.MATCHID.eq(field("M.Id", Int::class.java)))
                .join(TEAMS.`as`("T")).on(MATCHES.WHOWONID.eq(field("T.ID", Int::class.java)))
                .join(TEAMS.`as`("O")).on(MATCHES.WHOLOSTID.eq(field("O.ID", Int::class.java)))
        )
            /*
                where emd.MatchType = @match_type
      and emd.Result = 2
      and m.VictoryType = 3
      and I.InningsOrder = 1

             */
            .where(EXTRAMATCHDETAILS.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(
                EXTRAMATCHDETAILS.RESULT.eq(inline(2))
                    .and(MATCHES.VICTORYTYPE.eq(3))
                    .and(field("I.INNINGSORDER").eq(1))
                    .and(searchCondition)
                    .and(field("total").ge(searchParameters.pagingParameters.limit))
                    .and(
                        field("m.id").`in`(
                            select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                                .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                        )
                    )
            )

        return cte
    }


}