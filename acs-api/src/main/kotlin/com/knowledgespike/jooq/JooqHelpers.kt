package com.knowledgespike.jooq

import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.core.type.values.TeamId
import com.knowledgespike.db.tables.Battingdetails.Companion.BATTINGDETAILS
import com.knowledgespike.db.tables.Bowlingdetails.Companion.BOWLINGDETAILS
import com.knowledgespike.db.tables.Extramatchdetails.Companion
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.Playersteams.Companion.PLAYERSTEAMS
import com.knowledgespike.db.tables.Teams.Companion.TEAMS
import com.knowledgespike.db.tables.Teamsmatchtypes.Companion.TEAMSMATCHTYPES
import com.knowledgespike.db.tables.references.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.references.FIELDING
import com.knowledgespike.db.tables.references.MATCHSUBTYPE
import com.knowledgespike.db.tables.references.PLAYERSMATCHES
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import org.jooq.*
import org.jooq.impl.DSL.*


/**
 * Utility class for creating and handling temporary database tables and complex queries
 * with the jOOQ library for cricket-related data.
 */
object JooqHelpers {
    /**
     * Calculates the total count of records in the specified Common Table Expression (CTE).
     *
     * @param resultsCteName The name of the Common Table Expression (CTE) to perform the count on.
     * @return A SelectJoinStep containing the count of records within the specified CTE.
     */
    fun totalCountsCte(
        resultsCteName: String,
    ): SelectJoinStep<Record1<Int>> = select(count().`as`("count"))
        .from(resultsCteName)

    /**
     * Creates a temporary table with match IDs based on the specified search parameters.
     *
     * The method filters match IDs from the database by applying various conditions derived
     * from the `ValidatedSearchParameters` object and stores the results in a temporary table.
     *
     * @param context The DSLContext used for executing database queries.
     * @param tableName The name of the temporary table to be created.
     * @param searchParameters A ValidatedSearchParameters object containing filtering criteria
     * such as team, opponents, ground, host country, match type, date range, or season.
     */
    fun createTemporaryMatchIds(
        context: DSLContext,
        tableName: String,
        searchParameters: ValidatedSearchParameters,
    ) {
        val searchCondition = buildSearchConditions(searchParameters)

        context.dropTableIfExists(tableName).execute()
        context.createTemporaryTableIfNotExists(tableName).`as`(
            select(MATCHES.ID).from(MATCHES)
                .join(EXTRAMATCHDETAILS).on(MATCHES.ID.eq(EXTRAMATCHDETAILS.MATCHID))
                .where(
                    field("id").`in`(
                        select(BATTINGDETAILS.MATCHID)
                            .from(BATTINGDETAILS)
                            .where(BATTINGDETAILS.PLAYERID.ne(1))
                            .and(
                                BATTINGDETAILS.MATCHID.`in`(
                                    select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                                        .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                                )
                            )
                            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
                            .and(searchCondition)
                    )
                )
        )
            .execute()
    }

    /**
     * Creates a temporary table to store team details based on player-team relationships
     * and match types, using the provided context. This method drops any existing temporary
     * table with the given name, creates a new one with aggregated data, and sets up an index
     * for faster lookups.
     *
     * @param context The DSLContext used for executing SQL queries.
     * @param teamsDetailsTempTableName The name of the temporary table to be created or updated.
     * @param matchType The type of match used as a filter for generating team details.
     */
    fun createTemporaryTeamsTable(
        context: DSLContext,
        teamsDetailsTempTableName: String,
        matchIdTempTableName: String,
        matchType: MatchType,
        matchSubType: MatchType,
    ) {
        context.dropTableIfExists(teamsDetailsTempTableName).execute()
        //todo: aggregateDistinct should be groupConcatDistinct when fixed in JOOQ
        context.createTemporaryTableIfNotExists(teamsDetailsTempTableName).`as`(
            select(
                PLAYERSTEAMS.PLAYERID,
                TEAMSMATCHTYPES.MATCHTYPE,
                aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
            ).from(PLAYERSTEAMS)
                .join(TEAMS).on(TEAMS.ID.eq(PLAYERSTEAMS.TEAMID))
                .join(PLAYERSMATCHES).on(PLAYERSMATCHES.PLAYERID.eq(PLAYERSTEAMS.PLAYERID))
                .and(PLAYERSMATCHES.MATCHID.`in`(
                    select(
                        field("id", Int::class.java)
                    ).from(matchIdTempTableName)
                ))
                .and(PLAYERSMATCHES.TEAMID.eq(TEAMS.ID))
                .join(TEAMSMATCHTYPES)
                .on(TEAMS.ID.eq(TEAMSMATCHTYPES.TEAMID))
                .and(TEAMSMATCHTYPES.MATCHTYPE.eq(matchType.value))
                .and(PLAYERSTEAMS.MATCHTYPE.eq(matchSubType.value))
                .where(PLAYERSTEAMS.PLAYERID.ne(1))
                .groupBy(
                    PLAYERSTEAMS.PLAYERID,
                    TEAMSMATCHTYPES.MATCHTYPE
                )
        ).execute()

        context.dropIndexIfExists("idx_tmp_teams_playerid").on(teamsDetailsTempTableName).execute()
        context.createIndexIfNotExists("idx_tmp_teams_playerid").on(teamsDetailsTempTableName, "PLAYERID").execute()
    }

