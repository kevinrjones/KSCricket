package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.DIALECT
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.feature.scorecard.domain.model.VictoryType
import com.knowledgespike.feature.teamrecords.domain.model.*
import com.knowledgespike.feature.teamrecords.domain.repository.TeamRepository
import com.knowledgespike.jooq.JooqHelpers
import com.knowledgespike.plugins.DataSource
import org.jooq.impl.DSL.*

/**
 * Jooq-based implementation of the TeamRepository interface.
 *
 * This class provides methods to execute database queries for retrieving various team-related
 * statistics based on a set of validated search parameters. The results of these queries are
 * wrapped in DatabaseResult objects containing the relevant data types.
 *
 * @constructor Creates an instance of JooqTeamRepository with the provided DataSource.
 *
 * @property dataSource The DataSource used to manage connections to the database.
 */
class JooqTeamRepository(private val dataSource: DataSource) : TeamRepository {

    /**
     * Companion object containing constants and default values used in the JooqTeamRepository class.
     */
    companion object {
        /**
         * Represents the name of the temporary table used to store team scores
         * during query execution within the `JooqTeamRepository`.
         *
         * This constant is utilized in building query structures and mapping
         * operations that involve temporary storage of team-related scoring data.
         */
        private const val TEAM_TEMP_SCORES_TABLE = "tmp_team_scores"

        /**
         * Represents the temporary database table name used for storing team totals in intermediate calculations
         * within the Jooq-based team repository layer.
         */
        private const val TEAM_TEMP_TOTALS_TABLE = "tmp_team_totals"

        /**
         * Represents the name of the database table used to store or query results data.
         * This constant is used within the `JooqTeamRepository` class for queries
         * and database operations involving the "results" table.
         */
        private const val RESULTS_TABLE = "results"

        /**
         * Represents the name of the database table storing total counts information.
         *
         * This constant is used within the JooqTeamRepository class for database operations
         * involving records in the "total_counts" table. It simplifies table reference in
         * SQL queries and ensures consistency across the repository's methods.
         */
        private const val TOTAL_COUNTS_TABLE = "total_counts"

    }

    /**
     * Retrieves a summary of team statistics based on the given search parameters.
     *
     * This method leverages temporary database tables and SQL Common Table Expressions (CTEs)
     * to calculate and fetch the required team statistics based on the input criteria.
     *
     * @param searchParameters The validated search criteria, containing details like match type, team identifiers,
     *                         sorting preferences, and paging configurations.
     * @return A `DatabaseResult` containing a list of `PrimaryTeam` entries representing the requested summary
     *         along with the total count of results.
     */
    override fun getByTeamSummary(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqTeamSummary.createTemporaryScores(context, tempTableName = TEAM_TEMP_SCORES_TABLE, searchParameters)
        JooqTeamSummary.createTotalsTable(
            context,
            searchParameters.isTeamBattingRecord,
            tempTableName = TEAM_TEMP_TOTALS_TABLE,
            scoresTempTableName = TEAM_TEMP_SCORES_TABLE
        )

        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamSummary.createResultsCte(
                scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
                totalTableName = TEAM_TEMP_TOTALS_TABLE,
                minimumValue = searchParameters.pagingParameters.limit,
                isForBattingTeam = searchParameters.isTeamBattingRecord,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(sortSpecification, field("Team"))
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryTeam(it)
        }

        return DatabaseResult(results, count)

    }

