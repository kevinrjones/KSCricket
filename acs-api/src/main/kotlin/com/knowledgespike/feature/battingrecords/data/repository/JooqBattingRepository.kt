package com.knowledgespike.feature.battingrecords.data.repository

import com.knowledgespike.DIALECT
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.feature.battingrecords.domain.model.BattingInningsByInnings
import com.knowledgespike.feature.battingrecords.domain.model.BattingPrimary
import com.knowledgespike.feature.battingrecords.domain.repository.BattingRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.JooqHelpers
import com.knowledgespike.plugins.DataSource
import org.jooq.DSLContext
import org.jooq.Record22
import org.jooq.SelectConditionStep
import org.jooq.SelectConnectByStep
import org.jooq.impl.DSL.*
import java.time.LocalDate

/**
 * Repository class for fetching and managing batting-related data from the database.
 * The repository leverages jOOQ for constructing and executing database queries,
 * providing a rich set of functionalities for querying individual and overall batting statistics.
 *
 * This class includes multiple public methods to fetch batting statistics based on various
 * criteria such as overall performance, series-specific data, venue-specific data,
 * opponent-based stats, yearly performance, and season-related information. Additionally,
 * it supports querying detailed match-specific and innings-by-innings statistics.
 *
 * @property dataSource The data source used for establishing connections to the database.
 */
class JooqBattingRepository(private val dataSource: DataSource) : BattingRepository {

    companion object {
        private const val BATTING_TEMP_DETALS_TABLE = "tmp_bat_details"
        private const val TEAMS_TEMP_TABLE = "tmp_teams"
        private const val RESULTS_TABLE = "results"
        private const val TOTAL_COUNTS_TABLE = "total_counts"
        private const val MATCHIDS_TEMP_TABLE = "tmp_match_ids_table"
        private const val MATCHCOUNTS_TEMP_TABLE = "tmp_match_counts_table"
    }

