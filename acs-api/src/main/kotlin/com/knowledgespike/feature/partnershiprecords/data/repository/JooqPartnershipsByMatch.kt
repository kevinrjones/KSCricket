package com.knowledgespike.feature.partnershiprecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * Provides functionality to query partnerships by match using JOOQ for cricket statistics.
 * This object is designed to create complex Common Table Expressions (CTEs) tailored
 * to the specified search parameters, enabling detailed statistical analysis of partnerships.
 */
object JooqPartnershipsByMatch {

    /**
     * Creates a Common Table Expression (CTE) that generates a query for filtering and retrieving
     * cricket match records based on the provided search parameters.
     *
     * The method builds a query using various conditions, joins, and aggregations derived
     * from the validated search parameters. The resulting query allows extraction of match
     * details, partnerships, batting performance, and other related statistics. It further
     * filters the results using conditions such as team IDs, opponents IDs, venue, date range,
     * match results, and more. The final CTE is used to return a result set with applied conditions
     * and paging logic.
     *
     * @param searchParameters A data class containing validated search criteria, including
     * match type, teams involved, venue, date/season range, and filter conditions for the matches.
     * The parameters filter match records and support advanced querying based on multiple dimensions.
     * @return A jOOQ SelectConditionStep containing the query logic for retrieving filtered match records
     * and related statistics with the specified search parameters applied.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters
    ): SelectConditionStep<Record> {

        val cteName = "cteResults"


        val searchCondition = buildSearchConditions(searchParameters)

        val partitionBy = listOf(PARTNERSHIPS.MATCHID, PARTNERSHIPS.TEAMID, PARTNERSHIPS.PLAYERIDS)

        val cte =
            with(cteName).`as`(
                select(
                    MATCHES.CAID,
                    sum(PARTNERSHIPS.PARTNERSHIP).over().partitionBy(partitionBy).div(2).`as`("runs"),
                    sum(PARTNERSHIPS.SYNTHETICPARTNERSHIP).over().partitionBy(partitionBy).div(2).`as`("SyntheticPartnership"),
                    PARTNERSHIPS.WICKET,
                    PARTNERSHIPS.PREVIOUSWICKET,
                    PARTNERSHIPS.PREVIOUSSCORE,
                    PARTNERSHIPS.PLAYERIDS,
                    PARTNERSHIPS.PLAYERNAMES,
                    PARTNERSHIPS.CURRENTSCORE,
                    PARTNERSHIPS.FIFTY,
                    PARTNERSHIPS.HUNDRED,
                    sum(PARTNERSHIPS.UNBROKEN).over().partitionBy(partitionBy).div(2).`as`("Unbroken1"),
                    sum(inline(1)).over().partitionBy(partitionBy).div(2).`as`("innings"),
                    PARTNERSHIPS.MULTIPLE,
                    PARTNERSHIPS.PARTIAL,
                    field("T.Name").`as`("teams"),
                    field("O.Name").`as`("opponents"),
                    GROUNDS.KNOWNAS,
                    PLAYERS.FULLNAME.`as`("player1"),
                    PLAYERS.ID.`as`("player1Id"),
                    lead(PLAYERS.FULLNAME).over()
                        .partitionBy(partitionBy)
                        .orderBy(partitionBy)
                        .`as`("player2"),
                    lead(PLAYERS.ID).over()
                        .partitionBy(partitionBy)
                        .orderBy(partitionBy)
                        .`as`("player2Id"),
                    MATCHES.MATCHSTARTDATEASOFFSET,
                    MATCHES.MATCHSTARTDATE,
                    MATCHES.MATCHTITLE,
                    MATCHES.RESULTSTRING,
                    rowNumber().over()
                        .partitionBy(partitionBy)
                        .`as`("rn"),
                )
                    .from(PARTNERSHIPS)
                    .join(MATCHES).on(PARTNERSHIPS.MATCHID.eq(MATCHES.ID))
                    .join(EXTRAMATCHDETAILS).on(PARTNERSHIPS.MATCHID.eq(EXTRAMATCHDETAILS.MATCHID))
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(PARTNERSHIPS.TEAMID))
                    .leftJoin(PARTNERSHIPSPLAYERS).on(PARTNERSHIPS.ID.eq(PARTNERSHIPSPLAYERS.PARTNERSHIPID))
                    .leftJoin(PLAYERS).on(PARTNERSHIPSPLAYERS.PLAYERID.eq(PLAYERS.ID))
                    .join(TEAMS.`as`("T")).on(PARTNERSHIPS.TEAMID.eq(field("T.ID", Int::class.java)))
                    .join(TEAMS.`as`("O")).on(PARTNERSHIPS.OPPONENTSID.eq(field("O.ID", Int::class.java)))
                    .join(GROUNDS).on(GROUNDS.ID.eq(MATCHES.LOCATIONID))
                    .where(PARTNERSHIPS.MATCHTYPE.eq(searchParameters.matchType.value))
                    .and(PARTNERSHIPS.MULTIPLE.eq(0))
                    .and(
                        PARTNERSHIPS.MATCHID.`in`(
                            select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                                .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                        )
                    )
                    .and(searchCondition))
                .select().from(cteName)
                .where(field("${cteName}.rn").eq(1))
                .and(field("${cteName}.runs").ge(searchParameters.pagingParameters.limit))

        return cte
    }


}