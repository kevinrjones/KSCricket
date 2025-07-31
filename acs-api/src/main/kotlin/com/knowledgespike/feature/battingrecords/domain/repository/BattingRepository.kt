package com.knowledgespike.feature.battingrecords.domain.repository

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.battingrecords.domain.model.BattingInningsByInnings
import com.knowledgespike.feature.battingrecords.domain.model.BattingPrimary
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * Interface representing a repository for retrieving batting statistics.
 *
 * This repository provides functionalities to query overall batting statistics
 * using a set of validated search parameters. The data returned is structured as
 * a `DatabaseResult` containing a list of `PrimaryBatting` records and a total count.
 *
 * @see ValidatedSearchParameters for details on search criteria.
 * @see DatabaseResult for structure of the results.
 * @see BattingPrimary for the detailed structure of each batting record.
 */
interface BattingRepository {
    fun getByIndividualOverallBatting(searchParameters: ValidatedSearchParameters) : DatabaseResult<BattingPrimary>
    fun getByIndividualSeries(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary>
    fun getByIndividualGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary>
    fun getByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary>
    fun getByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary>
    fun getByIndividualInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingInningsByInnings>
    fun getByIndividualMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingInningsByInnings>
    fun getByIndividualYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary>
    fun getByIndividualSeason(searchParameters: ValidatedSearchParameters): DatabaseResult<BattingPrimary>
}