    /**
     * Fetches the overall batting statistics based on the provided validated search parameters.
     *
     * The method constructs a complex database query using Common Table Expressions (CTEs)
     * to apply filters, aggregate statistics, sort results, and provide pagination as per
     * the specified parameters.
     *
     * @param searchParameters The validated search parameters that include match types, teams,
     * opponents, venue, date range, sorting criteria, and paging configurations.
     * @return A `DatabaseResult` containing a list of `PrimaryBatting` objects that match
     * the search parameters, along with the total count of items in the result.
     */
    override fun getByIndividualOverallBatting(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary> {
        // Set up the database context
        val context = using(dataSource.dataSource, DIALECT)

        // Prepare Common Table Expressions (CTEs) for the query
        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
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
            JooqBattingCareerRecords.createResultsCte(
                searchParameters,
                BATTING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Fetches the batting statistics for an individual specific to a series, based on the provided
     * validated search parameters.
     *
     * The method constructs a complex database query using Common Table Expressions (CTEs) to filter,
     * aggregate, sort, and paginate the batting data as per the specified criteria. It returns detailed
     * information regarding the player's batting performance within the specified series.
     *
     * @param searchParameters The validated search parameters including match type, teams, opponents,
     * series, venue, date range, sorting preferences, and pagination details.
     * @return A `DatabaseResult` containing a list of `PrimaryBatting` objects representing the
     * individual's batting data for the series, along with the total count of matching records.
     */
    override fun getByIndividualSeries(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
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
            JooqBattingIndividualSeries.createResultsCte(
                searchParameters,
                BATTING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Fetches the batting statistics for an individual specific to different grounds based on the
     * provided validated search parameters.
     *
     * The method constructs a complex database query using Common Table Expressions (CTEs) to
     * apply filters, aggregate statistics, sort results, and provide pagination as specified
     * by the search parameters.
     *
     * @param searchParameters The validated search parameters including match type, teams, opponents,
     * ground, venue, date range, sorting preferences, and pagination details.
     * @return A `DatabaseResult` containing a list of `PrimaryBatting` objects representing
     * the individual batting data across different grounds, along with the total count of items
     * in the result.
     */
    override fun getByIndividualGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary> {
        // Set up the database context
        val context = using(dataSource.dataSource, DIALECT)

        // Prepare Common Table Expressions (CTEs) for the query
        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
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
            JooqBattingIndividualGrounds.createResultsCte(
                searchParameters,
                BATTING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)
    }

    /**
     * Fetches the batting statistics specific to matches hosted in a particular country based on the provided
     * validated search parameters.
     *
     * The method constructs a SQL query utilizing Common Table Expressions (CTEs) to apply filters, aggregate statistics,
     * sort results, and paginate the data as specified by the search parameters. It retrieves detailed batting information
     * for matches played in the specified host country.
     *
     * @param searchParameters The validated search parameters, including match type, teams, opponents,
     * host country, date range, sorting preferences, and pagination details.
     * @return A `DatabaseResult` containing a list of `PrimaryBatting` objects representing the batting data
     * in matches hosted in the specified country, along with the total count of matching records.
     */
    override fun getByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary> {
        // Set up the database context
        val context = using(dataSource.dataSource, DIALECT)

        // Prepare Common Table Expressions (CTEs) for the query
        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
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
        val resultsTable =
            JooqBattingIndividualHostCountry.createResultsCte(
                searchParameters,
                BATTING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)

    }

    /**
     * Fetches batting statistics based on opponents using the provided validated search parameters.
     *
     * The method constructs a complex database query utilizing Common Table Expressions (CTEs)
     * to filter data based on the opponents, apply sorting, aggregate results, and paginate
     * as per the specified parameters.
     *
     * @param searchParameters The validated search parameters that include details such as match type,
     *                         opponents, teams, date range, sorting preferences, and pagination details.
     * @return A `DatabaseResult` containing a list of `PrimaryBatting` objects matching the query,
     *         along with the total count of matching records.
     */
    override fun getByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
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
            JooqBattingIndividualOpposition.createResultsCte(
                searchParameters,
                BATTING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)

    }

    /**
     * Fetches the batting statistics for an individual specific to a year of the match start date,
     * based on the provided validated search parameters.
     *
     * The method constructs a SQL query utilizing Common Table Expressions (CTEs) to filter, aggregate,
     * sort, and paginate the batting data. It generates results that represent the player's performance
     * for the specified year of match start.
     *
     * @param searchParameters The validated search parameters, including match type, teams, opponents,
     *                         year of the match start, venue, date range, sorting preferences, and
     *                         pagination details.
     * @return A `DatabaseResult` containing a list of `PrimaryBatting` objects representing the individual's
     *         batting data for the given year, along with the total count of matching records.
     */
    override fun getByIndividualYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary> {
        // Set up the database context
        val context = using(dataSource.dataSource, DIALECT)

        // Prepare Common Table Expressions (CTEs) for the query
        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
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
            JooqBattingIndividualYearOfMatchStart.createResultsCte(
                searchParameters,
                BATTING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)

    }

    /**
     * Retrieves individual season batting data based on the provided search parameters. The query is
     * executed using multiple Common Table Expressions (CTEs) to prepare and shape the data,
     * including sorting and pagination.
     *
     * @param searchParameters The validated parameters used for filtering, sorting, and paginating
     *                          the individual season batting statistics query.
     * @return A [DatabaseResult] containing the list of [BattingPrimary] records that match the search
     *         criteria and the total count of results.
     */
    override fun getByIndividualSeason(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary> {
        // Set up the database context
        val context = using(dataSource.dataSource, DIALECT)

        // Prepare Common Table Expressions (CTEs) for the query
        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)
        JooqHelpers.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
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
            JooqBattingIndividualSeason.createResultsCte(
                searchParameters,
                BATTING_TEMP_DETALS_TABLE,
                TEAMS_TEMP_TABLE,
                MATCHCOUNTS_TEMP_TABLE
            )
        return generateResults(searchParameters, context, resultsTable)

    }


    /**
     * Fetches innings-by-innings batting statistics for an individual based on the provided validated search parameters.
     *
     * The method constructs a SQL query using Common Table Expressions (CTEs) to aggregate, filter,
     * sort, and paginate the batting data as per the specified criteria. It returns detailed
     * information about each innings for a player, including match-specific details and performance metrics.
     *
     * @param searchParameters The validated search parameters containing the match type, teams,
     * opponents, venue, date range, sorting preferences, and pagination details.
     * @return A `DatabaseResult` containing a list of `InningsByInningsBatting` objects representing
     * the batting data for each innings, along with the total count of matching records.
     */
    override fun getByIndividualInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)

        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)


        JooqHelpers.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
            matchIdTempTableName = MATCHIDS_TEMP_TABLE,
            searchParameters
        )

        val resultsTable =
            JooqBattingInningsByInnings.createResultsCte(searchParameters, BATTING_TEMP_DETALS_TABLE)
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)

