package com.knowledgespike.feature.teamrecords.domain.repository

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.feature.teamrecords.domain.model.*

/**
 * Interface representing a repository for retrieving team-related statistical data.
 */
interface TeamRepository {
    /**
     * Retrieves a summary of teams based on the specified search parameters.
     *
     * @param searchParameters The validated search parameters containing filters
     * such as match type, team details, venue, date range, sorting preferences,
     * and paging configurations.
     * @return A `DatabaseResult` containing a list of `TeamPrimary` objects that match
     * the provided search parameters, along with the total count of these records.
     */
    fun getByTeamSummary(searchParameters: ValidatedSearchParameters) : DatabaseResult<TeamPrimary>
    /**
     * Retrieves a team's primary record based on the given series-related search parameters.
     *
     * @param searchParameters The validated search parameters containing filters and criteria
     *                          such as match type, series details, and date range.
     * @return A `DatabaseResult` containing a list of `TeamPrimary` records and the total count of matching records.
     */
    fun getTeamBySeries(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary>
    /**
     * Retrieves team data based on provided ground-related search parameters.
     *
     * @param searchParameters The validated parameters containing criteria such as ground ID,
     * match type, date range, and other properties to filter the team data.
     * @return A DatabaseResult containing a list of TeamPrimary objects matching the search criteria
     * and the total count of results.
     */
    fun getTeamByGrounds(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary>
    /**
     * Retrieves team information based on the host country specified in the search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing criteria
     * such as the host country ID, match type, date range, and other search filters.
     * @return A DatabaseResult object containing a list of TeamPrimary instances and the total count
     * of matching entries based on the specified search criteria.
     */
    fun getTeamByHostCountry(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary>
    /**
     * Retrieves team information based on the specified opponent team(s).
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing the search criteria,
     * including the opponent team identifier, match type, date range, and other filtering preferences.
     * @return A DatabaseResult of type TeamPrimary that contains the list of teams matching the specified opponent criteria,
     * along with the count of matching results.
     */
    fun getTeamByOpponents(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary>
    /**
     * Retrieves the innings-by-innings performance details of a specific team based on the provided search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters that encapsulates
     *        details for querying such as team ID, opposing team, match type, venue, and date range.
     * @return A DatabaseResult containing a list of TeamInningsByInnings objects and the total count of results.
     */
    fun getTeamInningsByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamInningsByInnings>
    /**
     * Retrieves the match totals for a team based on the provided search parameters.
     *
     * The method queries the database to aggregate and return statistics for matches
     * involving the specified team, filtered and sorted according to the given search criteria.
     *
     * @param searchParameters The validated search criteria containing details about
     * match type, opposing teams, venues, date range, sorting preferences, and other filters.
     * @return A DatabaseResult containing a list of TeamInningsByInnings objects,
     * representing the aggregated match totals and the total count of results.
     */
    fun getTeamMatchTotals(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamInningsByInnings>
    /**
     * Retrieves the match results of a team based on the specified search parameters.
     *
     * @param searchParameters The validated search parameters used to filter the match results.
     * @return A `DatabaseResult` containing a list of `TeamMatchResult` objects and the total count of results.
     */
    fun getTeamMatchResults(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamMatchResult>
    /**
     * Retrieves team details based on the year of match start provided in the search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing the criteria
     * for the database query, including the range or specific year of match start.
     * @return A DatabaseResult containing a list of TeamPrimary objects that match the query
     * criteria, along with the total count of matching items.
     */
    fun getTeamByYearOfMatchStart(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary>
    /**
     * Fetches team data for a specific season based on validated search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing filter criteria,
     * including details such as season, match type, team ID, date range, and other optional parameters.
     * @return A DatabaseResult containing a list of TeamPrimary objects that match the given criteria,
     * along with the total count of matching records.
     */
    fun getTeamBySeason(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamPrimary>
    /**
     * Retrieves the overall extras statistics for a team based on the specified search parameters.
     *
     * @param searchParameters The validated search criteria containing details such as match type,
     * team ID, date range, and other filtering options.
     * @return A DatabaseResult containing a list of TeamExtrasOverall objects representing the
     * aggregated extras data and the total count of matching records.
     */
    fun getTeamOverallExtras(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamExtrasOverall>
    /**
     * Retrieves the team extras details split by innings based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters containing filters such as match type,
     * team ID, opponents, ground, date range, and other criteria to narrow down the query.
     * @return A DatabaseResult object containing a list of TeamExtrasInningsByInnings data and the total count of items.
     */
    fun getTeamExtrasByInnings(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamExtrasInningsByInnings>
    /**
     * Retrieves the highest total chased by a team based on the provided search parameters.
     *
     * @param searchParameters The validated search parameters containing criteria such as match type, team ID,
     *                         opponents ID, venue, date range, sorting preferences, and other filters.
     * @return A DatabaseResult containing a list of TeamTargetDetailsDto objects representing the highest totals chased,
     *         along with the count of such records.
     */
    fun getTeamHighestTotalChased(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamTargetDetailsDto>
    /**
     * Retrieves the lowest target successfully defended by a team based on the specified search parameters.
     *
     * @param searchParameters The validated search parameters that define the filtering criteria
     *                         for the query, such as team details, opponent information, match type,
     *                         venue, date range, and sorting preferences.
     * @return A DatabaseResult containing a list of TeamTargetDetailsDto objects that represent
     *         the lowest target defended records by the team, along with the total count of records returned.
     */
    fun getTeamLowestTargetDefended(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamTargetDetailsDto>
    /**
     * Retrieves the details of the lowest target defended by a team in an unreduced match
     * based on the specified search parameters. An unreduced match refers to matches that
     * have not been affected by interruptions or modifications such as those due to weather conditions.
     *
     * @param searchParameters The validated parameters defining the criteria
     * for filtering and searching matches. These include match type, team identifiers,
     * opponents, ground, date range, and other properties.
     *
     * @return A DatabaseResult containing a list of TeamTargetDetailsDto objects and the total count.
     * Each object provides the details of a match where the specified team has successfully
     * defended the lowest target under unreduced match conditions.
     */
    fun getTeamLowestTargetDefendedInUnreducedMatch(searchParameters: ValidatedSearchParameters): DatabaseResult<TeamTargetDetailsDto>
}