package com.knowledgespike.feature.fieldingrecords.data.repository

import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Players.Companion.PLAYERS
import com.knowledgespike.db.tables.Teams.Companion.TEAMS
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.MATCHES
import com.knowledgespike.db.tables.references.PLAYERSDATES
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.DSLContext
import org.jooq.Record19
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL.*

/**
 * Utility object containing functions for generating and managing database queries related to
 * individual fielding performance against opposition teams in the context of cricket analysis.
 */
object JooqFieldingIndividualOpposition {

    /**
     * Creates a Common Table Expression (CTE) to populate and manage an innings count table
     * based on match details, fielding, and player statistics.
     *
     * This function performs the following operations:
     * - Drops the existing innings count temporary table if it exists.
     * - Creates a new temporary table to store player innings counts against specific opponents.
     * - Populates the table by querying and combining information from fielding and match records.
     * - Adds an index on the `PLAYERID` column for the created temporary table for optimized querying.
     *
     * @param context The DSLContext used for constructing and executing database queries.
     * @param fieldingTableName The name of the table containing fielding data.
     * @param inningsCountTableName The name of the temporary table to store innings count data.
     * @param matchType The type of match being processed (e.g., Test, ODI, T20).
     */
    fun createInningsCountCte(
        context: DSLContext,
        fieldingTableName: String,
        inningsCountTableName: String,
        matchType: MatchType
    ) {

        val selInningsNumber = select(
            field("matchId"),
            field("teamId"),
            field("opponentsId"),
            field("playerId"),
        ).from(fieldingTableName)
            .where(field("inningsNumber", Int::class.java).eq(inline(1)))
            .asTable("tid")

        context.dropTableIfExists(inningsCountTableName).execute()
        context.createTemporaryTableIfNotExists(inningsCountTableName).`as`(
            select(
                field("playerId"),
                field("tid.OpponentsId"),
                count().`as`("innings"),
            ).from(INNINGS)
                .join(MATCHES).on(MATCHES.ID.eq(INNINGS.MATCHID))
                .and(MATCHES.MATCHTYPE.eq(matchType.value))
                .join(selInningsNumber)
                .on(
                    field("innings.matchId").eq(field("tid.matchId"))
                )
                .where(MATCHES.MATCHTYPE.eq(matchType.value))
                .and(field("innings.teamId").ne(field("tid.TeamId")))
                .and(field("innings.didBat").isTrue)
                .groupBy(field("tid.playerId"), field("tid.OpponentsId"))
        ).execute()

        context.dropIndexIfExists("idx_tmp_innings_playerid").on(inningsCountTableName).execute()
        context.createIndexIfNotExists("idx_tmp_innings_playerid").on(inningsCountTableName, "PLAYERID").execute()

    }

    /**
     * Creates a temporary table to store the best fielding statistics for players, categorized by opponent and match type.
     * Drops the table and its index if they already exist, then creates them with updated data.
     *
     * @param context The DSLContext to execute SQL queries.
     * @param fieldingTableName The name of the source table containing fielding statistics.
     * @param bestFieldingTableName The name of the temporary table to be created for storing best fielding statistics.
     */
    fun createTemporaryFieldingBest(
        context: DSLContext,
        fieldingTableName: String,
        bestFieldingTableName: String
    ) {
        context.dropTableIfExists(bestFieldingTableName).execute()
        context.createTemporaryTableIfNotExists(bestFieldingTableName).`as`(

            select(
                field("playerId", Int::class.java),
                field("matchType", String::class.java),
                field("OpponentsId", String::class.java),
                field("caughtf", Int::class.java).`as`("bestCaughtFielder"),
                field("caughtWk", Int::class.java).`as`("bestCaughtKeeper"),
                field("stumped", Int::class.java).`as`("bestStumpings"),
                field("dismissals", Int::class.java).`as`("bestDismissals"),
            ).from(
                select(
                    field("playerId", Int::class.java),
                    field("matchtype", Int::class.java),
                    field("caughtf", Int::class.java),
                    field("caughtwk", Int::class.java),
                    field("stumped", Int::class.java),
                    field("dismissals", Int::class.java),
                    field("OpponentsId", Int::class.java),
                    rowNumber().over().partitionBy(field("playerid"), field("OpponentsId"))
                        .orderBy(field("dismissals").desc(), field("caughtwk").desc()).`as`("rn")

                ).from(fieldingTableName)
            ).where(field("rn", Int::class.java).eq(inline(1)))
        ).execute()

        context.dropIndexIfExists("idx_tmp_fielding_playerid").on(bestFieldingTableName).execute()
        context.createIndexIfNotExists("idx_tmp_fielding_playerid").on(bestFieldingTableName, "PLAYERID").execute()

    }

