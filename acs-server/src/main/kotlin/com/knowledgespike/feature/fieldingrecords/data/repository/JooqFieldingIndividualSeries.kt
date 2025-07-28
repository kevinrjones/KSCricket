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
 * JooqFieldingIndividualSeries is a utility object designed to provide structured SQL query
 * logic for fielding data analysis in cricket statistics. It facilitates the creation of
 * Common Table Expressions (CTEs) and temporary tables used for calculating and analyzing
 * individual player fielding statistics across cricket series.
 */
object JooqFieldingIndividualSeries {

    /**
     * Creates and initializes a temporary table to track innings counts for players in a specific match type,
     * based on the provided input fielding table and match configurations. The method executes a series of database
     * operations including creating a Common Table Expression (CTE), dropping and creating temporary tables,
     * and setting up indexes where necessary.
     *
     * @param context The DSLContext used for constructing and executing database queries.
     * @param fieldingTableName The name of the fielding data table to query.
     * @param inningsCountTableName The name of the temporary table to be created for storing innings count data.
     * @param matchType The match type for which the innings count data is to be generated.
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
                MATCHES.SERIESNUMBER,
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
                .groupBy(field("tid.playerId"), field("matches.SeriesNumber"))
        ).execute()

        context.dropIndexIfExists("idx_tmp_innings_playerid").on(inningsCountTableName).execute()
        context.createIndexIfNotExists("idx_tmp_innings_playerid").on(inningsCountTableName, "PLAYERID").execute()

    }

    /**
     * Creates a temporary table representing the best fielding statistics for players by utilizing data
     * from an existing fielding table. The method first drops the temporary table if it exists, then creates
     * a new temporary table with the best statistics for dismissals, caught as a fielder, caught as a keeper,
     * and stumpings. An index is also created on the "PLAYERID" column for improved query performance.
     *
     * @param context The DSLContext used for executing SQL statements.
     * @param fieldingTableName The name of the existing table containing fielding statistics.
     * @param bestFieldingTableName The name of the temporary table to be created for storing the best fielding statistics.
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
                field("SeriesNumber", String::class.java),
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
                    field("SeriesNumber", Int::class.java),
                    rowNumber().over().partitionBy(field("playerid"), field("SeriesNumber"))
                        .orderBy(field("dismissals").desc(), field("caughtwk").desc()).`as`("rn")

                ).from(fieldingTableName)
            ).where(field("rn", Int::class.java).eq(inline(1)))
        ).execute()

        context.dropIndexIfExists("idx_tmp_fielding_playerid").on(bestFieldingTableName).execute()
        context.createIndexIfNotExists("idx_tmp_fielding_playerid").on(bestFieldingTableName, "PLAYERID").execute()

    }

    /**
     * Creates a Common Table Expression (CTE) representing the results of a cricket fielding statistics query
     * based on the given validated search parameters and temporary table/CTE names provided.
     * This function sets up SQL query components necessary to calculate and group fielding statistics such as
     * dismissals, catches, stumpings, innings, and matches for players, linking with various related tables.
     *
     * @param searchParameters A validated object containing search parameters for filtering and sorting the query.
     * @param tmpFieldingDetailsName The name of the temporary table/CTE holding fielding details data.
     * @param tempTeamsName The name of the temporary table/CTE holding team information associated with players.
     * @param tempMatchCountsName The name of the temporary table/CTE containing match counts associated with players.
     * @param inningsCountCteName The name of the CTE holding inning counts for individual players.
     * @param bestDismissalsCteName The name of the CTE holding data for the best dismissals (e.g., highest catches or stumpings).
     * @return A `SelectJoinStep` representing the SQL query that fetches fielding statistics grouped and filtered
     *         by the provided search parameters and intermediate table/CTE references.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpFieldingDetailsName: String,
        tempTeamsName: String,
        tempMatchCountsName: String,
        inningsCountCteName: String,
        bestDismissalsCteName: String,
    ): SelectJoinStep<Record20<Int?, String?, Int, Int, String?, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, String, String, Any>> {


        val searchCondition = buildSearchConditions(searchParameters)


        val cte =
            select(
                PLAYERS.ID.`as`("playerid"),
                PLAYERS.FULLNAME.`as`("name"),
                year(PLAYERSDATES.DEBUT).`as`("debut"),
                year(PLAYERSDATES.ACTIVEUNTIL).`as`("end"),
                PLAYERS.SORTNAMEPART,
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
                    field("${tmpFieldingDetailsName}.opponentsId"),
                    field("${tmpFieldingDetailsName}.SeriesDate"),
                    field("${tmpFieldingDetailsName}.SeriesNumber"),
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
                            "${tempMatchCountsName}.SeriesNumber",
                            String::class.java
                        ).eq(field("${tmpFieldingDetailsName}.SeriesNumber", String::class.java))
                    )
                    .join(inningsCountCteName).on(
                        field(
                            "${tmpFieldingDetailsName}.playerid",
                            Int::class.java
                        ).eq(field("${inningsCountCteName}.playerid", Int::class.java))
                    )
                    .and(field("${inningsCountCteName}.SeriesNumber").eq(field("${tmpFieldingDetailsName}.SeriesNumber")))
                    .join(EXTRAMATCHDETAILS)
                    .on(EXTRAMATCHDETAILS.MATCHID.eq(field("${tmpFieldingDetailsName}.matchid", Int::class.java)))
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("${tmpFieldingDetailsName}.teamid", Int::class.java)))
                    .and(searchCondition)
                    .leftJoin(bestDismissalsCteName)
                    .on(field("${bestDismissalsCteName}.playerId").eq(field("${tmpFieldingDetailsName}.playerId")))
                    .and(field("${bestDismissalsCteName}.matchType").eq(field("${tmpFieldingDetailsName}.matchType")))
                    .and(field("${bestDismissalsCteName}.SeriesNumber").eq(field("${tmpFieldingDetailsName}.SeriesNumber")))
                    .groupBy(field("matchtype"), field("playerid"), field("${tmpFieldingDetailsName}.SeriesNumber"))
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
                        field("${tempTeamsName}.SeriesNumber", Int::class.java).eq(
                            field(
                                "innings.SeriesNumber",
                                Int::class.java
                            )
                        )
                    )
                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(field("innings.opponentsId")))
                    .where(coalesce(field("dismissals").ge(searchParameters.pagingParameters.limit), inline(0)))
            )

        return cte
    }


}