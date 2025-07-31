package com.knowledgespike.feature.partnershiprecords.domain.repository

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.partnershiprecords.domain.model.PartnershipIndividualRecordDetails
import com.knowledgespike.feature.partnershiprecords.domain.model.PartnershipPrimary
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * PartnershipRepository provides methods to retrieve partnership statistics and records
 * from the underlying database based on various filters and search parameters.
 */
interface PartnershipRepository {
    /**
     * Fetches overall partnership statistics based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters specifying criteria such as match type,
     * team, opponents, venue, date range, and other constraints to filter partnership data.
     * @return A DatabaseResult object containing a list of PartnershipPrimary records and the total count
     * of records that match the given search criteria.
     */
    fun getOverallPartnership(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipPrimary>
    /**
     * Retrieves partnership records filtered by series based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters used to filter series-specific partnership records.
     * @return A DatabaseResult containing a list of PartnershipPrimary records that match the series filter and the count of the results.
     */
    fun getBySeries(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary>
    /**
     * Retrieves partnership records based on the specified ground or venue details.
     *
     * @param searchParameters A validated set of criteria used to filter partnership records.
     * These parameters include ground ID, match type, date range, sorting options, and more.
     * @return A result containing a list of partnership records matching the specified criteria,
     * along with the total count of retrieved records.
     */
    fun getByGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary>
    /**
     * Retrieves partnership records based on the host country specified in the search parameters.
     *
     * @param searchParameters The validated search parameters containing criteria such as match type,
     * host country ID, date range, and other filters used to identify the partnerships to retrieve.
     * @return A DatabaseResult containing a list of PartnershipPrimary objects that match the search
     * criteria, along with the total count of such records.
     */
    fun getByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary>
    /**
     * Retrieves partnership records filtered by specific opposing teams based on the provided search parameters.
     *
     * @param searchParameters A set of validated parameters defining the search criteria such as match type,
     * team IDs, opponents, date range, sorting preferences, and paging configurations.
     * @return A DatabaseResult containing a list of PartnershipPrimary records that match the search criteria
     * and the count of total records.
     */
    fun getByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary>
    /**
     * Retrieves partnership individual record details on an innings-by-innings basis
     * based on the provided validated search parameters.
     *
     * @param searchParameters The validated parameters that define the search criteria
     * for querying innings-by-innings partnership records, including match details,
     * teams, venues, date range, and other configurations.
     * @return A [DatabaseResult] containing a list of [PartnershipIndividualRecordDetails]
     * along with the total count of results matching the search criteria.
     */
    fun getInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipIndividualRecordDetails>
    /**
     * Retrieves partnership details for individual innings filtered by a specific wicket.
     *
     * @param searchParameters The validated parameters used to filter the database query,
     * such as match type, teams, venue, date range, sorting preferences, and the wicket number.
     * @return A wrapper containing a list of partnership records,
     * where each record provides details such as player information, runs scored, wickets, and match details.
     */
    fun getInningsByInningsForWicket(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipIndividualRecordDetails>
    /**
     * Retrieves individual partnership records for matches based on the provided search parameters.
     *
     * @param searchParameters The validated parameters specifying the criteria for querying partnership details,
     * such as match type, teams, opponents, venue, date range, sorting preferences, and paging configurations.
     * @return A DatabaseResult containing a list of PartnershipIndividualRecordDetails that meet the search criteria,
     * along with the total count of records.
     */
    fun getByMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipIndividualRecordDetails>
    /**
     * Retrieves partnership data filtered by the year in which matches started, based on the provided search parameters.
     *
     * @param searchParameters The validated parameters defining the criteria for filtering partnerships,
     * including match type, teams, opponents, grounds, date ranges, and paging configurations.
     * @return A `DatabaseResult` containing a list of `PartnershipPrimary` records that match the search criteria,
     * along with the total count of records in the result.
     */
    fun getByYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary>
    /**
     * Retrieves partnership statistics filtered by a specific season.
     *
     * @param searchParameters The validated search parameters defining the criteria for the query,
     * such as match type, season, team, opponents, and other filters.
     * @return A database result containing a list of PartnershipPrimary objects and the count of results matching
     * the query.
     */
    fun getBySeason(searchParameters: ValidatedSearchParameters): DatabaseResult<PartnershipPrimary>
}