        // Define sorting based on parameters
        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(determineSortFieldName(searchParameters.sortOrder.name)).asc()
            SortDirection.DESC -> field(determineSortFieldName(searchParameters.sortOrder.name)).desc()
        }


        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("$RESULTS_TABLE, $TOTAL_COUNTS_TABLE")
            .orderBy(
                sortSpecification,
                field("matchid"),
                field("inningsOrder"),
                field("position"),
                field("SortNamePart")
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
     * Fetches individual match-specific batting statistics based on the provided validated search parameters.
     *
     * The method constructs a SQL query utilizing Common Table Expressions (CTEs) to filter, aggregate,
     * sort, and paginate the batting data, returning a list of innings-by-innings batting statistics
     * matching the specified criteria.
     *
     * @param searchParameters The validated search parameters containing the match type, teams,
     * opponents, date range, venue, sorting preferences, and pagination details.
     * @return A `DatabaseResult` containing a list of `InningsByInningsBatting` objects representing
     * individual match totals along with the total count of such records.
     */
    override fun getByIndividualMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingInningsByInnings> {
        val context = using(dataSource.dataSource, DIALECT)
        JooqHelpers.createTemporaryMatchIds(context, MATCHIDS_TEMP_TABLE, searchParameters)

        JooqBattingMatchRecords.createTemporaryBattingDetailsTable(
            context,
            battingDetailsTempTableName = BATTING_TEMP_DETALS_TABLE,
            searchParameters = searchParameters,
        )

        val resultsTable: SelectConditionStep<Record22<String?, String?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, Int?, String?, String?, String?, Int?, String?, LocalDate?, Long?, String?, Double?>?> =
            JooqBattingMatchRecords.createResultsCte(searchParameters, BATTING_TEMP_DETALS_TABLE)

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
            .orderBy(sortSpecification, field("SortNamePart"))
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
     * Generates and fetches results from the database based on the provided search parameters.
     *
     * @param searchParameters The search parameters that include filters, pagination, and sorting details.
     * @param context The DSLContext used for building and executing the query in the database.
     * @param resultsTable The initial select query representing the results table to be queried.
     * @return A DatabaseResult containing a list of mapped `PrimaryBatting` domain objects and the total count of results.
     */
    private fun generateResults(
        searchParameters: ValidatedSearchParameters,
        context: DSLContext,
        resultsTable: SelectConnectByStep<*>
    ): DatabaseResult<BattingPrimary> {
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
            .orderBy(sortSpecification, field("SortNamePart"))
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects

        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryBatting(it)
        }

        return DatabaseResult(results, count)
    }

    private fun determineSortFieldName(sortOrderName: String): String {
        return if (sortOrderName == "Runs") "NotoutAdjustedScore" else sortOrderName
    }


    /**
     * Maps a database record to a `PrimaryBatting` object.
     *
     * @param record the database record of type `org.jooq.Record` to be mapped
     * @return a `PrimaryBatting` object containing the mapped data
     */
    private fun mapToPrimaryBatting(record: org.jooq.Record): BattingPrimary {
        return BattingPrimary(
            playerId = record.getValue("playerid", Int::class.java),
            name = record.getValue("FullName", String::class.java),
            team = record.getValue("teams", String::class.java),
            sortNamePart = record.getValue("SortNamePart", String::class.java),
            debut = record.getValueOrNull("debut", String::class.java) ?: "",
            end = record.getValueOrNull("end", String::class.java) ?: "",
            matches = record.getValue("matches", Int::class.java),
            innings = record.getValue("innings", Int::class.java),
            notOuts = record.getValue("notouts", Int::class.java),
            runs = record.getValue("runs", Int::class.java),
            highestScore = record.getValue("highestscore", Double::class.java),
            hundreds = record.getValue("hundreds", Int::class.java),
            fifties = record.getValue("fifties", Int::class.java),
            ducks = record.getValue("ducks", Int::class.java),
            fours = record.getValue("fours", Int::class.java),
            sixes = record.getValue("sixes", Int::class.java),
            balls = record.getValue("balls", Int::class.java),
            avg = record.getValue("avg", Double::class.java),
            sr = record.getValue("sr", Double::class.java),
            bi = record.getValue("bi", Double::class.java),
            opponents = record.getValueOrNull("opponents", String::class.java) ?: "",
            year = record.getValueOrNull("year", String::class.java) ?: "",
            ground = record.getValueOrNull("ground", String::class.java) ?: "",
            countryName = record.getValueOrNull("CountryName", String::class.java) ?: "",
        )
    }

    /**
     * Maps a database record to an `InningsByInningsBatting` object.
     *
     * @param record the database record of type `org.jooq.Record` to be mapped
     * @return an `InningsByInningsBatting` object containing the mapped data
     */
    private fun mapToInningsByInnings(record: org.jooq.Record): BattingInningsByInnings {
        return BattingInningsByInnings(
            playerId = record.getValue("playerid", Int::class.java),
            matchId = record.getValue("matchId", String::class.java),
            fullName = record.getValue("FullName", String::class.java),
            sortNamePart = record.getValue("SortNamePart", String::class.java),
            playerScore = record.getValue("runs", Int::class.java),
            notOut = record.getValueOrNull("notout", Int::class.java),
            bat1 = record.getValueOrNull("bat1", Int::class.java),
            notOut1 = record.getValueOrNull("notOut1", Int::class.java),
            bat2 = record.getValueOrNull("bat2", Int::class.java),
            notOut2 = record.getValueOrNull("notOut2", Int::class.java),
            inningsNumber = record.getValueOrNull("inningsOrder", Int::class.java) ?: 0,
            team = record.getValue("teams", String::class.java),
            matchDate = record.getValue("MatchStartDate", String::class.java),
            ground = record.getValue("KnownAs", String::class.java),
            opponents = record.getValue("opponents", String::class.java),
            sr = record.getValue("sr", Double::class.java),
            sixes = record.getValue("sixes", Int::class.java),
            fours = record.getValue("fours", Int::class.java),
            balls = record.getValue("balls", Int::class.java),
            minutes = record.getValue("minutes", Int::class.java),
        )
    }
}