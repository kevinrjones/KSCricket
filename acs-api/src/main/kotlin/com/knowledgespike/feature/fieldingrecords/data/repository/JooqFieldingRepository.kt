package com.knowledgespike.feature.fieldingrecords.data.repository

import com.knowledgespike.DIALECT
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.feature.bowlingrecords.data.repository.JooqBowlingRepository
import com.knowledgespike.feature.bowlingrecords.data.repository.JooqBowlingRepository.Companion
import com.knowledgespike.feature.fieldingrecords.domain.model.FieldingInningsByInnings
import com.knowledgespike.feature.fieldingrecords.domain.model.FieldingPrimary
import com.knowledgespike.feature.fieldingrecords.domain.repository.FieldingRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.JooqHelpers
import com.knowledgespike.plugins.DataSource
import org.jooq.DSLContext
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL.*

/**
 * JooqFieldingRepository is a repository implementation for retrieving fielding statistics
 * using JOOQ as the query generation and execution framework. It interacts with a `DataSource`
 * to manage database connections and execute queries based on validated search parameters.
 *
 * This repository provides multiple query methods to retrieve fielding statistics across
 * different criteria such as individual performance, opponents, host country, series-specific data,
 * grounds, match totals, and year or season filters.
 *
 * Core responsibilities:
 * - Fetch fielding-related data in primary and innings-by-innings formats.
 * - Map JOOQ query results to domain-specific data models like `FieldingPrimary` and `FieldingInningsByInnings`.
 * - Facilitate query customization by using validated search parameters as input for filtering, sorting,
 *   and paginating results.
 *
 * @param dataSource The data source used to manage database connections for executing queries.
 */
class JooqFieldingRepository(private val dataSource: DataSource) : FieldingRepository {

    /**
     * Companion object for the JooqFieldingRepository class. Contains constants for temporary
     * table names and other table references used in fielding-related database operations.
     */
    companion object {
        /**
         * Represents the name of the temporary database table used for storing
         * fielding details during query processing in the repository.
         *
         * This constant is utilized within the `JooqFieldingRepository` to handle
         * intermediate fielding data, especially when building complex queries or
         * temporary result sets.
         */
        private const val FIELDING_TEMP_DETALS_TABLE = "tmp_field_details"

        /**
         * Represents the name of the temporary database table used to store aggregated innings count data.
         * This constant is utilized within SQL queries to reference the temporary table during data processing
         * in the JooqFieldingRepository implementation.
         */
        private const val INNINGS_COUNT_TABLE = "tmp_innings_count"

        /**
         * Represents the temporary database table name used to store the best fielding statistics data.
         * This table is typically employed during processing or querying operations
         * in the JooqFieldingRepository class for intermediate results before final output.
         */
        private const val BEST_FIELDING_TABLE = "tmp_best_fielding"

        /**
         * Name of the temporary table used for storing team-related data during query execution.
         * This table is utilized in various fielding-related data retrieval operations.
         */
        private const val TEAMS_TEMP_TABLE = "tmp_teams"

        /**
         * Constant representing the name of the database table used to store and query results data.
         *
         * This table is utilized by various methods in the JooqFieldingRepository class to retrieve
         * fielding statistics based on different search parameters, including individual performance,
         * series performance, host country, opponents, and other criteria.
         */
        private const val RESULTS_TABLE = "results"

        /**
         * Represents the database table name used for storing or querying total count data.
         * This constant is primarily used in database operations within the JooqFieldingRepository class.
         */
        private const val TOTAL_COUNTS_TABLE = "total_counts"

        /**
         * Represents the name of a temporary database table used for storing match IDs during query processing.
         * This table is typically utilized in intermediate operations within database queries
         * for fielding-related data retrieval in the repository.
         */
        private const val MATCHIDS_TEMP_TABLE = "tmp_match_ids_table"

        /**
         * Represents the name of a temporary database table used for storing match counts during
         * specific query operations within the `JooqFieldingRepository` class methods.
         * This table is utilized internally to manage and process fielding-related search parameters
         * and results efficiently.
         */
        private const val MATCHCOUNTS_TEMP_TABLE = "tmp_match_counts_table"
    }

