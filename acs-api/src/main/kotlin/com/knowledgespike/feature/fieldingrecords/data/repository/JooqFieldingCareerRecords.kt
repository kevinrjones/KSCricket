package com.knowledgespike.feature.fieldingrecords.data.repository

import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches
import com.knowledgespike.db.tables.Matches.Companion
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
 * Utility object providing methods to handle JOOQ-based database operations
 * for fielding career records.
 */
object JooqFieldingCareerRecords {

    /**
     * Creates an innings count Common Table Expression (CTE) for analyzing player involvement
     * in different innings of matches based on specific match types.
     *
     * @param context The DSLContext used to execute database operations.
     * @param fieldingTableName The name of the fielding table used as the source data.
     * @param inningsCountTableName The name of the temporary table to be created for storing the innings count.
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
                .groupBy(field("tid.playerId"))
        ).execute()

        context.dropIndexIfExists("idx_tmp_innings_playerid").on(inningsCountTableName).execute()
        context.createIndexIfNotExists("idx_tmp_innings_playerid").on(inningsCountTableName, "PLAYERID").execute()

    }

    /**
     * Creates a temporary table to store the best fielding records for each player from the specified source table.
     *
     * @param context The DSLContext used for executing SQL queries.
     * @param fieldingTableName The name of the source fielding table from which data is queried.
     * @param bestFieldingTableName The name of the temporary table to be created and populated with the best fielding records.
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
                field("caughtf", Int::class.java).`as`("bestCaughtFielder"),
                field("caughtWk", Int::class.java).`as`("bestCaughtKeeper"),
                field("stumped", Int::class.java).`as`("bestStumpings"),
                field("dismissals", Int::class.java).`as`("bestDismissals"),
            ).from(
                select(
                    field("playerId", Int::class.java),
                    field("matchType", String::class.java),
                    field("caughtf", Int::class.java),
                    field("caughtwk", Int::class.java),
                    field("stumped", Int::class.java),
                    field("dismissals", Int::class.java),
                    rowNumber().over().partitionBy(field("playerid"))
                        .orderBy(field("dismissals").desc(), field("caughtwk").desc()).`as`("rn")

                ).from(fieldingTableName)
            ).where(field("rn", Int::class.java).eq(inline(1)))
        ).execute()

        context.dropIndexIfExists("idx_tmp_fielding_playerid").on(bestFieldingTableName).execute()
        context.createIndexIfNotExists("idx_tmp_fielding_playerid").on(bestFieldingTableName, "PLAYERID").execute()

    }

    /**
     * Creates a Common Table Expression (CTE) query for retrieving cricket statistics, specifically fielding details.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing criteria for filtering and sorting results.
     * @param tmpFieldingDetailsName The name of the temporary table containing fielding details.
     * @param tempTeamsName The name of the temporary table containing team data.
     * @param tempMatchCountsName The name of the temporary table containing match counts per player.
     * @param inningsCountCteName The name of the CTE containing innings counts per player.
     * @param bestDismissalsCteName The name of the CTE containing best dismissals statistics per player.
     * @return A query object ready for execution that selects records representing detailed fielding statistics.
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
                    .join(inningsCountCteName).on(
                        field(
                            "${tmpFieldingDetailsName}.playerid",
                            Int::class.java
                        ).eq(field("${inningsCountCteName}.playerid", Int::class.java))
                    )
                    .join(Matches.MATCHES).on(field("${tmpFieldingDetailsName}.matchid", Int::class.java).eq(Matches.MATCHES.ID))
                    .join(EXTRAMATCHDETAILS)
                    .on(EXTRAMATCHDETAILS.MATCHID.eq(field("${tmpFieldingDetailsName}.matchid", Int::class.java)))
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("${tmpFieldingDetailsName}.teamid", Int::class.java)))
                    .and(searchCondition)
                    .leftJoin(bestDismissalsCteName)
                    .on(field("${bestDismissalsCteName}.playerId").eq(field("${tmpFieldingDetailsName}.playerId")))
                    .and(field("${bestDismissalsCteName}.matchType").eq(field("${tmpFieldingDetailsName}.matchType")))
                    .groupBy(field("matchtype"), field("playerid"))
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
                    ).and(field("${tempTeamsName}.matchType", String::class.java).eq(searchParameters.matchType.value))
                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(searchParameters.opponentsId.id))
                    .where(coalesce(field("dismissals").ge(searchParameters.pagingParameters.limit), inline(0)))
            )

        return cte
    }


}