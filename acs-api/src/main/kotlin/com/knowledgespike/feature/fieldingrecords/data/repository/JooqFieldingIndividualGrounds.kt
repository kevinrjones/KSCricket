package com.knowledgespike.feature.fieldingrecords.data.repository

import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Players.Companion.PLAYERS
import com.knowledgespike.db.tables.Teams.Companion.TEAMS
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.MATCHES
import com.knowledgespike.db.tables.references.PLAYERSDATES
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.DSLContext
import org.jooq.Record21
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL.*

/**
 * The `JooqFieldingIndividualGrounds` object contains a set of utility functions used for creating and managing
 * database Common Table Expressions (CTEs) or temporary tables related to individual's fielding statistics in cricket
 * matches. These statistics include dismissals, best performances, and additional derived metrics.
 */
object JooqFieldingIndividualGrounds {

    /**
     * Creates a Common Table Expression (CTE) to count player innings based on the specified match type
     * and persists the results in a temporary table. Additionally, it manages the creation and indexing
     * of the temporary table for optimized querying.
     *
     * @param context The DSLContext used for building and executing SQL queries.
     * @param fieldingTableName The name of the table containing fielding data.
     * @param inningsCountTableName The name of the temporary table to store the innings count results.
     * @param matchType The type of match to filter and process data.
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
                field("matches.LocationId", Int::class.java),
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
                .groupBy(field("tid.playerId"), field("matches.LocationId"))
        ).execute()

        context.dropIndexIfExists("idx_tmp_innings_playerid").on(inningsCountTableName).execute()
        context.createIndexIfNotExists("idx_tmp_innings_playerid").on(inningsCountTableName, "PLAYERID").execute()

    }

    /**
     * Creates a temporary table for storing the best fielding statistics for players based on data
     * from the provided fielding table. The method drops the existing table and index if they exist,
     * then creates a new temporary table and index with the desired schema and constraints.
     *
     * @param context The DSLContext used for executing database operations.
     * @param fieldingTableName The name of the source table containing fielding statistics.
     * @param bestFieldingTableName The name of the temporary table to be created for the best fielding statistics.
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
                field("locationId", String::class.java),
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
                    field("locationId", Int::class.java),
                    rowNumber().over().partitionBy(field("playerid"), field("locationId"))
                        .orderBy(field("dismissals").desc(), field("caughtwk").desc()).`as`("rn")

                ).from(fieldingTableName)
            ).where(field("rn", Int::class.java).eq(inline(1)))
        ).execute()

        context.dropIndexIfExists("idx_tmp_fielding_playerid").on(bestFieldingTableName).execute()
        context.createIndexIfNotExists("idx_tmp_fielding_playerid").on(bestFieldingTableName, "PLAYERID").execute()

    }

    /**
     * Creates a Common Table Expression (CTE) query for retrieving aggregated fielding statistics
     * based on the given search parameters and temporary data sources.
     *
     * The query involves extensive use of joins, conditional filters, groupings, and computations for
     * various fielding statistics, including matches, innings, dismissals, best performances, and team/ground details.
     *
     * @param searchParameters Validated search parameters for customizing the query based on match type,
     *                         teams, venues, dates, results, and pagination.
     * @param tmpFieldingDetailsName The name of the temporary table containing detailed fielding data.
     * @param tempTeamsName The name of the temporary table containing team associations for players.
     * @param tempMatchCountsName The name of the temporary table with pre-aggregated match counts.
     * @param inningsCountCteName The name of the CTE containing innings count data for players.
     * @param bestDismissalsCteName The name of the CTE containing the best dismissals statistics for players.
     * @return The resulting SQL query represented as a `SelectJoinStep<Record19>` object, which includes
     *         various fielding statistics and associated metadata.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpFieldingDetailsName: String,
        tempTeamsName: String,
        tempMatchCountsName: String,
        inningsCountCteName: String,
        bestDismissalsCteName: String,
    ): SelectJoinStep<Record21<Int?, String?, String?, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, String, String, String?, String?>> {


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
                GROUNDS.KNOWNAS.`as`("ground"),
                GROUNDS.COUNTRYNAME,
            ).from(
                select(
                    field("${tempMatchCountsName}.count").`as`("matches"),
                    field("${inningsCountCteName}.innings").`as`("innings"),
                    field("${tmpFieldingDetailsName}.playerId"),
                    field("${tmpFieldingDetailsName}.locationId"),
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
                    .and(field("${tempMatchCountsName}.locationId", Int::class.java).eq(field("${tmpFieldingDetailsName}.LocationId", Int::class.java)))
                    .join(inningsCountCteName).on(
                        field(
                            "${tmpFieldingDetailsName}.playerid",
                            Int::class.java
                        ).eq(field("${inningsCountCteName}.playerid", Int::class.java))
                    )
                    .and(field("${inningsCountCteName}.LocationId").eq(field("${tmpFieldingDetailsName}.LocationId")))
                    .join(EXTRAMATCHDETAILS)
                    .on(EXTRAMATCHDETAILS.MATCHID.eq(field("${tmpFieldingDetailsName}.matchid", Int::class.java)))
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("${tmpFieldingDetailsName}.teamid", Int::class.java)))
                    .and(searchCondition)
                    .leftJoin(bestDismissalsCteName)
                    .on(field("${bestDismissalsCteName}.playerId").eq(field("${tmpFieldingDetailsName}.playerId")))
                    .and(field("${bestDismissalsCteName}.matchType").eq(field("${tmpFieldingDetailsName}.matchType")))
                    .and(field("${bestDismissalsCteName}.locationId").eq(field("${tmpFieldingDetailsName}.locationId")))
                    .groupBy(field("matchtype"), field("playerid"), field("${tmpFieldingDetailsName}.LocationId"))
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
                    .and(field("${tempTeamsName}.locationId", Int::class.java).eq(field("innings.locationid", Int::class.java)))

                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(searchParameters.opponentsId.id))
                    .join(GROUNDS).on(GROUNDS.ID.eq(field("innings.locationId", Int::class.java)))
                    .where(coalesce(field("dismissals").ge(searchParameters.pagingParameters.limit), inline(0)))
            )

        return cte
    }


}