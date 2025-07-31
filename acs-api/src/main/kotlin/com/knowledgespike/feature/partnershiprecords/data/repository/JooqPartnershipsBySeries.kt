package com.knowledgespike.feature.partnershiprecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.MATCHSUBTYPE
import com.knowledgespike.db.tables.references.PARTNERSHIPS
import com.knowledgespike.db.tables.references.PLAYERS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record16
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*
import java.io.Serializable

/**
 * Object responsible for generating a Common Table Expression (CTE) query for
 * partnerships during cricket series based on various search parameters.
 * The results are filtered and aggregated according to the specified conditions.
 */
object JooqPartnershipsBySeries {

    /**
     * Creates a Common Table Expression (CTE) that aggregates and filters partnership data
     * based on various search parameters, such as match type, date range, team identifiers, and others.
     *
     * @param searchParameters An instance of `ValidatedSearchParameters` containing the filtering criteria
     *                         like match type, team IDs, date range, ground ID, venue, and result.
     * @return A `SelectConditionStep` object containing the CTE query result with 16 fields, including
     *         player details, statistics such as hundreds and fifties, and computed metrics like average runs.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters
    ): SelectConditionStep<Record16<Any, Any, Any, String, String, String, String, Any, Any, String, String, String, String, String, String, Serializable?>> {

        val cteName = "cteResults"


        val searchCondition = buildSearchConditions(searchParameters)

        val completedInnings =
            field("${cteName}.innings", Int::class.java).minus(field("${cteName}.notouts", Int::class.java))

        val partitionBy = listOf(PARTNERSHIPS.PLAYERIDS, MATCHES.SERIESNUMBER)

        val cte =
            with(cteName).`as`(
                select(
                    PARTNERSHIPS.PLAYERIDS.`as`("playerids"),
                    PARTNERSHIPS.PLAYERNAMES.`as`("playernames"),
                    PARTNERSHIPS.TEAMID,
                    PARTNERSHIPS.OPPONENTSID,
                    MATCHES.SERIESDATE.`as`("seriesDate"),
                    sum(PARTNERSHIPS.PARTNERSHIP).over().partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("runs"),
                    sum(PARTNERSHIPS.UNBROKEN).over().partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("notouts"),
                    sum(PARTNERSHIPS.HUNDRED).over().partitionBy(partitionBy)
                        .orderBy(partitionBy)
                        .`as`("hundreds"),
                    sum(PARTNERSHIPS.FIFTY).over().partitionBy(partitionBy)
                        .orderBy(partitionBy)
                        .`as`("fifties"),
                    count(PARTNERSHIPS.PARTNERSHIP).over().partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("innings"),
                    max(PARTNERSHIPS.SYNTHETICPARTNERSHIP).over()
                        .partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("highest"),
                    rowNumber().over().partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("rn"),
                )
                    .from(PARTNERSHIPS)
                    .join(MATCHES).on(PARTNERSHIPS.MATCHID.eq(MATCHES.ID))
                    .join(EXTRAMATCHDETAILS).on(PARTNERSHIPS.MATCHID.eq(EXTRAMATCHDETAILS.MATCHID))
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(PARTNERSHIPS.TEAMID))
                    .where(PARTNERSHIPS.MATCHTYPE.eq(searchParameters.matchType.value))
                    .and(PARTNERSHIPS.MULTIPLE.eq(0))
                    .and(
                        PARTNERSHIPS.MATCHID.`in`(
                            select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                                .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                        )
                    )
                    .and(searchCondition)
            ).select(
                field("${cteName}.playerids").`as`("playerids"),
                field("${cteName}.playernames").`as`("playernames"),
                field("${cteName}.seriesDate").`as`("seriesDate"),
                field("p1.FullName", String::class.java).`as`("player1"),
                field("p2.FullName", String::class.java).`as`("player2"),
                field("p1.Id", String::class.java).`as`("player1Id"),
                field("p2.Id", String::class.java).`as`("player2Id"),
                field("T.NAME").`as`("Name"),
                field("O.NAME").`as`("opponents"),
                field("${cteName}.hundreds", String::class.java).`as`("hundreds"),
                field("${cteName}.fifties", String::class.java).`as`("fifties"),
                field("${cteName}.highest", String::class.java).`as`("highest"),
                field("${cteName}.runs", String::class.java).`as`("runs"),
                field("${cteName}.notouts", String::class.java).`as`("notouts"),
                field("${cteName}.innings", String::class.java).`as`("innings"),
                iif(
                    completedInnings.eq(0), null as Double?,
                    trunc(field("${cteName}.runs", Int::class.java).div(completedInnings), 2)
                ).`as`("avg"),
            ).from(cteName)
                .join(TEAMS.`as`("T")).on(field("T.Id").eq(field("${cteName}.teamid", Int::class.java)))
                .join(TEAMS.`as`("O")).on(field("O.Id").eq(field("${cteName}.opponentsid", Int::class.java)))
                .leftJoin(PLAYERS.`as`("p1"))
                .on(field("p1.id").eq(substring(field("${cteName}.playerids", String::class.java), 1, 8)))
                .leftJoin(PLAYERS.`as`("p2"))
                .on(field("p2.id").eq(substring(field("${cteName}.playerids", String::class.java), 9, 8)))
                .where(field("${cteName}.rn").eq(1))
                .and(field("${cteName}.runs").ge(searchParameters.pagingParameters.limit))

        return cte
    }


}