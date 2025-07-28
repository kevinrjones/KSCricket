package com.knowledgespike.feature.partnershiprecords.data.repository

import com.knowledgespike.DIALECT
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.feature.partnershiprecords.domain.model.PartnershipIndividualRecordDetails
import com.knowledgespike.feature.partnershiprecords.domain.model.PartnershipPrimary
import com.knowledgespike.feature.partnershiprecords.domain.repository.PartnershipRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.JooqHelpers
import com.knowledgespike.plugins.DataSource
import org.jooq.impl.DSL.*

/**
 * Repository implementation for managing and retrieving partnership statistics using jOOQ.
 *
 * This class provides methods to query partnership statistics across various dimensions,
 * such as series, grounds, host countries, opponents, years, and seasons, as well as
 * specific details at the innings and match level. The implementation leverages jOOQ
 * to map database records to domain objects. Each query method returns results encapsulated
 * within a DatabaseResult structure.
 *
 * @constructor Initializes a JooqPartnershipsRepository instance with the provided DataSource.
 * @param dataSource The DataSource instance used for database connections.
 */
class JooqPartnershipsRepository(private val dataSource: DataSource) : PartnershipRepository {

    /**
     * Companion object containing constant values used within the repository.
     */
    companion object {
        /**
         * Represents the name of the database table used to store results data.
         *
         * This constant is used in database queries and operations related to retrieving
         * and manipulating data regarding match results or partnership records in the
         * context of cricket statistics or similar datasets.
         */
        private const val RESULTS_TABLE = "results"
        /**
         * Represents the database table name for tracking total counts in partnership records.
         * This constant is primarily used within SQL queries or database-related operations
         * in the JooqPartnershipsRepository class to refer to the `total_counts` table.
         */
        private const val TOTAL_COUNTS_TABLE = "total_counts"
    }

    /**
     * Retrieves overall partnership data based on the provided search parameters.
     *
     * The method processes the search parameters to construct and execute the query to fetch
     * partnership data from the database. It includes sorting, filtering, and paging logic.
     *
     * @param searchParameters The validated search parameters used to filter and sort the query results.
     * @return A DatabaseResult object containing a list of PartnershipPrimary results and the total count of records.
     */
    override fun getOverallPartnership(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsOverall.createResultsCte(
                searchParameters,
            )
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
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.  map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryPartnerships(it)
        }

