package com.knowledgespike.feature.battingrecords.data.repository

import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.PLAYERS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * Object responsible for creating a Common Table Expression (CTE) for querying detailed
 * batting information such as player performance, match-related statistics, and conditions
 * specified by search parameters.
 */
object JooqBattingInningsByInnings {

    /**
     * Creates a Common Table Expression (CTE) for fetching batting details based on the provided
     * search parameters and temporary table name. The resulting CTE encapsulates information
     * such as player details, match statistics, and related filtering conditions.
     *
     * @param searchParameters The validated parameters containing the criteria for fetching
     * batting statistics, including match type, venue, results, and paging configurations.
     * @param tmpTableName The name of the temporary table containing batting details.
     * @return A `SelectConditionStep<Record>` representing the constructed CTE query.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpTableName: String,
    ): SelectConditionStep<Record> {

        val strikeRate = iif(
            field("balls", Int::class.java).eq(0),
            inline(0.0),
            field("score", Int::class.java).cast(Double::class.java)
                .div(field("balls", Int::class.java)).mul(100)
        )

        val searchCondition = buildSearchConditions(searchParameters)
        val limitClause = buildLimitClause(field("score"), searchParameters.pagingParameters.limit)

        val cte =
            select(
                PLAYERS.ID.`as`("playerid"),
                PLAYERS.FULLNAME,
                PLAYERS.SORTNAMEPART,
                field("score", Int::class.java).`as`("runs"),
                field("notout", Int::class.java),
                field("notoutAdjustedScore", Int::class.java),
                field("position", Int::class.java),
                field("balls", Int::class.java),
                field("fours", Int::class.java),
                field("sixes", Int::class.java),
                field("minutes", Int::class.java),
                field("captain", Int::class.java),
                field("${tmpTableName}.wicketKeeper", Int::class.java).`as`("wicketKeeper"),
                field("inningsOrder", Int::class.java),
                field("dismissal", String::class.java),
                field("T.Name", String::class.java).`as`("teams"),
                field("O.name", String::class.java).`as`("opponents"),
                MATCHES.CAID.`as`("matchId"),
                MATCHES.LOCATIONID,
                MATCHES.MATCHDESIGNATOR,
                MATCHES.MATCHSTARTDATE,
                MATCHES.MATCHSTARTDATEASOFFSET,
                GROUNDS.KNOWNAS,
                strikeRate.`as`("sr"),
            ).from(tmpTableName)
                .join(MATCHES).on(MATCHES.ID.eq(field(  "${tmpTableName}.matchid", Int::class.java)))
                .join(EXTRAMATCHDETAILS)
                .on(
                    EXTRAMATCHDETAILS.MATCHID.eq(
                        field(
                            "${tmpTableName}.matchid",
                            Int::class.java
                        )
                    )
                )
                .and(
                    EXTRAMATCHDETAILS.TEAMID.eq(
                        field(
                            "${tmpTableName}.teamid",
                            Int::class.java
                        )
                    )
                )
                .and(searchCondition)
                .join(TEAMS.`as`("T")).on(field("T.id").eq(field("${tmpTableName}.TeamId")))
                .join(TEAMS.`as`("O")).on(field("O.id").eq(field("${tmpTableName}.OpponentsId")))
                .join(PLAYERS).on(PLAYERS.ID.eq(field("${tmpTableName}.PlayerId", Int::class.java)))
                .join(GROUNDS).on(GROUNDS.ID.eq(field("${tmpTableName}.locationId", Int::class.java)))
                .where(limitClause)


        return cte
    }
}