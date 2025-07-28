package com.knowledgespike.feature.fieldingrecords.domain.repository

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.fieldingrecords.domain.model.FieldingInningsByInnings
import com.knowledgespike.feature.fieldingrecords.domain.model.FieldingPrimary
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * A repository interface for querying fielding statistics. This interface provides methods
 * to retrieve various types of fielding data including individual performance, team performance,
 * and contextual fielding performances in cricket matches.
 */
interface FieldingRepository {
    /**
     * Retrieves overall fielding statistics for an individual based on the specified search parameters.
     *
     * @param searchParameters The validated parameters that define the filter criteria for the query.
     * These parameters include details such as match type, teams, opponents, ground, host country,
     * date range, and more.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` objects that match the specified
     * search parameters, along with the total count of matching records.
     */
    fun getByIndividualOverallFielding(searchParameters: ValidatedSearchParameters) : DatabaseResult<FieldingPrimary>
    /**
     * Retrieves fielding statistics for individual players based on a series of matches defined by the search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing filters such as match type, teams, dates,
     *                         venues, sorting preferences, and paging options to refine the query.
     * @return A DatabaseResult containing a list of FieldingPrimary objects representing the fielding statistics, along with a count of the total results.
     */
    fun getByIndividualSeries(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary>
    /**
     * Retrieves fielding records for an individual player filtered by specific grounds.
     *
     * @param searchParameters The set of validated parameters used to define the search criteria,
     * including match type, ground, date range, team details, sorting preferences, and more.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` records that match the search criteria
     * and the total count of the results.
     */
    fun getByIndividualGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary>
    /**
     * Retrieves fielding statistics based on the host country, using the provided search parameters.
     *
     * @param searchParameters Validated search parameters containing criteria such as the host country,
     * match type, date range, sorting preferences, and paging configurations.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` records matching the search criteria
     * along with the total count of such records.
     */
    fun getByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary>
    /**
     * Retrieves fielding statistics associated with specific opponents based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters used to filter the fielding statistics.
     * These parameters include criteria such as match type, opposing teams, date range, and sorting preferences.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` objects that match the search criteria,
     * along with a count of the total records retrieved.
     */
    fun getByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary>
    /**
     * Retrieves detailed fielding innings-by-innings statistics for an individual player based on the provided search parameters.
     *
     * @param searchParameters The set of validated search parameters defining the criteria for the query,
     * including match type, team, opponents, ground, host country, date range, season, and other relevant filtering or sorting configurations.
     * @return A `DatabaseResult` containing a list of `FieldingInningsByInnings` objects, along with the total count of results.
     */
    fun getByIndividualInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingInningsByInnings>
    /**
     * Retrieves fielding statistics for an individual player, grouped by match totals.
     *
     * @param searchParameters The validated search parameters used to filter and query the fielding data.
     * @return A `DatabaseResult` containing a list of `FieldingInningsByInnings` objects and the total count of records.
     */
    fun getByIndividualMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingInningsByInnings>
    /**
     * Retrieves fielding statistics grouped by the year when matches started based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters that specify the criteria for querying
     * fielding statistics, such as match type, team, opponents, grounds, start date, end date, and others.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` records matching the search criteria
     * and the total count of such records.
     */
    fun getByIndividualYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary>
    /**
     * Retrieves fielding statistics for an individual player filtered by a specific season.
     *
     * @param searchParameters The validated search parameters containing criteria such as match type, team, opponents, venue, date range, season, and sorting preferences.
     * @return A `DatabaseResult` containing a list of `FieldingPrimary` objects with the player's fielding data for the specified season and the total count of records retrieved
     * .
     */
    fun getByIndividualSeason(searchParameters: ValidatedSearchParameters): DatabaseResult<FieldingPrimary>
}