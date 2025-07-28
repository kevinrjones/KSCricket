package com.knowledgespike.feature.bowlingrecords.data.repository


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
 * Object `JooqBowlingInningsByInnings` is responsible for constructing a common table expression (CTE)
 * for detailed bowling statistics retrieval. It integrates multiple filters and joins to fetch and filter
 * specific bowling data based on given parameters.
 */
object JooqBowlingInningsByInnings {

    /**
     * Constructs a common table expression (CTE) query to retrieve detailed bowling statistics based on the given search parameters.
     * The query applies various filters such as match result, venue, team, opponents, ground, and date range conditions.
     * It also joins multiple tables like matches, extra match details, teams, players, and grounds to collect the required data.
     *
     * @param searchParameters The validated set of search parameters that specify the filtering criteria for the query.
     * @param tmpTableName The temporary table name containing bowling details used within the CTE.
     * @return A `SelectConditionStep<Record>` query object representing the constructed CTE with all applied filters and joins.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpTableName: String,
    ): SelectConditionStep<Record> {

        val searchCondition = buildSearchConditions(searchParameters)

        val cte =
            select(
                PLAYERS.FULLNAME,
                PLAYERS.SORTNAMEPART,
                PLAYERS.ID.`as`("playerid"),
                coalesce(field("Balls", Int::class.java), 0).`as`("Balls"),
                coalesce(field("Maidens", Int::class.java), 0).`as`("Maidens"),
                coalesce(field("Dots", Int::class.java), 0).`as`("Dots"),
                coalesce(field("Runs", Int::class.java), 0).`as`("Runs"),
                coalesce(field("Wickets", Int::class.java), 0).`as`("Wickets"),
                field("position", Int::class.java),
                field("fours", Int::class.java),
                field("sixes", Int::class.java),
                field("captain", Int::class.java),
                field("inningsOrder", Int::class.java),
                field("inningsNumber", String::class.java),
                field("T.Name", String::class.java).`as`("teams"),
                field("O.name", String::class.java).`as`("opponents"),
                MATCHES.CAID.`as`("matchId"),
                MATCHES.LOCATIONID,
                MATCHES.MATCHDESIGNATOR,
                MATCHES.MATCHSTARTDATE,
                MATCHES.MATCHSTARTDATEASOFFSET,
                MATCHES.BALLSPEROVER,
                GROUNDS.KNOWNAS,
                iif(
                    field("balls", Int::class.java).eq(0),
                    0,
                    trunc(
                        field("runs", Int::class.java).div(cast(field("balls", Int::class.java), Double::class.java))
                            .mul(6), 2
                    ),
                ).`as`("rpo")
            ).from(tmpTableName)
                .join(MATCHES).on(MATCHES.ID.eq(field("${tmpTableName}.matchid", Int::class.java)))
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
                .where(coalesce(field("wickets").ge(searchParameters.pagingParameters.limit), inline(0)))


        return cte
    }
}