    /**
     * Retrieves overall fielding statistics for an individual based on the given search parameters.
     * This method makes use of temporary tables, common table expressions (CTEs), and queries
     * to compute the required fielding statistics for the specified individual.
     *
     * @param searchParameters The validated search parameters containing criteria such as match type,
     *                          date ranges, and other filtering conditions required for the query.
     * @return A DatabaseResult containing the resulting fielding statistics encapsulated in a FieldingPrimary object.
     */
    override fun getByIndividualOverallFielding(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary> {
        // Set up the database context
        val context = using(dataSource.dataSource, DIALECT)

        // Prepare Common Table Expressions (CTEs) for the query
        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
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

        JooqFieldingCareerRecords.createInningsCountCte(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            inningsCountTableName = INNINGS_COUNT_TABLE,
            matchType = searchParameters.matchType
        )

        JooqFieldingCareerRecords.createTemporaryFieldingBest(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            bestFieldingTableName = BEST_FIELDING_TABLE
        )

        val resultsTable =
            JooqFieldingCareerRecords.createResultsCte(
                searchParameters,
                FIELDING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE,
                INNINGS_COUNT_TABLE,
                BEST_FIELDING_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }


    /**
     * Retrieves fielding statistics for an individual series based on the given search parameters.
     *
     * This method uses multiple temporary tables to process and compute the required data, such as match IDs,
     * fielding details, teams information, match counts, innings counts, and best fielding performances.
     *
     * @param searchParameters Validated search parameters containing details such as match type and team ID
     *                         for filtering and retrieving the results.
     * @return A DatabaseResult object containing the processed fielding statistics for the specified individual series.
     */
    override fun getByIndividualSeries(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableBySeries(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableBySeries(context, MATCHCOUNTS_TEMP_TABLE, MATCHIDS_TEMP_TABLE)

        JooqFieldingIndividualSeries.createInningsCountCte(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            inningsCountTableName = INNINGS_COUNT_TABLE,
            matchType = searchParameters.matchType
        )

        JooqFieldingIndividualSeries.createTemporaryFieldingBest(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            bestFieldingTableName = BEST_FIELDING_TABLE
        )

        val resultsTable =
            JooqFieldingIndividualSeries.createResultsCte(
                searchParameters,
                FIELDING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE,
                INNINGS_COUNT_TABLE,
                BEST_FIELDING_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Retrieves fielding performance data based on the host country's specifications.
     *
     * The method implements various operations using temporary tables and Common Table Expressions (CTEs)
     * to filter and aggregate relevant fielding data. It considers details such as match type, team ID,
     * and innings counts, and generates a comprehensive result based on these constraints.
     *
     * @param searchParameters An instance of ValidatedSearchParameters that contains the search criteria,
     * including match type, team ID, and other filters relevant to the host country's fielding data.
     * @return A DatabaseResult containing fielding performance details encapsulated in FieldingPrimary
     * objects, based on the provided search parameters.
     */
    override fun getByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableByHostCountry(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableByHostCountry(context, MATCHCOUNTS_TEMP_TABLE, MATCHIDS_TEMP_TABLE)

        JooqFieldingIndividualHostCountry.createInningsCountCte(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            inningsCountTableName = INNINGS_COUNT_TABLE,
            matchType = searchParameters.matchType
        )

        JooqFieldingIndividualHostCountry.createTemporaryFieldingBest(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            bestFieldingTableName = BEST_FIELDING_TABLE
        )

        val resultsTable =
            JooqFieldingIndividualHostCountry.createResultsCte(
                searchParameters,
                FIELDING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE,
                INNINGS_COUNT_TABLE,
                BEST_FIELDING_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }


    /**
     * Retrieves fielding statistics based on the specified opponents and search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing search criteria such
     * as match type, team ID, and other filters to query the data.
     * @return A DatabaseResult containing a list of `FieldingPrimary` objects filtered based on the search parameters.
     */
    override fun getByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableByOpponents(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableByOpposition(context, MATCHCOUNTS_TEMP_TABLE, MATCHIDS_TEMP_TABLE)

        JooqFieldingIndividualOpposition.createInningsCountCte(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            inningsCountTableName = INNINGS_COUNT_TABLE,
            matchType = searchParameters.matchType
        )

        JooqFieldingIndividualOpposition.createTemporaryFieldingBest(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            bestFieldingTableName = BEST_FIELDING_TABLE
        )

        val resultsTable =
            JooqFieldingIndividualOpposition.createResultsCte(
                searchParameters,
                FIELDING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE,
                INNINGS_COUNT_TABLE,
                BEST_FIELDING_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Retrieves fielding statistics for an individual player on an innings-by-innings basis
     * based on the provided search parameters.
     *
     * The method processes search parameters to create temporary database tables for efficient query execution.
     * It constructs and executes a query with common table expressions (CTEs) for results and total counts,
     * applies sorting and pagination, and maps the query results into `FieldingInningsByInnings` domain objects.
     *
     * @param searchParameters The validated search parameters containing filtering, sorting, and pagination criteria.
     * @return A `DatabaseResult` containing a list of `FieldingInningsByInnings` objects and a total count of results.
     */
    override fun getByIndividualInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)


        JooqHelpers.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )

        val resultsTable =
            JooqFieldingInningsByInnings.createResultsCte(searchParameters, FIELDING_TEMP_DETALS_TABLE)
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
            .orderBy(sortSpecification, field("matchid"))
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
     * Retrieves fielding statistics by individual match totals based on the provided search parameters.
     *
     * This method utilizes temporary tables and Common Table Expressions (CTEs) to process
     * and query the data efficiently, applying filters, sorting, and pagination configurations
     * as defined in the search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters that includes the criteria
     * such as match type, date range, sorting preferences, and paging configurations for filtering
     * and retrieving fielding statistics.
     *
     * @return A DatabaseResult object containing a list of FieldingInningsByInnings domain objects
     * and the total count of matching results.
     */
    override fun getByIndividualMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)


        JooqFieldingMatchTotals.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE
        )

        val resultsTable =
            JooqFieldingMatchTotals.createResultsCte(searchParameters, FIELDING_TEMP_DETALS_TABLE)
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
            .orderBy(sortSpecification, field("matchid"))
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
     * Retrieves fielding data based on the individual year of match start.
     *
     * @param searchParameters Contains validated search parameters, including match types, team ID,
     * and other filtering criteria for generating the fielding statistics.
     * @return A DatabaseResult containing a collection of FieldingPrimary objects that match the given criteria.
     */
    override fun getByIndividualYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
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

        JooqFieldingIndividualYearOfMatchStart.createInningsCountCte(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            inningsCountTableName = INNINGS_COUNT_TABLE,
            matchType = searchParameters.matchType
        )

        JooqFieldingIndividualYearOfMatchStart.createTemporaryFieldingBest(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            bestFieldingTableName = BEST_FIELDING_TABLE
        )

        val resultsTable =
            JooqFieldingIndividualYearOfMatchStart.createResultsCte(
                searchParameters,
                FIELDING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE,
                INNINGS_COUNT_TABLE,
                BEST_FIELDING_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Retrieves fielding primary statistics based on the provided validated search parameters for a specific individual season.
     *
     * This method utilizes a series of temporary tables to filter and calculate the results based on the search parameters, including details like match type, team, and innings count
     * .
     * The method fetches results from a sequence of calculations and the composed results table.
     *
     * @param searchParameters the validated search parameters containing filters such as match type, season, and team identifiers.
     * @return the results of the query in the form of a `DatabaseResult<FieldingPrimary>` that contains the calculated fielding statistics for the individual season matching the
     *  search parameters.
     */
    override fun getByIndividualSeason(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableBySeason(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableBySeason(context, MATCHCOUNTS_TEMP_TABLE, MATCHIDS_TEMP_TABLE)

        JooqFieldingIndividualSeason.createInningsCountCte(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            inningsCountTableName = INNINGS_COUNT_TABLE,
            matchType = searchParameters.matchType
        )

        JooqFieldingIndividualSeason.createTemporaryFieldingBest(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            bestFieldingTableName = BEST_FIELDING_TABLE
        )

        val resultsTable =
            JooqFieldingIndividualSeason.createResultsCte(
                searchParameters,
                FIELDING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE,
                INNINGS_COUNT_TABLE,
                BEST_FIELDING_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Retrieves fielding performance details by individual grounds based on the provided search parameters.
     * The method utilizes temporary tables and performs various operations through JOOQ to filter,
     * process and aggregate data as per the specified parameters.
     *
     * @param searchParameters Validated search parameters that contain filtering criteria such as match type
     *                         and team ID. These parameters are used to generate the necessary queries and tables.
     * @return Returns a DatabaseResult containing a collection of FieldingPrimary objects, which represent
     *         the fielding details aggregated and processed by the grounds specified in the search parameters.
     */
    override fun getByIndividualGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryFieldingDetailsTable(
            context,
            fieldingDetailsTempTableName = FIELDING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )
        JooqHelpers.createTemporaryTeamsTableByGround(
            context,
            TEAMS_TEMP_TABLE,
            searchParameters.matchType,
            searchParameters.teamId
        )
        JooqHelpers.createTemporaryMatchCountsTableByGround(context, MATCHCOUNTS_TEMP_TABLE, MATCHIDS_TEMP_TABLE)

        JooqFieldingIndividualGrounds.createInningsCountCte(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            inningsCountTableName = INNINGS_COUNT_TABLE,
            matchType = searchParameters.matchType
        )

        JooqFieldingIndividualGrounds.createTemporaryFieldingBest(
            context = context,
            fieldingTableName = FIELDING_TEMP_DETALS_TABLE,
            bestFieldingTableName = BEST_FIELDING_TABLE
        )

        val resultsTable =
            JooqFieldingIndividualGrounds.createResultsCte(
                searchParameters,
                FIELDING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE,
                INNINGS_COUNT_TABLE,
                BEST_FIELDING_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Generates the query results based on the provided search parameters and context.
     *
     * @param searchParameters The validated search parameters that include paging and sorting options.
     * @param context The DSLContext used to execute the database query.
     * @param resultsTable The intermediate step of the SQL query from which results are fetched.
     * @return A DatabaseResult object containing the list of mapped results and the total count.
     */
    private fun generateResults(
        searchParameters: ValidatedSearchParameters,
        context: DSLContext,
        resultsTable: SelectJoinStep<*>
    ): DatabaseResult<FieldingPrimary> {
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
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryFielding(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Maps a JOOQ record to a FieldingPrimary object.
     *
     * @param record The JOOQ record containing fielding data.
     * @return A FieldingPrimary object populated with data from the record.
     */
    private fun mapToPrimaryFielding(record: org.jooq.Record): FieldingPrimary {
        return FieldingPrimary(
            playerId = record.getValue("playerid", Int::class.java),
            name = record.getValue("name", String::class.java),
            debut = record.getValueOrNull("debut", String::class.java) ?: "",
            end = record.getValueOrNull("end", String::class.java) ?: "",
            team = record.getValue("teams", String::class.java),
            opponents = record.getValueOrNull("opponents", String::class.java) ?: "",
            year = record.getValueOrNull("year", String::class.java) ?: "",
            matches = record.getValue("matches", Int::class.java),
            innings = record.getValue("innings", Int::class.java),
            ground = record.getValueOrNull("ground", String::class.java) ?: "",
            countryName = record.getValueOrNull("CountryName", String::class.java) ?: "",
            dismissals = record.getValue("dismissals", Int::class.java),
            wicketKeeperDismissals = record.getValue("wicketKeeperDismissals", Int::class.java),
            caughtKeeper = record.getValue("caughtKeeper", Int::class.java),
            caughtFielder = record.getValue("caughtFielder", Int::class.java),
            caught = record.getValue("caught", Int::class.java),
            stumpings = record.getValue("stumpings", Int::class.java),
            bestDismissals = record.getValue("bestDismissals", Int::class.java),
            bestCaughtKeeper = record.getValue("bestCaughtKeeper", Int::class.java),
            bestCaughtFielder = record.getValue("bestCaughtFielder", Int::class.java),
            bestStumpings = record.getValue("bestStumpings", Int::class.java),
        )
    }

    /**
     * Maps a jOOQ record to a `FieldingInningsByInnings` data object.
     *
     * @param record the jOOQ record containing fielding data to map
     * @return a `FieldingInningsByInnings` object built from the provided record
     */
    private fun mapToInningsByInnings(record: org.jooq.Record): FieldingInningsByInnings {
        return FieldingInningsByInnings(
            playerId = record.getValue("playerid", Int::class.java),
            matchId = record.getValue("matchId", String::class.java),
            fullName = record.getValue("FullName", String::class.java),
            inningsNumber = record.getValueOrNull("inningsOrder", Int::class.java) ?: 0,
            team = record.getValue("teams", String::class.java),
            matchDate = record.getValue("MatchStartDate", String::class.java),
            ground = record.getValue("KnownAs", String::class.java),
            opponents = record.getValue("opponents", String::class.java),
            caughtKeeper = record.getValue("caughtkeeper", Int::class.java),
            caughtFielder = record.getValue("caughtfielder", Int::class.java),
            caught = record.getValue("caught", Int::class.java),
            wicketKeepingDismissals = record.getValue("wicketKeeperDismissals", Int::class.java),
            stumpings = record.getValue("stumpings", Int::class.java),
            dismissals = record.getValue("dismissals", Int::class.java),
        )
    }
}