    /**
     * Creates a Common Table Expression (CTE) for selecting results based on the provided search parameters and various temporary table names.
     *
     * This function constructs a query that aggregates data related to player performance,
     * including match details, innings information, dismissals, and best dismissals data.
     * The resulting CTE query is configured to apply filtering and grouping based on the input parameters,
     * returning data in a structured format that includes statistical results related to player or team performance.
     *
     * @param searchParameters The validated search criteria containing filters such as match type, venue, result, and paging parameters.
     * @param tmpFieldingDetailsName The name of the temporary table containing detailed fielding information.
     * @param tempTeamsName The name of the temporary table containing team statistics.
     * @param tempMatchCountsName The name of the temporary table with match count information.
     * @param inningsCountCteName The name of the Common Table Expression (CTE) that provides innings count information.
     * @param bestDismissalsCteName The name of the CTE that includes data about the best dismissals for players.
     * @return A step in the query building process, specifically a `SelectJoinStep` that allows additional clauses to be added
     *         before executing or fetching the results. This step includes seventeen columns with data like player ID, names,
     *         dismissal stats, team names, and opposing team names.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpFieldingDetailsName: String,
        tempTeamsName: String,
        tempMatchCountsName: String,
        inningsCountCteName: String,
        bestDismissalsCteName: String,
    ): SelectJoinStep<Record19<Int?, String?, String?, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, String, String>> {


        val searchCondition = buildSearchConditions(searchParameters)

        val cte =
            select(
                PLAYERS.ID.`as`("playerid"),
                PLAYERS.FULLNAME.`as`("name"),
                PLAYERS.SORTNAMEPART,
                year(PLAYERSDATES.DEBUT).`as`("debut"),
                year(PLAYERSDATES.ACTIVEUNTIL).`as`("end"),
                field("matches", Int::class.java),
                field("innings", Int::class.java),
                field("caughtFielder", Int::class.java),
                field("caughtKeeper", Int::class.java),
                field("caught", Int::class.java),
                field("wicketKeeperDismissals", Int::class.java),
                field("stumpings", Int::class.java),
                field("dismissals", Int::class.java),
                field("bestDismissals", Int::class.java),
                field("bestCaughtKeeper", Int::class.java),
                field("bestCaughtFielder", Int::class.java),
                field("bestStumpings", Int::class.java),
                coalesce(field("${tempTeamsName}.teams", String::class.java), inline("Unknown")).`as`("teams"),
                coalesce(field("O.name", String::class.java), inline("Unknown")).`as`("opponents"),
            ).from(
                select(
                    field("${tempMatchCountsName}.count").`as`("matches"),
                    field("${inningsCountCteName}.innings").`as`("innings"),
                    field("${tmpFieldingDetailsName}.playerId"),
                    field("${tmpFieldingDetailsName}.OpponentsId"),
                    field("${tmpFieldingDetailsName}.matchType"),
                    sum(field("caughtf", Int::class.java)).`as`("caughtFielder"),
                    sum(field("caughtwk", Int::class.java)).`as`("caughtKeeper"),
                    sum(field("stumped", Int::class.java)).`as`("stumpings"),
                    (sum(field("caughtwk", Int::class.java)))
                        .add(sum(field("caughtf", Int::class.java)))
                        .`as`("caught"),
                    (sum(field("caughtwk", Int::class.java)))
                        .add(sum(field("stumped", Int::class.java)))
                        .`as`("wicketKeeperDismissals"),
                    sum(field("caughtf", Int::class.java))
                        .add(sum(field("caughtwk", Int::class.java)))
                        .add(sum(field("stumped", Int::class.java)))
                        .`as`("dismissals"),
                    field("${bestDismissalsCteName}.bestDismissals").`as`("bestDismissals"),
                    field("${bestDismissalsCteName}.bestCaughtKeeper").`as`("bestCaughtKeeper"),
                    field("${bestDismissalsCteName}.bestCaughtFielder").`as`("bestCaughtFielder"),
                    field("${bestDismissalsCteName}.bestStumpings").`as`("bestStumpings"),
                ).from(tmpFieldingDetailsName)
                    .join(tempMatchCountsName).on(
                        field(
                            "${tmpFieldingDetailsName}.playerid",
                            Int::class.java
                        ).eq(field("${tempMatchCountsName}.playerid", Int::class.java))
                    )
                    .and(
                        field(
                            "${tempMatchCountsName}.OpponentsId",
                            Int::class.java
                        ).eq(field("${tmpFieldingDetailsName}.OpponentsId", Int::class.java))
                    )
                    .join(inningsCountCteName).on(
                        field(
                            "${tmpFieldingDetailsName}.playerid",
                            Int::class.java
                        ).eq(field("${inningsCountCteName}.playerid", Int::class.java))
                    )
                    .and(field("${inningsCountCteName}.OpponentsId").eq(field("${tmpFieldingDetailsName}.OpponentsId")))
                    .join(EXTRAMATCHDETAILS)
                    .on(EXTRAMATCHDETAILS.MATCHID.eq(field("${tmpFieldingDetailsName}.matchid", Int::class.java)))
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("${tmpFieldingDetailsName}.teamid", Int::class.java)))
                    .and(searchCondition)
                    .leftJoin(bestDismissalsCteName)
                    .on(field("${bestDismissalsCteName}.playerId").eq(field("${tmpFieldingDetailsName}.playerId")))
                    .and(field("${bestDismissalsCteName}.matchType").eq(field("${tmpFieldingDetailsName}.matchType")))
                    .and(field("${bestDismissalsCteName}.OpponentsId").eq(field("${tmpFieldingDetailsName}.OpponentsId")))
                    .groupBy(field("matchtype"), field("playerid"), field("${tmpFieldingDetailsName}.OpponentsId"))
                    .asTable("innings")
                    .join(PLAYERSDATES).on(PLAYERSDATES.PLAYERID.eq(field("innings.playerid", Int::class.java)))
                    .and(PLAYERSDATES.MATCHTYPE.eq(searchParameters.matchType.value))
                    .join(PLAYERS).on(PLAYERS.ID.eq(field("innings.playerid", Int::class.java)))
                    .leftJoin(tempTeamsName)
                    .on(
                        field("${tempTeamsName}.playerId", Int::class.java)
                            .eq(
                                field("innings.playerid", Int::class.java)
                            )
                    )
                    .and(field("${tempTeamsName}.matchType", String::class.java).eq(searchParameters.matchType.value))
                    .and(
                        field("${tempTeamsName}.OpponentsId", Int::class.java).eq(
                            field(
                                "innings.OpponentsId",
                                Int::class.java
                            )
                        )
                    )

                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(field("innings.OpponentsId")))
                    .where(coalesce(field("dismissals").ge(searchParameters.pagingParameters.limit), inline(0)))
            )

        return cte
    }


}