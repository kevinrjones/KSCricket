package com.knowledgespike.feature.bowlingrecords.data.repository

import com.knowledgespike.DIALECT
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.feature.battingrecords.data.repository.JooqBattingRepository
import com.knowledgespike.feature.battingrecords.data.repository.JooqBattingRepository.Companion
import com.knowledgespike.feature.bowlingrecords.domain.model.BowlingInningsByInnings
import com.knowledgespike.feature.bowlingrecords.domain.model.BowlingPrimary
import com.knowledgespike.feature.bowlingrecords.domain.repository.BowlingRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.JooqHelpers
import com.knowledgespike.plugins.DataSource
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL.*

/**
 * Repository implementation for querying bowling statistics using JOOQ.
 *
 * This class provides functionalities to execute various types of
 * queries related to bowling statistics based on validated search parameters.
 * It interacts with the database through JOOQ to fetch, map, and return the
 * desired results.
 *
 * @property dataSource The database connection pool used for retrieving connections.
 */
class JooqBowlingRepository(private val dataSource: DataSource) : BowlingRepository {

    /**
     * A companion object that provides constants representing the names
     * of various temporary and results tables used within the repository.
     */
    companion object {
        /**
         * Represents the name of the temporary database table used for storing
         * intermediate or prepared bowling-related details during query execution
         * within the context of the JooqBowlingRepository.
         *
         * This constant is utilized internally by the repository to manage
         * temporary dataset operations associated with various bowling statistics.
         */
        private const val BOWLING_TEMP_DETALS_TABLE = "tmp_bowl_details"

        /**
         * Represents the name of the temporary table used for storing team-related data during intermediate
         * database operations in the repository. Typically utilized within query generation or result
         * processing to ensure temporary and isolated manipulation of team-specific records.
         */
        private const val TEAMS_TEMP_TABLE = "tmp_teams"

        /**
         * Represents the name of the database table used to store and query bowling results.
         * This constant is utilized in SQL query operations within the JooqBowlingRepository class.
         */
        private const val RESULTS_TABLE = "results"

        /**
         * The name of the database table that stores the total counts data.
         * This constant is utilized in constructing queries related to aggregated bowling statistics.
         */
        private const val TOTAL_COUNTS_TABLE = "total_counts"

        /**
         * Represents the name of a temporary table used within the repository for storing match IDs during data processing.
         * This table is primarily utilized for intermediate operations and is not intended to persist data.
         */
        private const val MATCHIDS_TEMP_TABLE = "tmp_match_ids_table"

        /**
         * Represents the name of the temporary database table used for storing match count data
         * within the JooqBowlingRepository implementation.
         *
         * This constant is primarily intended to streamline SQL operations by serving as a placeholder
         * for the name of the temporary table utilized during intermediate query processing. It aids in
         * maintaining consistency and reducing the potential for hard-coding errors.
         */
        private const val MATCHCOUNTS_TEMP_TABLE = "tmp_match_counts_table"
    }

