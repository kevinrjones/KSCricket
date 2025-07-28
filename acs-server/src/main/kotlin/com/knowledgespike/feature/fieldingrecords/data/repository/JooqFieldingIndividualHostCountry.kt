package com.knowledgespike.feature.fieldingrecords.data.repository

import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Players.Companion.PLAYERS
import com.knowledgespike.db.tables.Teams.Companion.TEAMS
import com.knowledgespike.db.tables.references.COUNTRIES
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
 * This object contains utilities to create and manipulate SQL Common Table Expressions (CTEs) and temporary tables
 * used to generate and analyze cricket fielding statistics with respect to the host country.
 */
object JooqFieldingIndividualHostCountry {

    /**
     * Creates a Common Table Expression (CTE) for tracking the count of innings played by players
     * in a specific match type and temporarily stores the result in a specified table. Additionally,
     * creates an index on the player ID column for optimized database querying. This method utilizes
     * a combination of SQL operations such as table creation, joins, grouping, and indexing.
     *
     * @param context The DSLContext instance used to execute database queries.
     * @param fieldingTableName The name of the fielding table which contains information about players' fielding details.
     * @param inningsCountTableName The name of the table where the inning counts for players will be temporarily stored.
     * @param matchType The type of match for which the innings count is being calculated.
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
                field("matches.HomeCountryId", Int::class.java),
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
                .groupBy(field("tid.playerId"), field("matches.HomeCountryId"))
        ).execute()

        context.dropIndexIfExists("idx_tmp_innings_playerid").on(inningsCountTableName).execute()
        context.createIndexIfNotExists("idx_tmp_innings_playerid").on(inningsCountTableName, "PLAYERID").execute()

    }

    /**
     * Creates a temporary table to store the best fielding statistics for players
     * based on their performance from a given fielding table. If a table with the
     * specified name for best fielding statistics already exists, it is dropped
     * before creating the new one.
     *
     * The method also creates an index on the `playerId` column of the temporary
     * best fielding table for optimized querying.
     *
     * @param context The DSLContext instance used to execute SQL queries.
     * @param fieldingTableName The name of the table containing raw fielding statistics.
     * @param bestFieldingTableName The name of the temporary table to store best fielding statistics.
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
                field("HomeCountryId", String::class.java),
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
                    field("HomeCountryId", Int::class.java),
                    rowNumber().over().partitionBy(field("playerid"), field("HomeCountryId"))
                        .orderBy(field("dismissals").desc(), field("caughtwk").desc()).`as`("rn")

                ).from(fieldingTableName)
            ).where(field("rn", Int::class.java).eq(inline(1)))
        ).execute()

        context.dropIndexIfExists("idx_tmp_fielding_playerid").on(bestFieldingTableName).execute()
        context.createIndexIfNotExists("idx_tmp_fielding_playerid").on(bestFieldingTableName, "PLAYERID").execute()

    }

    /**
     * Creates a Common Table Expression (CTE) for fetching detailed aggregated fielding statistics
     * based on various search parameters and temporary table aliases.
     *
     * This method constructs a SQL query to calculate fielding statistics such as matches, innings,
     * caught dismissals, stumpings, and best performances for players. Additionally, it performs
     * joins across several temporary tables and applies filters based on the provided search parameters.
     *
     * @param searchParameters ValidatedSearchParameters object containing filters and constraints for the query.
     * @param tmpFieldingDetailsName Alias for the temporary fielding details table used in the query.
     * @param tempTeamsName Alias for the temporary teams table used to fetch team details.
     * @param tempMatchCountsName Alias for the temporary match counts table used in aggregation.
     * @param inningsCountCteName Alias for the CTE managing innings count details.
     * @param bestDismissalsCteName Alias for the CTE handling the best dismissal-related statistics.
     * @return A SelectJoinStep<Record18> object representing the SQL CTE for aggregated fielding statistics.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpFieldingDetailsName: String,
        tempTeamsName: String,
        tempMatchCountsName: String,
        inningsCountCteName: String,
        bestDismissalsCteName: String,
    ): SelectJoinStep<Record20<Int?, String?, String?, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, String, String, String?>> {


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
                COUNTRIES.COUNTRYNAME,
            ).from(
                select(
                    field("${tempMatchCountsName}.count").`as`("matches"),
                    field("${inningsCountCteName}.innings").`as`("innings"),
                    field("${tmpFieldingDetailsName}.playerId"),
                    field("${tmpFieldingDetailsName}.HomeCountryId"),
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
                    .and(field("${tempMatchCountsName}.HomeCountryId", Int::class.java).eq(field("${tmpFieldingDetailsName}.HomeCountryId", Int::class.java)))
                    .join(inningsCountCteName).on(
                        field(
                            "${tmpFieldingDetailsName}.playerid",
                            Int::class.java
                        ).eq(field("${inningsCountCteName}.playerid", Int::class.java))
                    )
                    .and(field("${inningsCountCteName}.HomeCountryId").eq(field("${tmpFieldingDetailsName}.HomeCountryId")))
                    .join(EXTRAMATCHDETAILS)
                    .on(EXTRAMATCHDETAILS.MATCHID.eq(field("${tmpFieldingDetailsName}.matchid", Int::class.java)))
                    .and(EXTRAMATCHDETAILS.TEAMID.eq(field("${tmpFieldingDetailsName}.teamid", Int::class.java)))
                    .and(searchCondition)
                    .leftJoin(bestDismissalsCteName)
                    .on(field("${bestDismissalsCteName}.playerId").eq(field("${tmpFieldingDetailsName}.playerId")))
                    .and(field("${bestDismissalsCteName}.matchType").eq(field("${tmpFieldingDetailsName}.matchType")))
                    .and(field("${bestDismissalsCteName}.HomeCountryId").eq(field("${tmpFieldingDetailsName}.HomeCountryId")))
                    .groupBy(field("matchtype"), field("playerid"), field("${tmpFieldingDetailsName}.HomeCountryId"))
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
                    .and(field("${tempTeamsName}.HomeCountryId", Int::class.java).eq(field("innings.HomeCountryId", Int::class.java)))
                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(searchParameters.opponentsId.id))
                    .join(COUNTRIES).on(COUNTRIES.ID.eq(field("innings.HomeCountryId", Int::class.java)))
                    .where(coalesce(field("dismissals").ge(searchParameters.pagingParameters.limit), inline(0)))
            )

        return cte
    }


}