    /**
     * Creates a temporary table for team-related match data based on a specific season.
     * The data in the table includes information about players, match dates, match types,
     * and grouped team names, and is aggregated from existing match and team records.
     *
     * Indexes are created on relevant columns in the temporary table to facilitate efficient queries.
     *
     * @param context The DSLContext used to execute SQL operations.
     * @param temporaryTeamsTableName The name of the temporary table to be created.
     * @param matchType The type of match to filter the data. Only records matching this type
     *                  will be included in the temporary table.
     * @param teamId The ID of the team to filter the data. If the teamId is 0, no team-based
     *               filter is applied.
     */
    fun createTemporaryTeamsTableBySeason(
        context: DSLContext,
        temporaryTeamsTableName: String,
        matchType: MatchType,
        teamId: TeamId
    ) {
        var whereClause = PLAYERSMATCHES.PLAYERID.ne(1).and(MATCHES.MATCHTYPE.eq(matchType.value))

        if (teamId.id != 0) {
            whereClause = whereClause.and(TEAMS.ID.eq(teamId.id))
        }

        context.dropTableIfExists(temporaryTeamsTableName).execute()
        context.createTemporaryTableIfNotExists(temporaryTeamsTableName).`as`(
            select(
                PLAYERSMATCHES.PLAYERID,
                MATCHES.SERIESDATE,
                MATCHES.MATCHTYPE,
                aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
            ).from(PLAYERSMATCHES)
                .join(MATCHES).on(MATCHES.ID.eq(PLAYERSMATCHES.MATCHID))
                .join(TEAMS).on(TEAMS.ID.eq(PLAYERSMATCHES.TEAMID))
                .where(whereClause)
                .groupBy(
                    PLAYERSMATCHES.PLAYERID,
                    MATCHES.SERIESDATE,
                    MATCHES.MATCHTYPE
                )
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table to aggregate information about teams by match start year.
     * This method dynamically creates a temporary table based on provided criteria,
     * including match type and team ID, and generates a grouped summary of teams
     * for specific match types and years. It ensures appropriate indices are created
     * for efficient querying.
     *
     * @param context The `DSLContext` used to execute SQL queries within the database.
     * @param temporaryTeamsTableName The name of the temporary table to be created.
     * @param matchType The type of match to filter and group data by.
     * @param teamId The specific team identifier to filter the results. If `teamId` is zero, no filtering by team ID is applied.
     */
    fun createTemporaryTeamsTableByMatchStartYear(
        context: DSLContext,
        temporaryTeamsTableName: String,
        matchType: MatchType,
        teamId: TeamId
    ) {
        var whereClause = PLAYERSMATCHES.PLAYERID.ne(1)
            .and(MATCHES.MATCHTYPE.eq(matchType.value))

        if (teamId.id != 0) {
            whereClause = whereClause.and(TEAMS.ID.eq(teamId.id))
        }

        context.dropTableIfExists(temporaryTeamsTableName).execute()
        context.createTemporaryTableIfNotExists(temporaryTeamsTableName).`as`(
            select(
                PLAYERSMATCHES.PLAYERID,
                MATCHES.MATCHSTARTYEAR,
                MATCHES.MATCHTYPE,
                aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
            ).from(PLAYERSMATCHES)
                .join(MATCHES).on(MATCHES.ID.eq(PLAYERSMATCHES.MATCHID))
                .join(TEAMS).on(TEAMS.ID.eq(PLAYERSMATCHES.TEAMID))
                .where(whereClause)
                .groupBy(
                    PLAYERSMATCHES.PLAYERID,
                    MATCHES.MATCHSTARTYEAR,
                    MATCHES.MATCHTYPE
                )
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary teams table based on the specified grounds.
     * This function dynamically builds and executes SQL queries using the DSLContext
     * to create a temporary table, aggregate team names, and configure indexes for optimized queries.
     *
     * @param context The DSLContext used to execute SQL queries and manage the temporary table.
     * @param temporaryTeamsTableName The name of the temporary table to be created.
     * @param matchType The type of match, represented as a MatchType, used to filter records.
     * @param teamId The ID of the team, represented as a TeamId, used to add an optional team-specific filter.
     */
    fun createTemporaryTeamsTableByGround(
        context: DSLContext,
        temporaryTeamsTableName: String,
        matchType: MatchType,
        teamId: TeamId
    ) {
        var whereClause = PLAYERSMATCHES.PLAYERID.ne(1).and(MATCHES.MATCHTYPE.eq(matchType.value))

        if (teamId.id != 0) {
            whereClause = whereClause.and(TEAMS.ID.eq(teamId.id))
        }

        context.dropTableIfExists(temporaryTeamsTableName).execute()
        context.createTemporaryTableIfNotExists(temporaryTeamsTableName).`as`(
            select(
                PLAYERSMATCHES.PLAYERID,
                MATCHES.LOCATIONID,
                MATCHES.MATCHTYPE,
                aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
            ).from(PLAYERSMATCHES)
                .join(MATCHES).on(MATCHES.ID.eq(PLAYERSMATCHES.MATCHID))
                .join(TEAMS).on(TEAMS.ID.eq(PLAYERSMATCHES.TEAMID))
                .where(whereClause)
                .groupBy(
                    PLAYERSMATCHES.PLAYERID,
                    MATCHES.LOCATIONID,
                    MATCHES.MATCHTYPE
                )
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table in the database, populated with information
     * about teams grouped by players, match home country, and match type, based
     * on the given parameters.
     *
     * The method first constructs a `where` clause to filter data according to
     * the match type and optionally the team ID. It then creates a temporary table
     * and populates it by selecting the relevant data from several joined tables.
     * In addition, it handles the creation and deletion of indices for efficient query performance.
     *
     * @param context The `DSLContext` used for database operations.
     * @param temporaryTeamsTableName The name of the temporary table to be created.
     * @param matchType The type of match used to filter the data.
     * @param teamId An optional team ID to filter data; if 0, no filtering by team ID is applied.
     */
    fun createTemporaryTeamsTableByHostCountry(
        context: DSLContext,
        temporaryTeamsTableName: String,
        matchType: MatchType,
        teamId: TeamId
    ) {
        var whereClause = PLAYERSMATCHES.PLAYERID.ne(1).and(MATCHES.MATCHTYPE.eq(matchType.value))

        if (teamId.id != 0) {
            whereClause = whereClause.and(TEAMS.ID.eq(teamId.id))
        }

        context.dropTableIfExists(temporaryTeamsTableName).execute()
        context.createTemporaryTableIfNotExists(temporaryTeamsTableName).`as`(
            select(
                PLAYERSMATCHES.PLAYERID,
                MATCHES.HOMECOUNTRYID,
                MATCHES.MATCHTYPE,
                aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
            ).from(PLAYERSMATCHES)
                .join(MATCHES).on(MATCHES.ID.eq(PLAYERSMATCHES.MATCHID))
                .join(TEAMS).on(TEAMS.ID.eq(PLAYERSMATCHES.TEAMID))
                .where(whereClause)
                .groupBy(
                    PLAYERSMATCHES.PLAYERID,
                    MATCHES.HOMECOUNTRYID,
                    MATCHES.MATCHTYPE
                )
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary teams table based on opponents' data and specific filter conditions.
     *
     * This method dynamically generates a temporary table using metadata from the `PLAYERSMATCHES`,
     * `BATTINGDETAILS`, and `TEAMS` tables. It filters the data based on the specified match type
     * and optional team ID, constructs a grouped result set, and creates indexes on the temporary table
     * for optimized query performance.
     *
     * @param context The `DSLContext` instance used to interact with the database.
     * @param temporaryTeamsTableName The name of the temporary table to be created.
     * @param matchType The type of match used to filter data, represented as an instance of `MatchType`.
     * @param teamId The ID of the team to filter data by, represented as an instance of `TeamId`.
     *               If the ID is set to `0`, no team-specific filtering is applied.
     */
    fun createTemporaryTeamsTableByOpponents(
        context: DSLContext,
        temporaryTeamsTableName: String,
        matchType: MatchType,
        teamId: TeamId
    ) {
        var whereClause = PLAYERSMATCHES.PLAYERID.ne(1).and(BATTINGDETAILS.MATCHTYPE.eq(matchType.value))

        if (teamId.id != 0) {
            whereClause = whereClause.and(TEAMS.ID.eq(teamId.id))
        }

        context.dropTableIfExists(temporaryTeamsTableName).execute()
        context.createTemporaryTableIfNotExists(temporaryTeamsTableName).`as`(
            select(
                PLAYERSMATCHES.PLAYERID,
                BATTINGDETAILS.OPPONENTSID,
                BATTINGDETAILS.MATCHTYPE,
                aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
            ).from(PLAYERSMATCHES)
                .join(BATTINGDETAILS).on(BATTINGDETAILS.MATCHID.eq(PLAYERSMATCHES.MATCHID))
                .join(TEAMS).on(TEAMS.ID.eq(PLAYERSMATCHES.TEAMID))
                .where(whereClause)
                .groupBy(
                    PLAYERSMATCHES.PLAYERID,
                    BATTINGDETAILS.OPPONENTSID,
                    BATTINGDETAILS.MATCHTYPE
                )
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary teams table based on the series information, match type, and team ID.
     * This method generates an in-memory table with aggregated team data, ensuring indexes for optimized queries.
     *
     * @param context The `DSLContext` used for executing SQL queries and managing the temporary table lifecycle.
     * @param temporaryTeamsTableName The name of the temporary table to create.
     * @param matchType The type of match to filter data, encapsulated within the `MatchType` value class.
     * @param teamId The team ID used to filter and refine the query, encapsulated within the `TeamId` value class.
     */
    fun createTemporaryTeamsTableBySeries(
        context: DSLContext,
        temporaryTeamsTableName: String,
        matchType: MatchType,
        teamId: TeamId
    ) {

        var whereClause = PLAYERSMATCHES.PLAYERID.ne(1).and(MATCHES.MATCHTYPE.eq(matchType.value))

        if (teamId.id != 0) {
            whereClause = whereClause.and(TEAMS.ID.eq(teamId.id))
        }

        context.dropTableIfExists(temporaryTeamsTableName).execute()
        context.createTemporaryTableIfNotExists(temporaryTeamsTableName).`as`(
            select(
                PLAYERSMATCHES.PLAYERID,
                MATCHES.SERIESDATE,
                MATCHES.SERIESNUMBER,
                MATCHES.MATCHTYPE,
                aggregateDistinct("group_concat", String::class.java, TEAMS.NAME).`as`("Teams")
            ).from(PLAYERSMATCHES)
                .join(MATCHES).on(MATCHES.ID.eq(PLAYERSMATCHES.MATCHID))
                .join(TEAMS).on(TEAMS.ID.eq(PLAYERSMATCHES.TEAMID))
                .where(whereClause)
                .groupBy(
                    PLAYERSMATCHES.PLAYERID,
                    MATCHES.SERIESDATE,
                    MATCHES.SERIESNUMBER,
                    MATCHES.MATCHTYPE
                )
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(temporaryTeamsTableName, "PLAYERID")
            .execute()

    }


    /**
     * Creates a temporary table for batting details with the specified name and populates it with data based
     * on the contents of the provided match ID temporary table. The batting details table is dropped first
     * if it already exists before being recreated and populated.
     *
     * @param context the DSL context used to execute SQL queries.
     * @param battingDetailsTempTableName the name of the temporary batting details table to be created.
     * @param matchIdTempTableName the name of the temporary table containing match IDs used as a filter
     * in the data selection process.
     */
    fun createTemporaryBattingDetailsTable(
        context: DSLContext,
        battingDetailsTempTableName: String,
        matchIdTempTableName: String,
        searchParameters: ValidatedSearchParameters,
    ) {

        val resultAndHomeOrAwayCondition =
            buildMatchResultAndHomeAndAwayCondition(searchParameters.venue, searchParameters.result)

        var whereClause = BATTINGDETAILS.MATCHID.`in`(
            select(
                field("id", Int::class.java)
            ).from(matchIdTempTableName)
        ).and(
            BATTINGDETAILS.PLAYERID.ne(1)
        )

        if (searchParameters.teamId.id != 0) {
            whereClause = whereClause.and(BATTINGDETAILS.TEAMID.eq(searchParameters.teamId.id))
        }

        context.dropTableIfExists(battingDetailsTempTableName).execute()
        context.createTemporaryTableIfNotExists(battingDetailsTempTableName).`as`(
            select(
                BATTINGDETAILS.ID,
                BATTINGDETAILS.MATCHID,
                BATTINGDETAILS.MATCHTYPE,
                BATTINGDETAILS.PLAYERID,
                BATTINGDETAILS.TEAMID,
                BATTINGDETAILS.OPPONENTSID,
                BATTINGDETAILS.INNINGSNUMBER,
                BATTINGDETAILS.DISMISSALTYPE,
                BATTINGDETAILS.SCORE,
                BATTINGDETAILS.POSITION,
                BATTINGDETAILS.NOTOUT,
                BATTINGDETAILS.NOTOUTADJUSTEDSCORE,
                BATTINGDETAILS.BALLS,
                BATTINGDETAILS.FOURS,
                BATTINGDETAILS.SIXES,
                BATTINGDETAILS.HUNDRED,
                BATTINGDETAILS.FIFTY,
                BATTINGDETAILS.DUCK,
                BATTINGDETAILS.MINUTES,
                BATTINGDETAILS.CAPTAIN,
                BATTINGDETAILS.WICKETKEEPER,
                BATTINGDETAILS.INNINGSORDER,
                BATTINGDETAILS.DISMISSAL,
                BATTINGDETAILS.GROUNDID.`as`("locationId"),
                MATCHES.HOMECOUNTRYID,
                MATCHES.SERIESDATE,
                MATCHES.SERIESNUMBER,
                MATCHES.MATCHSTARTYEAR,
            ).from(BATTINGDETAILS)
                .join(EXTRAMATCHDETAILS).on(EXTRAMATCHDETAILS.MATCHID.eq(BATTINGDETAILS.MATCHID))
                .and(EXTRAMATCHDETAILS.TEAMID.eq(BATTINGDETAILS.TEAMID))
                .join(MATCHES).on(MATCHES.ID.eq(BATTINGDETAILS.MATCHID))
                .and(resultAndHomeOrAwayCondition)
                .where(whereClause)
        ).execute()
    }


    /**
     * Creates a temporary table for bowling details based on the provided search parameters and conditions.
     * The method first drops any existing table with the same name, then generates a new temporary table
     * that contains filtered and processed bowling statistics to be used for further analysis or reporting.
     *
     * @param context The `DSLContext` used to execute database queries and statements.
     * @param bowlingDetailsTempTableName The name of the temporary table to be created for bowling details.
     * @param matchIdTempTableName The name of the temporary table containing match IDs used for filtering.
     * @param searchParameters The validated input parameters that contain filters for venue, results, team ID,
     * and other criteria to query and filter relevant bowling statistics.
     */
    fun createTemporaryBowlingDetailsTable(
        context: DSLContext,
        bowlingDetailsTempTableName: String,
        matchIdTempTableName: String,
        searchParameters: ValidatedSearchParameters
    ) {

        val resultAndHomeOrAwayCondition =
            buildMatchResultAndHomeAndAwayCondition(searchParameters.venue, searchParameters.result)

        var whereClause = BOWLINGDETAILS.MATCHID.`in`(
            select(
                field("id", Int::class.java)
            ).from(matchIdTempTableName)
        ).and(
            BOWLINGDETAILS.PLAYERID.ne(1)
        )

        if (searchParameters.teamId.id != 0) {
            whereClause = whereClause.and(BOWLINGDETAILS.TEAMID.eq(searchParameters.teamId.id))
        }

        context.dropTableIfExists(bowlingDetailsTempTableName).execute()
        context.createTemporaryTableIfNotExists(bowlingDetailsTempTableName).`as`(
            select(
                BOWLINGDETAILS.ID,
                BOWLINGDETAILS.MATCHID,
                BOWLINGDETAILS.MATCHTYPE,
                BOWLINGDETAILS.PLAYERID,
                BOWLINGDETAILS.DIDBOWL,
                BOWLINGDETAILS.INNINGSNUMBER,
                BOWLINGDETAILS.INNINGSORDER,
                BOWLINGDETAILS.POSITION,
                BOWLINGDETAILS.TEAMID,
                BOWLINGDETAILS.OPPONENTSID,
                BOWLINGDETAILS.BALLS,
                BOWLINGDETAILS.MAIDENS,
                BOWLINGDETAILS.RUNS,
                BOWLINGDETAILS.WICKETS,
                BOWLINGDETAILS.WIDES,
                BOWLINGDETAILS.NOBALLS,
                BOWLINGDETAILS.DOTS,
                BOWLINGDETAILS.FOURS,
                BOWLINGDETAILS.SIXES,
                BOWLINGDETAILS.TENFOR,
                BOWLINGDETAILS.SYNTHETICBESTBOWLING,
                BOWLINGDETAILS.SYNTHETICBESTBOWLINGMATCH,
                MATCHES.SERIESNUMBER,
                MATCHES.SERIESDATE,
                MATCHES.MATCHSTARTYEAR,
                BOWLINGDETAILS.CAPTAIN,
                BOWLINGDETAILS.GROUNDID,
                BOWLINGDETAILS.GROUNDID.`as`("locationId"),
                MATCHES.HOMECOUNTRYID,
            ).from(BOWLINGDETAILS)
                .join(MATCHES).on(MATCHES.ID.eq(BOWLINGDETAILS.MATCHID))
                .join(EXTRAMATCHDETAILS).on(EXTRAMATCHDETAILS.MATCHID.eq(BOWLINGDETAILS.MATCHID))
                .and(EXTRAMATCHDETAILS.TEAMID.eq(BOWLINGDETAILS.TEAMID))
                .and(resultAndHomeOrAwayCondition)
                .where(whereClause)
        ).execute()
    }

    /**
     * Creates a temporary database table to store the count of matches for each player, based on match IDs from
     * a specified temporary table. Additionally, it creates and indexes the temporary table for efficient querying.
     *
     * @param context JOOQ's DSLContext used for executing queries and database interaction.
     * @param matchCountsTempTableName Name of the temporary table to store match counts for players.
     * @param matchIdTempTableName Name of the existing temporary table containing match IDs to filter data.
     */
    fun createTemporaryMatchCountsTable(
        context: DSLContext,
        searchParameters: ValidatedSearchParameters,
        matchCountsTempTableName: String,
        matchIdTempTableName: String,
    ) {

        val resultAndHomeOrAwayCondition =
            buildMatchResultAndHomeAndAwayCondition(searchParameters.venue, searchParameters.result)

        context.dropTableIfExists(matchCountsTempTableName).execute()
        context.createTemporaryTableIfNotExists(matchCountsTempTableName).`as`(
            select(PLAYERSMATCHES.PLAYERID, count().`as`("count"))
                .from(PLAYERSMATCHES)
                .join(EXTRAMATCHDETAILS).on(EXTRAMATCHDETAILS.MATCHID.eq(PLAYERSMATCHES.MATCHID))
                .and(EXTRAMATCHDETAILS.TEAMID.eq(PLAYERSMATCHES.TEAMID))
                .and(resultAndHomeOrAwayCondition)
                .where(
                    PLAYERSMATCHES.MATCHID.`in`(
                        select(field("id", Int::class.java))
                            .from(matchIdTempTableName)
                    ).and(PLAYERSMATCHES.ISSUBSTITUTEFIELDER.ne(1))
                ).groupBy(PLAYERSMATCHES.PLAYERID)
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table to store match counts grouped by player ID and home country ID. The table is
     * generated based on data from the provided match ID temporary table. Indexes are created on the resulting
     * temporary table to improve query performance.
     *
     * @param context The DSLContext used for executing SQL statements.
     * @param matchCountsTempTableName The name of the temporary table to store match counts. The table is dropped
     * before creation if it already exists.
     * @param matchIdTempTableName The name of the temporary table containing match IDs that is used as a source
     * for filtering matches relevant to the new temporary table.
     */
    fun createTemporaryMatchCountsTableByHostCountry(
        context: DSLContext,
        matchCountsTempTableName: String,
        matchIdTempTableName: String,
    ) {
        context.dropTableIfExists(matchCountsTempTableName).execute()
        context.createTemporaryTableIfNotExists(matchCountsTempTableName).`as`(
            select(PLAYERSMATCHES.PLAYERID, MATCHES.HOMECOUNTRYID, count().`as`("count")).from(PLAYERSMATCHES)
                .join(MATCHES).on(
                    MATCHES.ID.eq(
                        PLAYERSMATCHES.MATCHID
                    )
                )
                .where(
                    PLAYERSMATCHES.MATCHID.`in`(
                        select(field("id", Int::class.java))
                            .from(matchIdTempTableName)
                    ).and(PLAYERSMATCHES.ISSUBSTITUTEFIELDER.ne(1))
                )
                .groupBy(MATCHES.MATCHTYPE, PLAYERSMATCHES.PLAYERID, MATCHES.HOMECOUNTRYID)
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table to count player matches by ground and configures an index on the resulting table.
     *
     * @param context The DSLContext used to execute SQL queries.
     * @param matchCountsTempTableName The name of the temporary table to be created for storing match counts.
     * @param matchIdTempTableName The name of the temporary table used to filter match IDs for counting.
     */
    fun createTemporaryMatchCountsTableByGround(
        context: DSLContext,
        matchCountsTempTableName: String,
        matchIdTempTableName: String,
    ) {
        context.dropTableIfExists(matchCountsTempTableName).execute()
        context.createTemporaryTableIfNotExists(matchCountsTempTableName).`as`(
            select(
                PLAYERSMATCHES.PLAYERID,
                com.knowledgespike.db.tables.references.MATCHES.LOCATIONID,
                count().`as`("count")
            ).from(PLAYERSMATCHES)
                .join(com.knowledgespike.db.tables.references.MATCHES)
                .on(com.knowledgespike.db.tables.references.MATCHES.ID.eq(PLAYERSMATCHES.MATCHID))
                .where(
                    PLAYERSMATCHES.MATCHID.`in`(
                        select(field("id", Int::class.java))
                            .from(matchIdTempTableName)
                    ).and(PLAYERSMATCHES.ISSUBSTITUTEFIELDER.ne(1))
                )
                .groupBy(
                    com.knowledgespike.db.tables.references.MATCHES.MATCHTYPE,
                    PLAYERSMATCHES.PLAYERID,
                    com.knowledgespike.db.tables.references.MATCHES.LOCATIONID
                )
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table for match counts by opposition in the database.
     * The method first drops the table if it exists, then creates a temporary table
     * populated with data about players, their opponents, and the count of matches.
     * Additionally, it ensures the creation of an index on the `PLAYERID` column
     * for optimized access.
     *
     * @param context The DSLContext instance used to interact with the database.
     * @param matchCountsTempTableName Name of the temporary table to store match counts by opposition.
     * @param matchIdTempTableName Name of the temporary table containing match IDs to filter data.
     */
    fun createTemporaryMatchCountsTableByOpposition(
        context: DSLContext,
        matchCountsTempTableName: String,
        matchIdTempTableName: String,
    ) {
        context.dropTableIfExists(matchCountsTempTableName).execute()
        context.createTemporaryTableIfNotExists(matchCountsTempTableName).`as`(
            select(PLAYERSMATCHES.PLAYERID, BOWLINGDETAILS.OPPONENTSID, count().`as`("count")).from(PLAYERSMATCHES)
                .join(BOWLINGDETAILS).on(
                    BOWLINGDETAILS.MATCHID.eq(
                        PLAYERSMATCHES.MATCHID
                    )
                )
                .and(BOWLINGDETAILS.PLAYERID.eq(PLAYERSMATCHES.PLAYERID))
                .and(BOWLINGDETAILS.INNINGSNUMBER.eq(1))
                .where(
                    PLAYERSMATCHES.MATCHID.`in`(
                        select(field("id", Int::class.java))
                            .from(matchIdTempTableName)
                    ).and(PLAYERSMATCHES.ISSUBSTITUTEFIELDER.ne(1))
                )
                .groupBy(BOWLINGDETAILS.MATCHTYPE, PLAYERSMATCHES.PLAYERID, BOWLINGDETAILS.OPPONENTSID)
        ).execute()
        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table to store match counts by season for players. The table is populated with data
     * filtered and grouped by match type, player ID, and series date. Additionally, creates an index on the
     * player ID for the temporary table.
     *
     * @param context The DSLContext to execute SQL queries.
     * @param matchCountsTempTableName The name of the temporary table to store match counts data.
     * @param matchIdTempTableName The name of the temporary table containing match IDs used for filtering.
     */
    fun createTemporaryMatchCountsTableBySeason(
        context: DSLContext,
        matchCountsTempTableName: String,
        matchIdTempTableName: String,
    ) {
        context.dropTableIfExists(matchCountsTempTableName).execute()
        context.createTemporaryTableIfNotExists(matchCountsTempTableName).`as`(
            select(PLAYERSMATCHES.PLAYERID, MATCHES.SERIESDATE, count().`as`("count")).from(PLAYERSMATCHES)
                .join(MATCHES).on(
                    MATCHES.ID.eq(
                        PLAYERSMATCHES.MATCHID
                    )
                )
                .where(
                    PLAYERSMATCHES.MATCHID.`in`(
                        select(field("id", Int::class.java))
                            .from(matchIdTempTableName)
                    ).and(PLAYERSMATCHES.ISSUBSTITUTEFIELDER.ne(1))
                )
                .groupBy(MATCHES.MATCHTYPE, PLAYERSMATCHES.PLAYERID, MATCHES.SERIESDATE)
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table for match counts grouped by series along with an index on the PLAYERID column.
     *
     * @param context The DSLContext used to execute database operations.
     * @param matchCountsTempTableName The name of the temporary table to create for storing match counts data.
     * @param matchIdTempTableName The name of the temporary table containing match IDs to be used for filtering.
     */
    fun createTemporaryMatchCountsTableBySeries(
        context: DSLContext,
        matchCountsTempTableName: String,
        matchIdTempTableName: String,
    ) {
        context.dropTableIfExists(matchCountsTempTableName).execute()
        context.createTemporaryTableIfNotExists(matchCountsTempTableName).`as`(
            select(PLAYERSMATCHES.PLAYERID, MATCHES.SERIESNUMBER, MATCHES.SERIESDATE, count().`as`("count")).from(
                PLAYERSMATCHES
            )
                .join(MATCHES).on(
                    MATCHES.ID.eq(
                        PLAYERSMATCHES.MATCHID
                    )
                )
                .where(
                    PLAYERSMATCHES.MATCHID.`in`(
                        select(field("id", Int::class.java))
                            .from(matchIdTempTableName)
                    ).and(PLAYERSMATCHES.ISSUBSTITUTEFIELDER.ne(1))
                )
                .groupBy(MATCHES.MATCHTYPE, PLAYERSMATCHES.PLAYERID, MATCHES.SERIESNUMBER, MATCHES.SERIESDATE)
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table to store match counts grouped by player ID and year of match start.
     * Additionally, the method ensures indices are properly created for the temporary table
     * after its creation.
     *
     * @param context The DSLContext instance used to interact with the database.
     * @param matchCountsTempTableName The name of the temporary table to be created for storing match counts.
     * @param matchIdTempTableName The name of the temporary table containing match IDs.
     */
    fun createTemporaryMatchCountsTableByYearOfMatchStart(
        context: DSLContext,
        matchCountsTempTableName: String,
        matchIdTempTableName: String,
    ) {
        context.dropTableIfExists(matchCountsTempTableName).execute()
        context.createTemporaryTableIfNotExists(matchCountsTempTableName).`as`(
            select(PLAYERSMATCHES.PLAYERID, MATCHES.MATCHSTARTYEAR, count().`as`("count")).from(PLAYERSMATCHES)
                .join(MATCHES).on(
                    MATCHES.ID.eq(
                        PLAYERSMATCHES.MATCHID
                    )
                )
                .where(
                    PLAYERSMATCHES.MATCHID.`in`(
                        select(field("id", Int::class.java))
                            .from(matchIdTempTableName)
                    ).and(PLAYERSMATCHES.ISSUBSTITUTEFIELDER.ne(1))
                )
                .groupBy(MATCHES.MATCHTYPE, PLAYERSMATCHES.PLAYERID, MATCHES.MATCHSTARTYEAR)
        ).execute()

        context.dropIndexIfExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName).execute()
        context.createIndexIfNotExists("idx_tmp_match_counts_playerid").on(matchCountsTempTableName, "PLAYERID")
            .execute()
    }

    /**
     * Creates a temporary table for storing fielding details by extracting data from an existing fielding
     * records table and applying specified conditions. This method also ensures that any pre-existing table
     * with the temporary table name is dropped before creating a new one.
     *
     * @param context The DSLContext used for executing SQL queries with jOOQ.
     * @param fieldingDetailsTempTableName The name of the temporary table to be created for storing fielding details.
     * @param matchIdTempTableName The name of the temporary table containing match IDs to filter the fielding data.
     */
    fun createTemporaryFieldingDetailsTable(
        context: DSLContext,
        fieldingDetailsTempTableName: String,
        matchIdTempTableName: String,
        searchParameters: ValidatedSearchParameters
    ) {
        val resultAndHomeOrAwayCondition =
            buildMatchResultAndHomeAndAwayCondition(searchParameters.venue, searchParameters.result)

        context.dropTableIfExists(fieldingDetailsTempTableName).execute()
        context.createTemporaryTableIfNotExists(fieldingDetailsTempTableName).`as`(
            select(
                FIELDING.ID,
                FIELDING.MATCHID,
                FIELDING.MATCHTYPE,
                FIELDING.PLAYERID,
                FIELDING.TEAMID,
                FIELDING.OPPONENTSID,
                FIELDING.INNINGSNUMBER,
                FIELDING.CAUGHTF,
                FIELDING.CAUGHTWK,
                FIELDING.STUMPED,
                FIELDING.CAUGHTF.add(FIELDING.CAUGHTWK).add(FIELDING.STUMPED).`as`("dismissals"),
                MATCHES.LOCATIONID,
                MATCHES.HOMECOUNTRYID,
                MATCHES.SERIESDATE,
                MATCHES.SERIESNUMBER,
                MATCHES.MATCHSTARTYEAR,
            ).from(FIELDING)
                .join(MATCHES).on(MATCHES.ID.eq(FIELDING.MATCHID))
                .join(EXTRAMATCHDETAILS).on(EXTRAMATCHDETAILS.MATCHID.eq(FIELDING.MATCHID))
                .and(EXTRAMATCHDETAILS.TEAMID.eq(FIELDING.TEAMID))
                .and(resultAndHomeOrAwayCondition)

                .where(
                    FIELDING.MATCHID.`in`(
                        select(
                            field("id", Int::class.java)
                        ).from(matchIdTempTableName)
                    ).and(
                        FIELDING.PLAYERID.ne(1)
                    )
                )
        ).execute()
    }
}

private fun buildMatchResultAndHomeAndAwayCondition(venue: Int, result: Int): Condition {
    val matchResultCondition = if (result != 0) {
        and(Companion.EXTRAMATCHDETAILS.RESULT.bitAnd(result).notEqual(0))
    } else trueCondition()
    val homeOrAwayCondition = if (venue != 0) {
        and(Companion.EXTRAMATCHDETAILS.HOMEAWAY.bitAnd(venue).notEqual(0))
    } else trueCondition()
    return matchResultCondition.and(homeOrAwayCondition)
}

fun buildSearchConditions(searchParameters: ValidatedSearchParameters): Condition {
    val teamIdCondition = if (searchParameters.teamId.id != 0) {
        and(Companion.EXTRAMATCHDETAILS.TEAMID.eq(searchParameters.teamId.id))
    } else trueCondition()
    val opponentsIdCondition = if (searchParameters.opponentsId.id != 0) {
        and(Companion.EXTRAMATCHDETAILS.OPPONENTSID.eq(searchParameters.opponentsId.id))
    } else trueCondition()
    val groundCondition = if (searchParameters.groundId != 0) {
        and(MATCHES.LOCATIONID.eq(searchParameters.groundId))
    } else trueCondition()
    val homeCountryIdCondition = if (searchParameters.hostCountryId != 0) {
        and(MATCHES.HOMECOUNTRYID.eq(searchParameters.hostCountryId))
    } else trueCondition()

    val dateOrSeasonCondition = if (searchParameters.season != "0" && searchParameters.season != "All Seasons") {
        and(MATCHES.SERIESDATE.eq(searchParameters.season))
    } else {
        and(MATCHES.MATCHSTARTDATEASOFFSET.ge(searchParameters.startDate.value))
            .and(MATCHES.MATCHSTARTDATEASOFFSET.le(searchParameters.endDate.value))
    }

    val matchResultCondition = if (searchParameters.result != 0) {
        and(Companion.EXTRAMATCHDETAILS.RESULT.bitAnd(searchParameters.result).notEqual(0))
    } else trueCondition()
    val homeOrAwayCondition = if (searchParameters.venue != 0) {
        and(Companion.EXTRAMATCHDETAILS.HOMEAWAY.bitAnd(searchParameters.venue).notEqual(0))
    } else trueCondition()
//    return matchResultCondition.and(homeOrAwayCondition)

    return teamIdCondition.and(opponentsIdCondition)
        .and(groundCondition)
        .and(homeCountryIdCondition)
        .and(dateOrSeasonCondition)
        .and(matchResultCondition)
        .and(homeOrAwayCondition)
}