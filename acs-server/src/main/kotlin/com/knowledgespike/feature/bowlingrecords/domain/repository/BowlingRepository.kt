package com.knowledgespike.feature.bowlingrecords.domain.repository

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.bowlingrecords.domain.model.BowlingInningsByInnings
import com.knowledgespike.feature.bowlingrecords.domain.model.BowlingPrimary
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * Interface defining methods for retrieving bowling statistics from a data source.
 * This repository provides various query methods for fetching bowling-related data
 * based on specific search parameters.
 */
interface BowlingRepository {
    /**
     * Retrieves overall bowling statistics for an individual based on the provided search parameters.
     *
     * @param searchParameters The validated parameters used to filter and query the bowling statistics.
     * @return A DatabaseResult containing a list of BowlingPrimary objects along with the total count.
     */
    fun getByIndividualOverallBowling(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingPrimary>
    /**
     * Retrieves bowling statistics for an individual across various series based on the specified search parameters.
     *
     * @param searchParameters A set of validated search parameters that specify criteria such as match type,
     * team, opponents, venue, date range, season, sorting preferences, and paging configurations.
     * These parameters ensure accurate and consistent query results.
     * @return A DatabaseResult containing a list of BowlingPrimary objects that match the specified search parameters,
     * along with the total count of items in the result.
     */
    fun getByIndividualSeries(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary>
    /**
     * Retrieves bowling statistics for an individual player filtered by specific grounds.
     *
     * @param searchParameters The validated search parameters including match type, teams, ground ID,
     *                         date range, sorting preferences, and paging configurations.
     * @return A `DatabaseResult` containing a list of `BowlingPrimary` entries and the total count
     *         of records matching the criteria.
     */
    fun getByIndividualGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary>
    /**
     * Retrieves bowling statistics filtered by the host country specified in the search parameters.
     *
     * @param searchParameters The validated search parameters used to filter the query,
     * including the host country identifier and other criteria such as match type, date range,
     * and sorting preferences.
     * @return A DatabaseResult containing a list of BowlingPrimary objects that match
     * the criteria specified in the search parameters, along with the total count of results.
     */
    fun getByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary>
    /**
     * Retrieves bowling statistics filtered by opposing teams based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters containing filtering and sorting criteria,
     * including opponents, match type, date range, and paging configurations.
     * @return A DatabaseResult containing a list of BowlingPrimary entities and the total count
     * of results matching the criteria.
     */
    fun getByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary>
    /**
     * Retrieves bowling statistics for a specific player, organized on an innings-by-innings basis,
     * based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters used to filter and query the data.
     *        These parameters specify criteria such as match type, team, opponents, date range,
     *        and other relevant filters and configurations.
     * @return A DatabaseResult containing a list of BowlingInningsByInnings objects that match
     *         the provided search criteria, along with the total count of items.
     */
    fun getByIndividualInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingInningsByInnings>
    /**
     * Retrieves bowling performance records for individual players, aggregated on a per-match basis.
     *
     * @param searchParameters The validated parameters for filtering and querying bowling records,
     * including match type, opponents, date range, sorting, and paging configurations.
     * @return A DatabaseResult containing a list of BowlingInningsByInnings objects representing
     * individual player performances match by match and the total count of such records.
     */
    fun getByIndividualMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingInningsByInnings>
    /**
     * Fetches bowling statistics grouped by the individual year of the match start date based on the given search parameters.
     *
     * @param searchParameters The validated parameters defining the search criteria such as match type, opponents,
     *                         start and end dates, sorting preferences, and paging configurations.
     * @return A `DatabaseResult` containing a list of `BowlingPrimary` items and the total count of results.
     */
    fun getByIndividualYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary>
    /**
     * Retrieves individual bowling statistics filtered by specific seasons.
     *
     * This method queries the database using the provided validated search parameters
     * and returns bowling statistics that match the specified season criteria.
     *
     * @param searchParameters The validated search parameters that include filtering
     *        and sorting criteria such as season, match type, and additional options.
     * @return A `DatabaseResult` containing a list of `BowlingPrimary` objects that match
     *         the search parameters, along with the total count of matching items.
     */
    fun getByIndividualSeason(searchParameters: ValidatedSearchParameters): DatabaseResult<BowlingPrimary>
}