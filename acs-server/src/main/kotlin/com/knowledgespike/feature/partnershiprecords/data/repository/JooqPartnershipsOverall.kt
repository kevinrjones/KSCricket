package com.knowledgespike.feature.partnershiprecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.MATCHSUBTYPE
import com.knowledgespike.db.tables.references.PARTNERSHIPS
import com.knowledgespike.db.tables.references.PLAYERS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record14
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*
import java.io.Serializable

/**
 * Object responsible for generating a Common Table Expression (CTE) related to partnerships data
 * with the application of various filtering conditions.
 */
object JooqPartnershipsOverall {

    /**
     * Creates a Common Table Expression (CTE) query to retrieve partnership results based on the specified search parameters.
     *
     * @param searchParameters The validated parameters containing user-specified search filters such as match type, venue,
     * team, opponent, date range, season, and others to shape the query results.
     * @return A query of type `SelectConditionStep<Record14>` which retrieves the filtered data for partnerships,
     * including player details, team information, aggregate stats (like runs, hundreds, fifties), and calculated averages.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters
    ): SelectConditionStep<Record14<Any, Any, String, String, String, String, String?, String, String, String, String, String, String, Serializable?>> {

        val cteName = "cteResults"
        
        val searchCondition = buildSearchConditions(searchParameters)

        val partitionBy = listOf(PARTNERSHIPS.PLAYERIDS)

        val completedInnings =
            field("${cteName}.innings", Int::class.java).minus(field("${cteName}.notouts", Int::class.java))

        val cte =
            with(cteName).`as`(
                select(
                    PARTNERSHIPS.PLAYERIDS.`as`("playerids"),
                    PARTNERSHIPS.PLAYERNAMES.`as`("playernames"),
                    PARTNERSHIPS.TEAMID,
                    sum(PARTNERSHIPS.PARTNERSHIP).over().partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("runs"),
                    sum(PARTNERSHIPS.UNBROKEN).over().partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("notouts"),
                    sum(PARTNERSHIPS.HUNDRED).over().partitionBy(partitionBy).orderBy(partitionBy)
                        .`as`("hundreds"),
                    sum(PARTNERSHIPS.FIFTY).over().partitionBy(partitionBy).orderBy(partitionBy)
                        .`as`("fifties"),
                    count(PARTNERSHIPS.PARTNERSHIP).over().partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("innings"),
                    max(PARTNERSHIPS.SYNTHETICPARTNERSHIP).over().partitionBy(partitionBy)
                        .orderBy(partitionBy).`as`("highest"),
                    rowNumber().over().partitionBy(partitionBy).orderBy(partitionBy).`as`("rn"),
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
            ).from(cteName)
                .join(TEAMS).on(TEAMS.ID.eq(field("${cteName}.teamid", Int::class.java)))
                .leftJoin(PLAYERS.`as`("p1"))
                .on(field("p1.id").eq(substring(field("${cteName}.playerids", String::class.java), 1, 8)))
                .leftJoin(PLAYERS.`as`("p2"))
                .on(field("p2.id").eq(substring(field("${cteName}.playerids", String::class.java), 9, 8)))
                .where(field("${cteName}.rn").eq(1))
                .and(field("${cteName}.runs").ge(searchParameters.pagingParameters.limit))

        return cte
    }


}