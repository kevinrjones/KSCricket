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
import org.jooq.Record20
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL.*

/**
 * The `JooqFieldingIndividualSeason` object encapsulates functions to perform database operations
 * related to fielding statistics across individual seasons. This includes creating Common Table Expressions (CTEs)
 * and temporary tables to compute various fielding performance metrics, such as match and innings counts,
 * best performances, and summary aggregates.
 */
object JooqFieldingIndividualSeason {

    /**
     * Creates a Common Table Expression (CTE) to calculate the innings count for players in a match.
     * The method performs the following steps:
     * 1. Selects data from the specified fielding table where the innings number is 1.
     * 2. Drops the existing table for innings count if it exists.
     * 3. Creates a new temporary table with aggregated innings data grouped by player and series date.
     * 4. Ensures indexes are created for efficient querying on the temporary innings count table.
     *
     * @param context The DSLContext to interact with the database.
     * @param fieldingTableName The name of the table containing fielding data.
     * @param inningsCountTableName The name of the temporary table to hold the innings count data.
     * @param matchType The type of match, determining the filtering criteria for fetching data.
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
                field("playerId", Int::class.java),
                field("matches.SeriesDate", String::class.java),
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
                .groupBy(field("tid.playerId"), field("matches.SeriesDate"))
        ).execute()

        context.dropIndexIfExists("idx_tmp_innings_playerid").on(inningsCountTableName).execute()
        context.createIndexIfNotExists("idx_tmp_innings_playerid").on(inningsCountTableName, "PLAYERID").execute()

    }

    /**
     * Creates a temporary table to store the best fielding statistics for players
     * based on specific criteria such as dismissals, caught as fielder or keeper, and stumpings.
     *
     * @param context The DSLContext used for database interaction.
     * @param fieldingTableName The name of the source table containing fielding data.
     * @param bestFieldingTableName The name of the temporary table to be created and populated with best fielding data.
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
                field("SeriesDate", String::class.java),
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
                    field("SeriesDate", Int::class.java),
                    rowNumber().over().partitionBy(field("playerid"), field("SeriesDate"))
                        .orderBy(field("dismissals").desc(), field("caughtwk").desc()).`as`("rn")

                ).from(fieldingTableName)
            ).where(field("rn", Int::class.java).eq(inline(1)))
        ).execute()

        context.dropIndexIfExists("idx_tmp_fielding_playerid").on(bestFieldingTableName).execute()
        context.createIndexIfNotExists("idx_tmp_fielding_playerid").on(bestFieldingTableName, "PLAYERID").execute()

    }

    /**
     * Creates a Common Table Expression (CTE) query to generate detailed results for a set of players based
     * on the provided search parameters and temporary tables. This query retrieves performance statistics
     * such as matches played, innings, dismissals, catching performance, and best performances, grouped by
     * certain criteria like match type and series.
     *
     * @param searchParameters Represents the validated search constraints which may include match type, teams,
     *                         venue, date range, paging configuration, and filtering options.
     * @param tmpFieldingDetailsName The name of the temporary table containing fielding details for players.
     * @param tempTeamsName The name of the temporary table containing teams associated with players.
     * @param tempMatchCountsName The name of the temporary table containing match count statistics for players.
     * @param inningsCountCteName The name of the Common Table Expression holding innings counts.
     * @param bestDismissalsCteName The name of the Common Table Expression containing best dismissal statistics.
     * @return A `SelectJoinStep` data type representing the query configured with player statistics, filtered,
     *         and aggregated based on the given parameters.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpFieldingDetailsName: String,
        tempTeamsName: String,
        tempMatchCountsName: String,
        inningsCountCteName: String,
        bestDismissalsCteName: String,
    ): SelectJoinStep<Record20<Int?, String?, String?, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, String, String, Any>> {


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
                field("year")
            ).from(
                select(
                    field("${tempMatchCountsName}.count").`as`("matches"),
                    field("${inningsCountCteName}.innings").`as`("innings"),
                    field("${tmpFieldingDetailsName}.playerId"),
                    field("${tmpFieldingDetailsName}.SeriesDate"),
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
                    field("${tmpFieldingDetailsName}.SeriesDate", String::class.java).`as`("year"),
                ).from(tmpFieldingDetailsName)
                    .join(tempMatchCountsName).on(
                        field(
                            "${tmpFieldingDetailsName}.playerid",
                            Int::class.java
                        ).eq(field("${tempMatchCountsName}.playerid", Int::class.java))
                    )
                    .and(
                        field(
                            "${tempMatchCountsName}.SeriesDate",
                            String::class.java
                        ).eq(field("${tmpFieldingDetailsName}.SeriesDate", String::class.java))
                    )
                    .join(inningsCountCteName).on(
                        field(
                            "${tmpFieldingDetailsName}.playerid",
                            Int::class.java
                        ).eq(field("${inningsCountCteName}.playerid", Int::class.java))
                    )
                    .and(field("${inningsCountCteName}.SeriesDate").eq(field("${tmpFieldingDetailsName}.SeriesDate")))
                    .join(EXTRAMATCHDETAILS)
                    .on(EXTRAMATCHDETAILS.MATCHID.eq(field("${tmpFieldingDetailsName}.matchid", Int::class.java)))
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("${tmpFieldingDetailsName}.teamid", Int::class.java)))
                    .and(searchCondition)
                    .leftJoin(bestDismissalsCteName)
                    .on(field("${bestDismissalsCteName}.playerId").eq(field("${tmpFieldingDetailsName}.playerId")))
                    .and(field("${bestDismissalsCteName}.matchType").eq(field("${tmpFieldingDetailsName}.matchType")))
                    .and(field("${bestDismissalsCteName}.SeriesDate").eq(field("${tmpFieldingDetailsName}.SeriesDate")))
                    .groupBy(field("matchtype"), field("playerid"), field("${tmpFieldingDetailsName}.SeriesDate"))
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
                        field("${tempTeamsName}.SeriesDate", Int::class.java).eq(
                            field(
                                "innings.SeriesDate",
                                Int::class.java
                            )
                        )
                    )
                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(searchParameters.opponentsId.id))
                    .where(coalesce(field("dismissals").ge(searchParameters.pagingParameters.limit), inline(0)))
            )

        return cte
    }


}