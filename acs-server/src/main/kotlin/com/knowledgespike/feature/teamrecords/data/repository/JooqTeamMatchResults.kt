package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.MATCHSUBTYPE
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record14
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * An object containing methods to compute and manage cricket team career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into team performances.
 */
object JooqTeamMatchResults {

    /**
     * Creates a Common Table Expression (CTE) query for filtering match and team details
     * based on validated search parameters. It applies multiple conditions and joins
     * to return data matching the criteria specified in the search parameters.
     *
     * @param searchParameters The validated search parameters used to filter the query.
     *                          This includes criteria such as match type, date range,
     *                          team information, venue, and other relevant filters.
     * @return A jOOQ `SelectConditionStep` representing the CTE query with the applied filters.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
    ): SelectConditionStep<Record14<Any, Any, Int?, Int?, Int?, String?, Long?, String?, String?, String?, Any, Any, Int?, Int?>> {


        val searchCondition = buildSearchConditions(searchParameters)

        val (teamid, opponentsid) = if(searchParameters.isTeamBattingRecord) {
            Pair(EXTRAMATCHDETAILS.TEAMID, EXTRAMATCHDETAILS.OPPONENTSID)
        } else {
            Pair(EXTRAMATCHDETAILS.OPPONENTSID, EXTRAMATCHDETAILS.TEAMID)
        }

        val cte = select(
            field("team.name").`as`("team"),
            field("opponents.name").`as`("opponents"),
            MATCHES.ID.`as`("matchId"),
            MATCHES.VICTORYTYPE,
            MATCHES.HOWMUCH,
            MATCHES.MATCHDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.RESULTSTRING,
            GROUNDS.KNOWNAS,
            MATCHES.CAID,
            field("team.id").`as`("teamid"),
            field("opponents.id").`as`("opponentsid"),
            coalesce(MATCHES.WHOWONID, 0).`as`("WhoWonId"),
            coalesce(MATCHES.TOSSTEAMID, 0).`as`("TossTeamId"),
        ).from(MATCHES)
            .join(EXTRAMATCHDETAILS).on(
                EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID))
            .join(TEAMS.`as`("team")).on(field("team.id").eq(teamid)).and(MATCHES.ID.eq(
                EXTRAMATCHDETAILS.MATCHID))
            .join(TEAMS.`as`("opponents")).on(field("opponents.id").eq(opponentsid)).and(MATCHES.ID.eq(
                EXTRAMATCHDETAILS.MATCHID))
            .join(GROUNDS).on(GROUNDS.ID.eq(MATCHES.LOCATIONID))
            .where(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(MATCHES.ID.`in`(select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE).where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))))
            .and(searchCondition)
            .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))
            .and(MATCHES.HOWMUCH.ge(searchParameters.pagingParameters.limit))

        return cte
    }


}