    /**
     * Retrieves a list of primary teams and their associated data based on the specified search parameters,
     * applying sorting and pagination criteria.
     *
     * @param searchParameters An object containing validated search criteria, including filtering options,
     *                         sorting preferences, and pagination settings.
     * @return A DatabaseResult object containing the list of primary teams matching the search criteria
     *         and the total count of results.
     */
    override fun getTeamBySeries(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqTeamBySeries.createTemporaryScores(context, tempTableName = TEAM_TEMP_SCORES_TABLE, searchParameters)
        JooqTeamBySeries.createTotalsTable(
            context,
            searchParameters.isTeamBattingRecord,
            tempTableName = TEAM_TEMP_TOTALS_TABLE,
            scoresTempTableName = TEAM_TEMP_SCORES_TABLE
        )

        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamBySeries.createResultsCte(
                scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
                totalTableName = TEAM_TEMP_TOTALS_TABLE,
                minimumValue = searchParameters.pagingParameters.limit,
                isForBattingTeam = searchParameters.isTeamBattingRecord,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("SeriesDate"), field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryTeam(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves the team statistics based on the specified search parameters for grounds.
     *
     * @param searchParameters The search parameters used to filter and sort the team statistics. This includes details
     * such as match type, ground, date range, sorting preference, and paging configuration.
     * @return A `DatabaseResult` object containing a list of `PrimaryTeam` items matching the query and the total count
     * of results.
     */
    override fun getTeamByGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqTeamByGround.createTemporaryScores(context, tempTableName = TEAM_TEMP_SCORES_TABLE, searchParameters)
        JooqTeamByGround.createTotalsTable(
            context,
            tempTableName = TEAM_TEMP_TOTALS_TABLE,
            scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
            searchParameters
        )

        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamByGround.createResultsCte(
                scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
                totalTableName = TEAM_TEMP_TOTALS_TABLE,
                minimumValue = searchParameters.pagingParameters.limit,
                isForBattingTeam = searchParameters.isTeamBattingRecord,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryTeam(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves team statistics filtered by the host country and other search parameters.
     *
     * This method leverages temporary tables to calculate scores and totals, applies the search and sorting criteria,
     * and retrieves the corresponding team data from the database.
     *
     * @param searchParameters The validated search parameters that define the filter, sort, and paging configurations
     *                          for the query.
     * @return A `DatabaseResult` containing the list of teams matched by the query and the total count of results.
     */
    override fun getTeamByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqTeamByHostCountry.createTemporaryScores(context, tempTableName = TEAM_TEMP_SCORES_TABLE, searchParameters)
        JooqTeamByHostCountry.createTotalsTable(
            context,
            tempTableName = TEAM_TEMP_TOTALS_TABLE,
            scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
            searchParameters
        )

        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamByHostCountry.createResultsCte(
                scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
                totalTableName = TEAM_TEMP_TOTALS_TABLE,
                minimumValue = searchParameters.pagingParameters.limit,
                isForBattingTeam = searchParameters.isTeamBattingRecord,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryTeam(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves a team's statistics based on their performance against opponents, considering the specified search parameters.
     *
     * @param searchParameters Validated search parameters that define the criteria for filtering and sorting team statistics.
     *                         Includes details like match type, opponents, date range, sorting preferences, and paging configuration.
     * @return A DatabaseResult containing a list of PrimaryTeam objects that match the search criteria, along with the total count.
     */
    override fun getTeamByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqTeamByOpponents.createTemporaryScores(context, tempTableName = TEAM_TEMP_SCORES_TABLE, searchParameters)
        JooqTeamByOpponents.createTotalsTable(
            context,
            searchParameters.isTeamBattingRecord,
            tempTableName = TEAM_TEMP_TOTALS_TABLE,
            scoresTempTableName = TEAM_TEMP_SCORES_TABLE
        )

        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamByOpponents.createResultsCte(
                scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
                totalTableName = TEAM_TEMP_TOTALS_TABLE,
                minimumValue = searchParameters.pagingParameters.limit,
                isForBattingTeam = searchParameters.isTeamBattingRecord,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryTeam(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves team innings statistics based on the provided search parameters.
     *
     * The method executes a database query to fetch innings-by-innings data for a specific team,
     * using the given search parameters which include filters such as match type, team ID, opponents,
     * date range, sorting order, and paging configuration. The result includes detailed innings statistics
     * like total runs, wickets, run rate, match details, and more.
     *
     * @param searchParameters The validated search parameters that define the query criteria, including
     *                         filters for matches, teams, venues, sorting preferences, and paging configurations.
     * @return A `DatabaseResult` containing a list of `InningsByInningsTeam` objects representing the
     *         retrieved innings-by-innings statistics, and the total count of results matching the query.
     */
    override fun getTeamInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)


        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamInningsByInnings.createResultsCte(
                searchParameters = searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("MatchId"), field("Innings"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToInningsByInnings(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves the match totals for a team based on the provided validated search parameters.
     *
     * This method queries the database for innings-by-innings statistics of a team, applies sorting,
     * and returns a paged result set along with the total count. The results include details such as
     * the team, opponents, innings, total runs, run rate, match result, location, date, wickets, and
     * overs. It uses common table expressions (CTEs) for efficient querying and calculates the count
     * metadata alongside the main query.
     *
     * @param searchParameters The validated search parameters specifying filters such as team, opponents,
     * match type, venue, date ranges, sorting preferences, and pagination details.
     * @return A `DatabaseResult` object that contains a list of `InningsByInningsTeam` representing the
     * team match totals and the total count of records.
     */
    override fun getTeamMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)


        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamMatchTotals.createResultsCte(
                searchParameters = searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToInningsByInnings(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves team match results based on the provided search parameters.
     *
     * The method executes a query against the database to fetch match results, including details such as
     * team, opponents, match victory type, location, start date, and other necessary metadata. The results
     * are sorted and paginated according to the input parameters.
     *
     * @param searchParameters The validated search parameters containing filtering, sorting, and pagination details.
     * @return A DatabaseResult object containing a list of MatchResultTeam items and the total count of results.
     */
    override fun getTeamMatchResults(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamMatchResult> {
        val context = using(dataSource.dataSource, DIALECT)


        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamMatchResults.createResultsCte(
                searchParameters = searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToMatchResult(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves team data based on the year of the match start, according to the given search parameters.
     *
     * This method processes temporary score and total tables, applies sorting and paging configurations,
     * and executes the query with the specified parameters to fetch the final results.
     *
     * @param searchParameters The validated search parameters containing match details, sorting preferences,
     *                         and paging configurations to filter the query.
     * @return A `DatabaseResult` containing a list of `PrimaryTeam` objects and the total count of results.
     */
    override fun getTeamByYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqTeamByYearOfMatchStart.createTemporaryScores(
            context,
            tempTableName = TEAM_TEMP_SCORES_TABLE,
            searchParameters
        )
        JooqTeamByYearOfMatchStart.createTotalsTable(
            context,
            tempTableName = TEAM_TEMP_TOTALS_TABLE,
            scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
            searchParameters
        )

        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamByYearOfMatchStart.createResultsCte(
                scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
                totalTableName = TEAM_TEMP_TOTALS_TABLE,
                minimumValue = searchParameters.pagingParameters.limit,
                isForBattingTeam = searchParameters.isTeamBattingRecord,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryTeam(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves team statistics for a specific season based on the provided search parameters.
     *
     * This method executes a series of database queries to fetch and process team data,
     * including creating temporary tables, computing totals, and applying sorting and pagination.
     *
     * @param searchParameters The validated search parameters used to filter and organize the query output,
     * including season, sorting criteria, and pagination configuration.
     * @return A `DatabaseResult` containing a list of `PrimaryTeam` entities that match the criteria
     * and the total count of such entities.
     */
    override fun getTeamBySeason(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqTeamBySeason.createTemporaryScores(context, tempTableName = TEAM_TEMP_SCORES_TABLE, searchParameters)
        JooqTeamBySeason.createTotalsTable(
            context,
            tempTableName = TEAM_TEMP_TOTALS_TABLE,
            scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
            searchParameters
        )

        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamBySeason.createResultsCte(
                scoresTempTableName = TEAM_TEMP_SCORES_TABLE,
                totalTableName = TEAM_TEMP_TOTALS_TABLE,
                minimumValue = searchParameters.pagingParameters.limit,
                isForBattingTeam = searchParameters.isTeamBattingRecord,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryTeam(it)
        }

        return DatabaseResult(results, count)
    }

    override fun getTeamOverallExtras(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamExtrasOverall> {
        val context = using(dataSource.dataSource, DIALECT)


        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamExtrasOverall.createResultsCte(
                searchParameters = searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToTeamOverallExtras(it)
        }

        return DatabaseResult(results, count)
    }

    override fun getTeamExtrasByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamExtrasInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)


        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(searchParameters.sortOrder.name).asc()
            SortDirection.DESC -> field(searchParameters.sortOrder.name).desc()
        }

        val resultsTable =
            JooqTeamExtrasInningsByInnings.createResultsCte(
                searchParameters = searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                sortSpecification, field("Team"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToTeamInningsByInningsExtras(it)
        }

        return DatabaseResult(results, count)
    }

    override fun getTeamHighestTotalChased(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamTargetDetailsDto> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqTeamHighestTargetChased.createResultsCte(
                searchParameters = searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                field("Target").desc(),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToTeamTargetDetails(it)
        }

        return DatabaseResult(results, count)
    }

    override fun getTeamLowestTargetDefended(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamTargetDetailsDto> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqTeamLowestTargetSuccessfullyDefended.createResultsCte(
                searchParameters = searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                field("Target").desc(),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToTeamTargetDetails(it)
        }

        return DatabaseResult(results, count)
    }

    override fun getTeamLowestTargetDefendedInUnreducedMatch(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamTargetDetailsDto> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqTeamLowestTargetSuccessfullyDefendedInUnreducedMatch.createResultsCte(
                searchParameters = searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(
                field("target"),
            )
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToTeamTargetDetails(it)
        }

        return DatabaseResult(results, count)

    }

    /**
     * Maps a jOOQ Record to a PrimaryTeam object by extracting specific fields.
     *
     * @param record the jOOQ Record that contains team-related data to be transformed into a PrimaryTeam object.
     * @return a PrimaryTeam object populated with values from the provided jOOQ Record.
     */
    private fun mapToPrimaryTeam(record: org.jooq.Record): TeamPrimary {
        return TeamPrimary(
            team = record.getValue("team", String::class.java),
            opponents = record.getValueOrNull("opponents", String::class.java) ?: "",
            played = record.getValue("matches", Int::class.java),
            won = record.getValue("won", Int::class.java),
            drawn = record.getValue("drawn", Int::class.java),
            lost = record.getValue("lost", Int::class.java),
            tied = record.getValue("tied", Int::class.java),
            innings = record.getValueOrNull("innings", Int::class.java) ?: 0,
            totalRuns = record.getValue("Runs", Int::class.java),
            wickets = record.getValue("wickets", Int::class.java),
            totalBalls = record.getValue("totalBalls", Int::class.java),
            hs = record.getValue("hs", Int::class.java),
            ls = record.getValue("ls", Int::class.java),
            avg = record.getValue("avg", Double::class.java),
            rpo = record.getValue("rpo", Double::class.java),
            sr = record.getValue("sr", Double::class.java),
            seriesDate = record.getValueOrNull("seriesDate", String::class.java) ?: "",
            matchStartYear = record.getValueOrNull("matchStartYear", String::class.java) ?: "",
            ground = record.getValueOrNull("ground", String::class.java) ?: "",
            countryName = record.getValueOrNull("CountryName", String::class.java) ?: "",
        )
    }

    /**
     * Maps a database record to an InningsByInningsTeam object.
     *
     * @param record the jOOQ record containing data to be mapped
     * @return an instance of InningsByInningsTeam populated with data from the record
     */
    private fun mapToInningsByInnings(record: org.jooq.Record): TeamInningsByInnings {
        return TeamInningsByInnings(
            matchId = record.getValue("matchId", String::class.java),
            caId = record.getValue("CaId", String::class.java),
            team = record.getValue("team", String::class.java),
            opponents = record.getValue("opponents", String::class.java),
            innings = record.getValue("Innings", Int::class.java),
            totalRuns = record.getValue("Runs", Int::class.java),
            ballsBowled = record.getValue("BallsBowled", Int::class.java),
            ballsPerOver = record.getValue("BallsPerOver", Int::class.java),
            rpo = record.getValue("rpo", Double::class.java),
            resultString = record.getValue("ResultString", String::class.java),
            ground = record.getValue("ground", String::class.java),
            matchDate = record.getValue("MatchDate", String::class.java),
            totalWickets = record.getValue("Wickets", Int::class.java),
            allOut = record.getValue("AllOut", Boolean::class.java),
            declared = record.getValue("Declared", Boolean::class.java),
        )
    }

    /**
     * Maps a database record into a `MatchResultTeam` model object.
     *
     * @param record the database record to map
     * @return a `MatchResultTeam` object containing the mapped data from the record
     */
    private fun mapToMatchResult(record: org.jooq.Record): TeamMatchResult {
        return TeamMatchResult(
            matchId = record.getValue("matchId", String::class.java),
            team = record.getValue("team", String::class.java),
            opponents = record.getValue("opponents", String::class.java),
            victoryType = VictoryType.entries[record.getValue("VictoryType", Int::class.java)],
            howMuch = record.getValue("HowMuch", Int::class.java),
            ground = record.getValue("KnownAs", String::class.java),
            matchStartDate = record.getValue("MatchDate", String::class.java),
            resultString = record.getValue("ResultString", String::class.java),
            teamId = record.getValue("teamid", Int::class.java),
            opponentsId = record.getValue("opponentsid", Int::class.java),
            whoWonId = record.getValue("WhoWonId", Int::class.java),
            teamTossId = record.getValue("TossTeamId", Int::class.java),
        )
    }

    private fun mapToTeamOverallExtras(record: org.jooq.Record): TeamExtrasOverall {
        return TeamExtrasOverall(
            team = record.getValue("team", String::class.java),
            played = record.getValue("played", Int::class.java),
            runs = record.getValue("runs", Int::class.java),
            extras = record.getValue("extras", Int::class.java),
            byes = record.getValue("byes", Int::class.java),
            legByes = record.getValue("legByes", Int::class.java),
            wides = record.getValue("wides", Int::class.java),
            noBalls = record.getValue("noBalls", Int::class.java),
            penalties = record.getValue("penalties", Int::class.java),
            balls = record.getValue("balls", Int::class.java),
            wickets = record.getValue("wickets", Int::class.java),
            percentage = record.getValue("percentage", Double::class.java),
        )
    }

    private fun mapToTeamInningsByInningsExtras(record: org.jooq.Record): TeamExtrasInningsByInnings {
        return TeamExtrasInningsByInnings(
            matchId = record.getValue("MatchId", String::class.java),
            team = record.getValue("team", String::class.java),
            extras = record.getValue("Extras", Int::class.java),
            byes = record.getValue("Byes", Int::class.java),
            legByes = record.getValue("LegByes", Int::class.java),
            wides = record.getValue("Wides", Int::class.java),
            noBalls = record.getValue("Noballs", Int::class.java),
            penalties = record.getValue("penalties", Int::class.java),
            total = record.getValue("Total", Int::class.java),
            wickets = record.getValue("Wickets", Int::class.java),
            overs = record.getValue("Overs", String::class.java),
            ground = record.getValue("KnownAs", String::class.java),
            opponents = record.getValue("opponents", String::class.java),
            matchStartDate = record.getValue("MatchStartDate", String::class.java),
            percentage = record.getValueOrNull("percentage", Double::class.java),
        )
    }

    private fun mapToTeamTargetDetails(record: org.jooq.Record): TeamTargetDetailsDto {
        return TeamTargetDetailsDto(
            matchId = record.getValue("matchid", String::class.java),
            winningTeam = record.getValue("WinningTeam", String::class.java),
            losingTeam = record.getValue("LosingTeam", String::class.java),
            matchTitle = record.getValue("MatchTitle", String::class.java),
            matchDate = record.getValue("MatchDate", String::class.java),
            seriesDate = record.getValue("SeriesDate", String::class.java),
            ground = record.getValue("ground", String::class.java),
            resultString = record.getValue("ResultString", String::class.java),
            target = record.getValue("target", Int::class.java),
        )
    }
}