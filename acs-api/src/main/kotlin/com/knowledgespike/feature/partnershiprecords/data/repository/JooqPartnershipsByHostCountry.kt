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
 * Object representing functionality to generate a Common Table Expression (CTE) and corresponding query
 * to retrieve partnership details for players by host country.
 */
object JooqPartnershipsByHostCountry {

    /**
     * Creates and returns a Common Table Expression (CTE) query for retrieving match results based on
     * specified search parameters. The resulting query is focused on generating statistical data for
     * player partnerships within matches.
     *
     * @param searchParameters Object containing validated search parameters such as match type, date range,
     *                         team, opponents, venue, ground, and other filtering conditions for retrieving
     *                         match results.
     * @return A query configuration object representing the constructed SQL statement with the defined
     *         conditions and aggregated calculations to retrieve results in a structured format.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters
    ): SelectConditionStep<Record15<Any, Any, String, String, String, String, String?, String, String, String, String, String, String, Serializable?, String?>> {

        val cteName = "cteResults"


        val searchCondition = buildSearchConditions(searchParameters)

        val completedInnings =
            field("${cteName}.innings", Int::class.java).minus(field("${cteName}.notouts", Int::class.java))

        val partitionBy = listOf(PARTNERSHIPS.PLAYERIDS, MATCHES.HOMECOUNTRYID)

        val cte =
            with(cteName).`as`(
                select(
                    PARTNERSHIPS.PLAYERIDS.`as`("playerids"),
                    PARTNERSHIPS.PLAYERNAMES.`as`("playernames"),
                    PARTNERSHIPS.TEAMID,
                    MATCHES.HOMECOUNTRYID,
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
                    .and(searchCondition))
                .select(
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
                    COUNTRIES.COUNTRYNAME.`as`("countryName"),
                ).from(cteName)
                .join(TEAMS).on(TEAMS.ID.eq(field("${cteName}.teamid", Int::class.java)))
                .leftJoin(PLAYERS.`as`("p1"))
                .on(field("p1.id").eq(substring(field("${cteName}.playerids", String::class.java), 1, 8)))
                .leftJoin(PLAYERS.`as`("p2"))
                .on(field("p2.id").eq(substring(field("${cteName}.playerids", String::class.java), 9, 8)))
                .join(COUNTRIES).on(COUNTRIES.ID.eq(field("${cteName}.HOMECOUNTRYID", Int::class.java)))
                .where(field("${cteName}.rn").eq(1))
                .and(field("${cteName}.runs").ge(searchParameters.pagingParameters.limit))

        return cte
    }


}