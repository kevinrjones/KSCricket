package com.knowledgespike.feature.battingrecords.data.repository

import com.knowledgespike.db.tables.Battingdetails.Companion.BATTINGDETAILS
import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.PLAYERS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Record20
import org.jooq.Record22
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*
import java.time.LocalDate

/**
 * Provides utility functions for database operations related to cricket match and player records.
 *
 * This object encapsulates methods to create and manage temporary tables for statistical analysis
 * of cricket player performances, as well as constructing parameterized queries for fetching
 * filtered batting statistics based on given search parameters.
 */
object JooqBattingMatchRecords {

    /**
     * Creates a temporary table for batting details based on the given search parameters.
     *
     * This method generates a temporary table that consolidates batting performance data
     * such as scores, balls faced, boundaries hit, etc., for players across multiple
     * innings of a match. It filters and structures this data according to the specified
     * search parameters for further analysis or processing.
     *
     * @param context The DSL context used for executing SQL commands.
     * @param battingDetailsTempTableName The name of the temporary table to be created.
     * @param searchParameters Validated search parameters that define the filtering and paging criteria for batting details.
     */
    fun createTemporaryBattingDetailsTable(
        context: DSLContext,
        battingDetailsTempTableName: String,
        searchParameters: ValidatedSearchParameters,
    ) {

        val strikeRate = iif(
            coalesce(field("bd1.balls", Int::class.java), field("bd1.balls", Int::class.java)).eq(0),
            inline(0.0),
            coalesce(field("bd1.score", Int::class.java), field("bd2.score", Int::class.java)).cast(Double::class.java)
                .div(coalesce(field("bd1.balls", Int::class.java), field("bd1.balls", Int::class.java))).mul(100)
        )

        @Suppress("UNCHECKED_CAST")
        val limitClause = buildLimitClause(
            coalesce(field("bd1.score", Int::class.java), field("bd2.score")) as Field<Any>,
            searchParameters.pagingParameters.limit
        )

        context.dropTableIfExists(battingDetailsTempTableName).execute()
        context.createTemporaryTableIfNotExists(battingDetailsTempTableName).`as`(
            select(
                field("bd1.MatchId", Int::class.java),
                field("bd1.MatchType", String::class.java),
                field("bd1.TeamId", Int::class.java),
                field("bd1.OpponentsId", Int::class.java),
                field("bd1.PlayerId", Int::class.java),
                field("bd1.Score", Int::class.java).`as`("bat1"),
                field("bd1.NotOut", Int::class.java).`as`("notOut1"),
                field("bd2.Score", Int::class.java).`as`("bat2"),
                field("bd2.NotOut", Int::class.java).`as`("notOut2"),
                (coalesce(field("bd1.score", Int::class.java), 0)
                        + coalesce(field("bd2.score", Int::class.java), 0)).`as`("playerscore"),
                (coalesce(field("bd1.Balls", Int::class.java), 0)
                        + coalesce(field("bd2.Balls", Int::class.java), 0)).`as`("balls"),
                (coalesce(field("bd1.fours", Int::class.java), 0)
                        + coalesce(field("bd2.fours", Int::class.java), 0)).`as`("fours"),
                (coalesce(field("bd1.sixes", Int::class.java), 0)
                        + coalesce(field("bd2.sixes", Int::class.java), 0)).`as`("sixes"),
                (coalesce(field("bd1.minutes", Int::class.java), 0)
                        + coalesce(field("bd2.minutes", Int::class.java), 0)).`as`("minutes"),
                field("bd1.captain", Int::class.java),
                field("bd1.wicketkeeper", Int::class.java),
                strikeRate.`as`("sr")
            ).from(BATTINGDETAILS.`as`("bd1"))
                .leftOuterJoin(BATTINGDETAILS.`as`("bd2"))
                .on(field("bd1.matchId").eq(field("bd2.matchId")))
                .and(field("bd1.playerId").eq(field("bd2.playerId")))
                .and(field("bd1.matchType").eq(field("bd2.matchType")))
                .and(field("bd1.inningsNumber").ne(field("bd2.inningsNumber")))
                .where(field("bd1.matchType").eq(searchParameters.matchType.value))
                .and(field("bd1.inningsNumber").eq(1))
                .and(limitClause)
        ).execute()
    }

    /**
     * Creates a Common Table Expression (CTE) query to retrieve filtered batting statistics.
     * This method constructs a dynamic query based on the provided search parameters to fetch cricket-related data such as player performance,
     * match details, team and opponent information, and other related fields.
     *
     * @param searchParameters An instance of ValidatedBattingSearchParameters that defines the filtering criteria for the query,
     * including parameters like team ID, opponent ID, venue, ground, date range, and sorting preferences.
     * @param tmpBattingDetailsName The name of the temporary table or dataset containing batting details used as the base for the query construction.
     * @return A SelectConditionStep that represents the built query as a step in jOOQ's DSL, returning a Record20 containing fields such as player name,
     * team details, match performance metrics, and other relevant data.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpBattingDetailsName: String,
    ): SelectConditionStep<Record22<String?, String?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, String?, String?, String?, Int?, String?, LocalDate?, Long?, String?, Double?>?> {


        val searchCondition = buildSearchConditions(searchParameters)

        val cte =
            select(
                PLAYERS.FULLNAME,
                PLAYERS.SORTNAMEPART,
                PLAYERS.ID.`as`("playerid"),
                field("playerscore", Int::class.java).`as`("runs"),
                field("bat1", Int::class.java),
                field("notOut1", Int::class.java),
                field("bat2", Int::class.java),
                field("notOut2", Int::class.java),
                field("balls", Int::class.java),
                field("fours", Int::class.java),
                field("sixes", Int::class.java),
                field("minutes", Int::class.java),
                field("captain", Int::class.java),
                field("T.Name", String::class.java).`as`("teams"),
                field("O.name", String::class.java).`as`("opponents"),
                MATCHES.CAID.`as`("matchId"),
                MATCHES.LOCATIONID,
                MATCHES.MATCHDESIGNATOR,
                MATCHES.MATCHSTARTDATE,
                MATCHES.MATCHSTARTDATEASOFFSET,
                GROUNDS.KNOWNAS,
                field("sr", Double::class.java),
            ).from(tmpBattingDetailsName)
                .join(EXTRAMATCHDETAILS)
                .on(
                    EXTRAMATCHDETAILS.MATCHID.eq(
                        field(
                            "${tmpBattingDetailsName}.matchid",
                            Int::class.java
                        )
                    )
                )
                .and(
                    EXTRAMATCHDETAILS.TEAMID.eq(
                        field(
                            "${tmpBattingDetailsName}.teamid",
                            Int::class.java
                        )
                    )
                )
                .join(MATCHES).on(MATCHES.ID.eq(field("${tmpBattingDetailsName}.matchid", Int::class.java)))
                .and(searchCondition)
                .join(TEAMS.`as`("T")).on(field("T.id").eq(field("${tmpBattingDetailsName}.TeamId")))
                .join(TEAMS.`as`("O")).on(field("O.id").eq(field("${tmpBattingDetailsName}.OpponentsId")))
                .join(PLAYERS).on(PLAYERS.ID.eq(field("${tmpBattingDetailsName}.PlayerId", Int::class.java)))
                .join(GROUNDS).on(GROUNDS.ID.eq(field("locationId", Int::class.java)))
                .where(coalesce(field("playerscore").ge(searchParameters.pagingParameters.limit), inline(0)))


        return cte
    }

}