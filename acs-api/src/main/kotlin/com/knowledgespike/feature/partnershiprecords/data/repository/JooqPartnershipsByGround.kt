package com.knowledgespike.feature.partnershiprecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record15
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*
import java.io.Serializable

/**
 * Object responsible for generating complex SQL queries related to partnerships on specific grounds
 * using jOOQ, incorporating various filtering parameters.
 */
object JooqPartnershipsByGround {

    /**
     * Creates a common table expression (CTE) query that computes and filters statistical results
     * for cricket partnerships based on the given search parameters. The results are filtered using
     * conditions like match type, team, opponents, venue, date range, and other criteria.
     *
     * @param searchParameters The validated search parameters containing filtering criteria such as
     * match type, team IDs, date range, season, venue, and paging parameters.
     * @return A `SelectConditionStep` that executes a CTE query returning a record containing
     * partnerships, their associated players, teams, grounds, statistical aggregates (e.g.,
     * number of runs, hundreds, fifties, averages), and additional partnership details.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters
    ): SelectConditionStep<Record15<Any, Any, String, String, String, String, String?, String, String, String, String, String, String, Serializable?, String?>> {

        val cteName = "cteResults"


        val searchCondition = buildSearchConditions(searchParameters)

        val completedInnings =
            field("${cteName}.innings", Int::class.java).minus(field("${cteName}.notouts", Int::class.java))

        val partitionBy = listOf(PARTNERSHIPS.PLAYERIDS, MATCHES.LOCATIONID)

        val cte =
            with(cteName).`as`(
                select(
                    PARTNERSHIPS.PLAYERIDS.`as`("playerids"),
                    PARTNERSHIPS.PLAYERNAMES.`as`("playernames"),
                    PARTNERSHIPS.TEAMID,
                    MATCHES.LOCATIONID,
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
                field("p1.FullName", String::class.java).`as`("player1"),
                field("p2.FullName", String::class.java).`as`("player2"),
                field("p1.Id", String::class.java).`as`("player1Id"),
                field("p2.Id", String::class.java).`as`("player2Id"),
                TEAMS.NAME,
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
                GROUNDS.KNOWNAS.`as`("ground"),
            ).from(cteName)
                .join(TEAMS).on(TEAMS.ID.eq(field("${cteName}.teamid", Int::class.java)))
                .leftJoin(PLAYERS.`as`("p1"))
                .on(field("p1.id").eq(substring(field("${cteName}.playerids", String::class.java), 1, 8)))
                .leftJoin(PLAYERS.`as`("p2"))
                .on(field("p2.id").eq(substring(field("${cteName}.playerids", String::class.java), 9, 8)))
                .join(GROUNDS).on(GROUNDS.ID.eq(field("${cteName}.locationid", Int::class.java)))
                .where(field("${cteName}.rn").eq(1))
                .and(field("${cteName}.runs").ge(searchParameters.pagingParameters.limit))

        return cte
    }


}