    /**
     * Retrieves overall individual bowling statistics based on the provided search parameters.
     *
     * This method executes multiple steps to query and compute individual bowling records:
     * - Creates and uses temporary tables for matches, bowling details, teams, and match counts
     *   based on the provided search parameters.
     * - Performs a database query to fetch and sort results according to the specified
     *   search parameters, such as team ID, match type, and sort order.
     * - Maps the query results to domain objects representing primary bowling statistics.
     *
     * @param searchParameters The validated search parameters containing details such as match type,
     *                          team information, sorting order, and paging preferences.
     * @return A `DatabaseResult` containing the list of bowling statistics and the total count.
     */
    override fun getByIndividualOverallBowling(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTable(
            context,
            teamsDetailsTempTableName = TEAMS_TEMP_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.matchSubType
        )
        JooqHelpers.createTemporaryMatchCountsTable(
            context,
            searchParameters,
            MATCHCOUNTS_TEMP_TABLE,
            MATCHIDS_TEMP_TABLE
        )
        val resultsTable =
            JooqBowlingCareerRecords.createResultsCte(
                searchParameters = searchParameters,
                bowlTempName = BOWLING_TEMP_DETALS_TABLE,
                teamsTempName = TEAMS_TEMP_TABLE,
                tempMatchCountsName = MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Retrieves bowling statistics for an individual based on series-specific search parameters.
     *
     * This method executes various operations to filter and compute bowling data from the database,
     * using series as a primary grouping criterion. Temporary tables are generated to handle the
     * filtering, grouping, and aggregation logic. The results are then calculated and transformed
     * into the desired format.
     *
     * @param searchParameters The validated search parameters that specify the criteria for the query,
     * including match type, team, date range, sorting, and paging configurations.
     * @return A DatabaseResult containing a list of BowlingPrimary objects that match the specified
     * search criteria, along with the total count of matching results.
     */
    override fun getByIndividualSeries(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableBySeries(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableBySeries(
            context,
            MATCHCOUNTS_TEMP_TABLE,
            MATCHIDS_TEMP_TABLE
        )
        val resultsTable =
            JooqBowlingIndividualSeries.createResultsCte(
                searchParameters = searchParameters,
                bowlTempName = BOWLING_TEMP_DETALS_TABLE,
                teamsTempName = TEAMS_TEMP_TABLE,
                tempMatchCountsName = MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)

    }

    /**
     * Retrieves bowling statistics for individual players based on specific grounds.
     *
     * This method utilizes various intermediate temporary tables and procedures to
     * process the query parameters and fetch the aggregated results. The results are
     * based on filtering criteria like match type, teams, and grounds, provided
     * through the `ValidatedSearchParameters` object.
     *
     * @param searchParameters The validated search criteria containing match type, team,
     * ground details, sorting preferences, and other filters that dictate the results.
     * @return A DatabaseResult containing the aggregated bowling records and the total
     * number of items matching the provided search parameters.
     */
    override fun getByIndividualGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableByGround(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableByGround(
            context,
            MATCHCOUNTS_TEMP_TABLE,
            MATCHIDS_TEMP_TABLE
        )
        val resultsTable =
            JooqBowlingIndividualGrounds.createResultsCte(
                searchParameters = searchParameters,
                bowlTempName = BOWLING_TEMP_DETALS_TABLE,
                teamsTempName = TEAMS_TEMP_TABLE,
                tempMatchCountsName = MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)

    }

    /**
     * Retrieves bowling statistics based on the host country specified in the search parameters.
     * This method processes data by leveraging various temporary tables for matches, bowling details,
     * teams, and match counts, filtered according to the provided parameters.
     *
     * @param searchParameters A ValidatedSearchParameters object containing the filtering criteria,
     * such as match type, team ID, host country, and additional sorting options.
     * @return A DatabaseResult containing a list of BowlingPrimary results, along with the total
     * count of matching records.
     */
    override fun getByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableByHostCountry(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableByHostCountry(
            context,
            MATCHCOUNTS_TEMP_TABLE,
            MATCHIDS_TEMP_TABLE
        )
        val resultsTable =
            JooqBowlingIndividualHostCountry.createResultsCte(
                searchParameters = searchParameters,
                bowlTempName = BOWLING_TEMP_DETALS_TABLE,
                tempTeamsName = TEAMS_TEMP_TABLE,
                tempMatchCountsName = MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)

    }

    /**
     * Retrieves bowling statistics for a team based on their opponents using the given search parameters.
     *
     * This method performs several steps to prepare the required data, including creating temporary tables
     * for match IDs, bowling details, teams, and match counts. It then assembles a results table and invokes
     * the `generateResults` method to construct the final query result.
     *
     * @param searchParameters The validated search parameters used to filter and query the bowling statistics.
     *                          These parameters include match type, team identifiers, date range, and other
     *                          filtering options necessary for the query.
     * @return A `DatabaseResult` containing a list of `BowlingPrimary` entries and a count of the total results.
     */
    override fun getByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableByOpponents(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableByOpposition(
            context,
            MATCHCOUNTS_TEMP_TABLE,
            MATCHIDS_TEMP_TABLE
        )
        val resultsTable =
            JooqBowlingIndividualOpposition.createResultsCte(
                searchParameters = searchParameters,
                bowlTempName = BOWLING_TEMP_DETALS_TABLE,
                tempTeamsName = TEAMS_TEMP_TABLE,
                tempMatchCountsName = MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)

    }

    /**
     * Retrieves bowling statistics grouped by the individual years when matches started.
     * The results are filtered and generated based on the provided search parameters.
     *
     * @param searchParameters The validated parameters specifying filters such as match type, team ID, and date range.
     *                         These parameters dictate the scope and nature of the results returned.
     * @return A [DatabaseResult] containing a list of [BowlingPrimary] records and the total count of items.
     */
    override fun getByIndividualYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableByMatchStartYear(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableByYearOfMatchStart(
            context,
            MATCHCOUNTS_TEMP_TABLE,
            MATCHIDS_TEMP_TABLE
        )
        val resultsTable =
            JooqBowlingIndividualYearOfMatchStart.createResultsCte(
                searchParameters = searchParameters,
                bowlTempName = BOWLING_TEMP_DETALS_TABLE,
                tempTeamsName = TEAMS_TEMP_TABLE,
                tempMatchCountsName = MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Retrieves bowling statistics aggregated by individual seasons.
     *
     * This method utilizes various temporary tables to filter and process relevant data
     * based on the provided search parameters, and generates a result set containing bowling
     * data grouped by individual seasons.
     *
     * @param searchParameters The validated search parameters containing filters such as match type,
     * season, team ID, opponents, sorting preferences, and paging configurations.
     * @return A DatabaseResult containing a list of BowlingPrimary objects representing the
     * aggregated bowling statistics for individual seasons along with the total count of records.
     */
    override fun getByIndividualSeason(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableBySeason(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableBySeason(
            context,
            MATCHCOUNTS_TEMP_TABLE,
            MATCHIDS_TEMP_TABLE
        )
        val resultsTable =
            JooqBowlingIndividualSeason.createResultsCte(
                searchParameters = searchParameters,
                bowlTempName = BOWLING_TEMP_DETALS_TABLE,
                teamsTempName = TEAMS_TEMP_TABLE,
                tempMatchCountsName = MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Fetches bowling statistics for an individual player, presented on an innings-by-innings basis,
     * based on the provided validated search parameters.
     *
     * This method utilizes temporary tables and Common Table Expressions (CTEs) to facilitate
     * efficient querying and sorting of bowling records from the database. The results are ordered
     * by the specified sort parameters and mapped to domain objects before returning.
     *
     * @param searchParameters The validated parameters that define the criteria for fetching
     * bowling statistics, including match type, team, sort preferences, and paging information.
     * @return A DatabaseResult containing a list of BowlingInningsByInnings objects along with
     * the total count of the records matching the search criteria.
     */
    override fun getByIndividualInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)


        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )

        val resultsTable =
            JooqBowlingInningsByInnings.createResultsCte(searchParameters, BOWLING_TEMP_DETALS_TABLE)
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)

        // Define sorting based on parameters
        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification,
                field("MatchStartDateAsOffset"),
                field("inningsOrder"),
                field("wickets").desc(),
                field("runs"),
                field("position"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()


        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToInningsByInnings(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves bowling match totals for individual players based on the provided search parameters.
     *
     * @param searchParameters The validated parameters specifying the criteria for the search,
     *                          including match type, team ID, opponents, date range, sorting,
     *                          paging configurations, and other relevant filters.
     * @return A `DatabaseResult` containing a list of `BowlingInningsByInnings` objects, which
     *         represent the queried matches and their corresponding count.
     */
    override fun getByIndividualMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)


        JooqHelpers.createTemporaryBowlingDetailsTable(
            context,
            bowlingDetailsTempTableName = BOWLING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )

        val whereClause = field("SyntheticBestBowlingMatch").isNotNull
            .and(field("rn", Int::class.java).eq(1))
            .and(field("balls").gt(0))
            .and(field("wickets").ge(searchParameters.pagingParameters.limit))

        val resultsTable =
            JooqBowlingMatchTotals.createResultsCte(searchParameters, BOWLING_TEMP_DETALS_TABLE)
        val totalCountsTable =
            JooqHelpers.totalCountsCte(RESULTS_TABLE).where(whereClause)


        // Define sorting based on parameters
        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        // Execute the query with CTEs and fetch results
        // todo - what should the limit field be (see ksstats - JooqBowlingRecordsDao.kt)
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(
                select()
                    .from(totalCountsTable)
            )
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .where(whereClause)
            .orderBy(
                sortSpecification,
                field("MatchStartDateAsOffset"),
                field("inningsOrder"),
                field("wickets").desc(),
                field("runs"),
                field("position"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()


        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToInningsByInnings(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Generates and returns paginated and sorted results from a database query.
     *
     * This method utilizes common table expressions (CTEs) to perform database queries
     * based on the given search parameters, context, and the results table definition.
     * It applies sorting, applies pagination, and maps the database records into domain objects.
     *
     * @param searchParameters The validated search parameters for filtering, sorting, and paginating results.
     * @param context The DSL context used to execute the database query.
     * @param resultsTable The definition of the base query or table to fetch results from.
     * @return A DatabaseResult object containing the mapped domain objects and the total count of results.
     */
    private fun generateResults(
        searchParameters: ValidatedSearchParameters,
        context: DSLContext,
        resultsTable: SelectJoinStep<Record>
    ): DatabaseResult<BowlingPrimary> {
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)

        // Define sorting based on parameters
        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("$RESULTS_TABLE, $TOTAL_COUNTS_TABLE")
            .orderBy(
                sortSpecification,
                field("wickets").desc(),
                field("runs"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryBowling(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Maps a database record to a `BowlingPrimary` domain object.
     *
     * @param record The database record to be mapped.
     * @return A `BowlingPrimary` object containing data mapped from the given record.
     */
    private fun mapToPrimaryBowling(record: Record): BowlingPrimary {
        return BowlingPrimary(
            playerId = record.getValue("playerid", Int::class.java),
            name = record.getValue("FullName", String::class.java),
            sortNamePart = record.getValue("SortNamePart", String::class.java),
            debut = record.getValueOrNull("debut", String::class.java) ?: "",
            end = record.getValueOrNull("end", String::class.java) ?: "",
            team = record.getValue("teams", String::class.java),
            opponents = record.getValueOrNull("opponents", String::class.java),
            matches = record.getValue("matches", Int::class.java),
            innings = record.getValue("innings", Int::class.java),
            balls = record.getValue("balls", Int::class.java),
            maidens = record.getValue("maidens", Int::class.java),
            dots = record.getValue("dots", Int::class.java),
            runs = record.getValue("runs", Int::class.java),
            wickets = record.getValue("wickets", Int::class.java),
            ground = record.getValueOrNull("ground", String::class.java) ?: "",
            countryName = record.getValueOrNull("CountryName", String::class.java) ?: "",
            year = record.getValueOrNull("year", String::class.java) ?: "",
            avg = record.getValue("avg", Double::class.java),
            sr = record.getValue("sr", Double::class.java),
            bi = record.getValue("bi", Double::class.java),
            fours = record.getValue("fours", Int::class.java),
            sixes = record.getValue("sixes", Int::class.java),
            fiveFor = record.getValue("fivefor", Int::class.java),
            tenFor = record.getValue("tenfor", Int::class.java),
            bbi = record.getValue("bbi", Double::class.java),
            bbm = record.getValue("bbm", Double::class.java),
        )
    }

    /**
     * Maps a database record to a BowlingInningsByInnings object by extracting and converting
     * the required information from the provided record fields.
     *
     * @param record the database record containing data to be mapped into a BowlingInningsByInnings instance
     * @return a BowlingInningsByInnings object containing the mapped data from the input record
     */
    private fun mapToInningsByInnings(record: Record): BowlingInningsByInnings {
        return BowlingInningsByInnings(
            playerId = record.getValue("playerid", Int::class.java),
            matchId = record.getValue("matchId", String::class.java),
            fullName = record.getValue("FullName", String::class.java),
            sortNamePart = record.getValue("SortNamePart", String::class.java),
            team = record.getValue("teams", String::class.java),
            opponents = record.getValue("opponents", String::class.java),
            inningsNumber = record.getValue("inningsNumber", Int::class.java),
            ground = record.getValue("KnownAs", String::class.java),
            matchDate = record.getValue("MatchStartDate", String::class.java),
            playerBalls = record.getValue("Balls", Int::class.java),
            playerMaidens = record.getValueOrNull("Maidens", Int::class.java),
            playerDots = record.getValueOrNull("Dots", Int::class.java),
            playerRuns = record.getValue("Runs", Int::class.java),
            playerWickets = record.getValue("Wickets", Int::class.java),
            ballsPerOver = record.getValue("BallsPerOver", Int::class.java),
            econ = record.getValue("rpo", String::class.java)
        )
    }
}