        return DatabaseResult(results, count)

    }

    /**
     * Fetches partnership data for a specific series based on the provided search parameters.
     *
     * This method executes a database query with the specified sorting and paging parameters,
     * then maps the resulting records to domain objects representing primary partnerships.
     *
     * @param searchParameters A validated set of search parameters including filters for match type,
     * match details, sorting preferences, and paging configurations.
     * @return A `DatabaseResult` containing a list of mapped partnership data and the total count
     * of records matching the search criteria.
     */
    override fun getBySeries(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsBySeries.createResultsCte(
                searchParameters,
            )
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
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryPartnerships(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves partnership statistics based on the grounds specified in the provided search parameters.
     * Utilizes paging and sorting configurations to organize and limit the results.
     *
     * @param searchParameters The validated search parameters specifying criteria such as grounds,
     * sorting options, and paging configurations for the query.
     * @return A `DatabaseResult` containing a list of `PartnershipPrimary` items representing the
     * results of the query along with the total count of matching items.
     */
    override fun getByGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsByGround.createResultsCte(
                searchParameters,
            )
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
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryPartnerships(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Executes a query to fetch partnerships by the host country based on the given search parameters.
     *
     * @param searchParameters The validated search parameters containing filters, sorting preferences,
     *                          paging configurations, and other criteria to refine the search results.
     * @return A `DatabaseResult` object containing a list of `PartnershipPrimary` results that match
     *         the specified search criteria and the total count of items.
     */
    override fun getByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsByHostCountry.createResultsCte(
                searchParameters,
            )
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
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryPartnerships(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves partnership records for specific opponents based on the given search parameters.
     *
     * The method uses the provided validated search parameters to query the database and fetch
     * partnership records filtered by opponents, then applies sorting and paging as specified
     * in the parameters.
     *
     * @param searchParameters A `ValidatedSearchParameters` object that includes details such as
     *                         the specific opponents, sorting preferences, and paging configurations.
     * @return A `DatabaseResult<PartnershipPrimary>` object containing the queried data and the
     *         total count of records.
     */
    override fun getByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsByOpposition.createResultsCte(
                searchParameters,
            )
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
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryPartnerships(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves partnership data filtered by the year of match start.
     *
     * @param searchParameters The validated search parameters containing filtering, sorting, and paging details for the query.
     * @return A DatabaseResult containing a list of PartnershipPrimary objects representing the partnership data and the total count of matching records.
     */
    override fun getByYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsByYearOfMatchStart.createResultsCte(
                searchParameters,
            )
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
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryPartnerships(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves partnership statistics filtered and sorted by season.
     *
     * The method processes the validated search parameters to query the database
     * for partnerships within the specified season, applying sorting and paging
     * criteria defined in the search parameters.
     *
     * @param searchParameters The validated search parameters used to filter, sort,
     *                          and paginate the database query results.
     * @return A `DatabaseResult` containing a list of `PartnershipPrimary` objects
     *         representing the partnerships and the total count of records.
     */
    override fun getBySeason(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsBySeason.createResultsCte(
                searchParameters,
            )
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
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToPrimaryPartnerships(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves individual partnership records on an innings-by-innings basis based on the provided search parameters.
     *
     * @param searchParameters The validated set of search parameters specifying the filter options,
     *                          sorting preferences, and pagination details for the query.
     * @return A `DatabaseResult` containing a list of `PartnershipIndividualRecordDetails` that match the search criteria
     *         and the total count of records.
     */
    override fun getInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipIndividualRecordDetails> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsInningsByInnings.createResultsCte(
                searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)


        val sortOrderName = if(searchParameters.sortOrder.name == "Runs") {
            "SyntheticPartnership"
        } else {
            searchParameters.sortOrder.name
        }

        // Define sorting based on parameters
        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(sortOrderName).asc()
            SortDirection.DESC -> field(sortOrderName).desc()
        }

        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToIndividualPartnerships(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves partnership data for specific innings grouped by wicket number, based on the provided search parameters.
     *
     * This function executes a database query with filtering, sorting, and pagination criteria defined
     * in the provided `searchParameters`, and returns the results as a list of partnership details
     * along with the total count.
     *
     * @param searchParameters The validated search parameters containing filters, sorting preferences, and paging configurations.
     * @return A `DatabaseResult` object containing a list of partnership individual record details and the total count of results.
     */
    override fun getInningsByInningsForWicket(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipIndividualRecordDetails> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsInningsByInningsForWicket.createResultsCte(
                searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)

        val sortOrderName = if(searchParameters.sortOrder.name == "Runs") {
            "SyntheticPartnership"
        } else {
            searchParameters.sortOrder.name
        }

        // Define sorting based on parameters
        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(sortOrderName).asc()
            SortDirection.DESC -> field(sortOrderName).desc()
        }

        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToIndividualPartnerships(it)
        }

        return DatabaseResult(results, count)
    }

    /**
     * Retrieves partnership individual record details filtered and sorted based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters containing filtering, sorting, and paging details.
     * @return A DatabaseResult object containing a list of partnership individual record details and the total count.
     */
    override fun getByMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipIndividualRecordDetails> {
        val context = using(dataSource.dataSource, DIALECT)

        val resultsTable =
            JooqPartnershipsByMatch.createResultsCte(
                searchParameters,
            )
        val totalCountsTable = JooqHelpers.totalCountsCte(RESULTS_TABLE)

        val sortOrderName = if(searchParameters.sortOrder.name == "Runs") {
            "SyntheticPartnership"
        } else {
            searchParameters.sortOrder.name
        }

        // Define sorting based on parameters
        val sortSpecification = when (searchParameters.sortDirection) {
            SortDirection.ASC -> field(sortOrderName).asc()
            SortDirection.DESC -> field(sortOrderName).desc()
        }

        // Execute the query with CTEs and fetch results
        val databaseResults = context
            .with(RESULTS_TABLE).`as`(select().from(resultsTable))
            .with(TOTAL_COUNTS_TABLE).`as`(select().from(totalCountsTable))
            .select()
            .from("${RESULTS_TABLE}, ${TOTAL_COUNTS_TABLE}")
            .orderBy(sortSpecification)
            .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
            .fetch()

        // Map results to domain objects
        var count = 0
        val results = databaseResults.map {
            count = it.getValue("count", Int::class.java)
            mapToIndividualPartnerships(it)
        }

        return DatabaseResult(results, count)
    }


    /**
     * Maps a jOOQ Record object to an instance of the PartnershipPrimary data class.
     *
     * @param record the jOOQ Record object containing the data to be mapped
     * @return an instance of PartnershipPrimary created using the values in the provided record
     */
    private fun mapToPrimaryPartnerships(record: org.jooq.Record): PartnershipPrimary {

        val highest = record.getValue("highest", Double::class.java)
        val highestScore = highest.toInt()
        val isUnbroken = highest.rem(highestScore) > 0.0

        return PartnershipPrimary(
            playerIds = record.getValue("playerids", String::class.java),
            playerNames = record.getValue("playernames", String::class.java),
            player1 = record.getValue("player1", String::class.java),
            player2 = record.getValue("player2", String::class.java),
            player1Id = record.getValue("player1Id", Int::class.java),
            player2Id = record.getValue("player2Id", Int::class.java),
            team = record.getValue("Name", String::class.java),
            hundreds = record.getValue("hundreds", Int::class.java),
            fifties = record.getValue("fifties", Int::class.java),
            runs = record.getValue("runs", Int::class.java),
            avg = record.getValue("avg", Double::class.java),
            highestScore = highestScore,
            unbroken = isUnbroken,
            innings = record.getValue("innings", Int::class.java),
            notOuts = record.getValue("notouts", Int::class.java),
            opponents = record.getValueOrNull("opponents", String::class.java) ?: "",
            ground = record.getValueOrNull("ground", String::class.java) ?: "",
            countryName = record.getValueOrNull("countryName", String::class.java) ?: "",
            seriesDate = record.getValueOrNull("seriesDate", String::class.java) ?: "",
        )
    }

    /**
     * Maps a jOOQ Record to a PartnershipIndividualRecordDetails object.
     * This function extracts values from the record and uses them to populate the fields of a PartnershipIndividualRecordDetails instance.
     *
     * @param record the jOOQ Record containing the data to be mapped
     * @return a PartnershipIndividualRecordDetails instance populated with the extracted data
     */
    private fun mapToIndividualPartnerships(record: org.jooq.Record): PartnershipIndividualRecordDetails {

        return PartnershipIndividualRecordDetails(
            matchId = record.getValue("CaId", String::class.java),
            playerIds = record.getValue("PlayerIds", String::class.java),
            playerNames = record.getValue("PlayerNames", String::class.java),
            player1 = record.getValue("player1", String::class.java),
            player2 = record.getValue("player2", String::class.java),
            player1Id = record.getValue("player1Id", Int::class.java),
            player2Id = record.getValue("player2Id", Int::class.java),
            team = record.getValue("teams", String::class.java),
            opponents = record.getValue("opponents", String::class.java),
            runs = record.getValue("runs", Int::class.java),
            innings = record.getValue("innings", Int::class.java),
            wicket = record.getValue("Wicket", Int::class.java),
            currentScore = record.getValue("CurrentScore", Int::class.java),
            unbroken1 = record.getValueOrNull("Unbroken1", Boolean::class.java) ?: false,
            unbroken2 = record.getValueOrNull("Unbroken2", Boolean::class.java) ?: false,
            previousWicket = record.getValue("PreviousWicket", Int::class.java),
            previousScore = record.getValue("PreviousScore", Int::class.java),
            ground = record.getValue("KnownAs", String::class.java),
            matchStartDate = record.getValue("MatchStartDate", String::class.java),
            matchTitle = record.getValue("MatchTitle", String::class.java),
            resultString = record.getValue("ResultString", String::class.java),
        )
    }

}