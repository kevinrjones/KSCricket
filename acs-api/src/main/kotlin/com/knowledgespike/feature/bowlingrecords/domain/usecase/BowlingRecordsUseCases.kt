package com.knowledgespike.feature.bowlingrecords.domain.usecase

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.bowlingrecords.domain.model.BowlingInningsByInnings
import com.knowledgespike.feature.bowlingrecords.domain.model.BowlingPrimary
import com.knowledgespike.feature.bowlingrecords.domain.repository.BowlingRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * A collection of use cases that encapsulate specific operations for retrieving
 * and processing bowling records from various perspectives. Each of the included
 * use cases represents a distinct query or data-fetching operation related to
 * an individual's bowling performance in cricket.
 *
 * @property getIndividualOverallBowlingRecords Use case to fetch an individual's overall bowling records.
 * @property getIndividualSeriesUseCase Use case to fetch an individual's performance in specific series.
 * @property getIndividualByYearOfMatchStartUseCase Use case to fetch an individual's records categorized by the year matches started.
 * @property getIndividualSeasonUseCase Use case to fetch an individual's performance during specific seasons.
 * @property getIndividualGroundsUseCase Use case to fetch an individual's records for matches played at specific grounds.
 * @property getByHostCountryUseCase Use case to fetch bowling records based on matches hosted in specific countries.
 * @property getByOpponentsUseCase Use case to fetch bowling records against specific opponents.
 * @property getIndividualInningsByInningsRecords Use case to fetch detailed records for an individual's bowling across innings.
 * @property gatIndividualMatchTotalsRecords Use case to fetch aggregate bowling totals for an individual across matches.
 */
data class BowlingRecordsUseCases(
    val getIndividualOverallBowlingRecords: GetIndividualOverallBowlingUseCase,
    val getIndividualSeriesUseCase: GetIndividualSeriesUseCase,
    val getIndividualByYearOfMatchStartUseCase: GetIndividualByYearOfMatchStartUseCase,
    val getIndividualSeasonUseCase: GetIndividualSeasonUseCase,
    val getIndividualGroundsUseCase: GetIndividualGroundsUseCase,
    val getByHostCountryUseCase: GetByHostCountryUseCase,
    val getByOpponentsUseCase: GetByOpponentsUseCase,
    val getIndividualInningsByInningsRecords: GetIndividualInningsByInningsUseCase,
    val gatIndividualMatchTotalsRecords: GetIndividualMatchTotalsUseCase,
)


/**
 * A use case for retrieving overall bowling statistics for an individual.
 *
 * This class interacts with the `BowlingRepository` to fetch bowling data based on
 * predefined and validated query parameters.
 *
 * @constructor Initializes the use case with the provided `BowlingRepository` instance.
 * @property repository The repository responsible for handling bowling-related data retrieval.
 */
class GetIndividualOverallBowlingUseCase(val repository: BowlingRepository) {
    /**
     * Invokes the use case to retrieve individual overall bowling statistics based on the specified search parameters.
     *
     * @param searchParameters The validated search parameters containing filters and configurations for the query.
     * @return A database result encapsulating a list of BowlingPrimary objects and the total count of results.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingPrimary> {
        return repository.getByIndividualOverallBowling(searchParameters)
    }
}

/**
 * Use case for fetching individual series-wise bowling statistics.
 *
 * This class encapsulates the business logic for querying a repository
 * to retrieve bowling statistics for an individual, organized by series,
 * based on the provided validated search parameters.
 *
 * @property repository A repository instance for accessing bowling statistics.
 */
class GetIndividualSeriesUseCase(val repository: BowlingRepository) {
    /**
     * Retrieves bowling statistics for an individual, organized by series, based on
     * the provided search parameters.
     *
     * @param searchParameters The validated search parameters used to query bowling statistics.
     * @return A result object containing a list of bowling statistics and the total count.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingPrimary> {
        return repository.getByIndividualSeries(searchParameters)
    }
}

/**
 * Use case for fetching bowling statistics grouped by the individual year of the match start date.
 *
 * This use case interacts with the [BowlingRepository] to retrieve data based on specific
 * search parameters provided as input. It leverages the repository's `getByIndividualYearOfMatchStart`
 * function to obtain the relevant results and return them in a structured [DatabaseResult].
 *
 * @constructor Creates an instance of this use case with the specified [BowlingRepository].
 * @property repository The repository used to access bowling data.
 */
class GetIndividualByYearOfMatchStartUseCase(val repository: BowlingRepository) {
    /**
     * Executes a query to fetch bowling data based on the provided validated search parameters.
     *
     * @param searchParameters The validated search parameters, including filters like match type, teams, venue, date range,
     * sorting preferences, and more, to specify the criteria for fetching bowling statistics.
     * @return A `DatabaseResult` containing a list of `BowlingPrimary` entities that match the given search parameters,
     * along with the total count of results.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingPrimary> {
        return repository.getByIndividualYearOfMatchStart(searchParameters)
    }
}

/**
 * A use case for retrieving bowling statistics of an individual player filtered by a specific season.
 *
 * This class interacts with the `BowlingRepository` to fetch data based on the provided
 * validated search parameters, ensuring accurate and consistent results.
 *
 * @constructor Creates an instance of `GetIndividualSeasonUseCase` with the specified repository.
 * @param repository An implementation of `BowlingRepository` to handle the data fetching logic.
 */
class GetIndividualSeasonUseCase(val repository: BowlingRepository) {
    /**
     * Invokes the use case to fetch bowling statistics based on the provided search parameters.
     *
     * @param searchParameters The validated parameters used to filter and query bowling statistics.
     * @return A result containing a list of bowling statistics and the total count of records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingPrimary> {
        return repository.getByIndividualSeason(searchParameters)
    }
}

/**
 * Use case for retrieving individual bowling statistics filtered by specific grounds.
 *
 * This class acts as a mediator between the repository and the user interface or other
 * domain layers where these results are needed. It encapsulates the invocation logic
 * for querying the `BowlingRepository` with the relevant parameters.
 *
 * @property repository The repository instance used to fetch bowling statistics data.
 */
class GetIndividualGroundsUseCase(val repository: BowlingRepository) {
    /**
     * Executes a search query using the provided validated search parameters.
     *
     * @param searchParameters The validated search parameters used to filter the database query.
     * @return A `DatabaseResult` containing a list of `BowlingPrimary` objects that match the search criteria.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingPrimary> {
        return repository.getByIndividualGrounds(searchParameters)
    }
}

/**
 * Use case for retrieving bowling statistics filtered by the host country.
 *
 * This class provides an abstraction for querying the repository
 * to fetch bowling statistics based on validated search parameters
 * that include the host country and other criteria.
 *
 * @property repository The `BowlingRepository` instance used to access bowling-related data.
 */
class GetByHostCountryUseCase(val repository: BowlingRepository) {
    /**
     * Retrieves bowling records for the specified host country based on validated search parameters.
     *
     * This function acts as an operator to directly query the bowling repository, using
     * the given search parameters to fetch records matching the specified criteria.
     *
     * @param searchParameters The validated search parameters used to filter and retrieve bowling records.
     *                         These parameters include various filtering options like match type, host country ID,
     *                         date range, and sorting preferences.
     * @return A `DatabaseResult` containing a list of `BowlingPrimary` records that match the search criteria
     *         and the total count of matching records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingPrimary> {
        return repository.getByHostCountry(searchParameters)
    }
}

/**
 * Use case for retrieving bowling statistics filtered by opposing teams.
 *
 * This class provides a method to invoke the repository function
 * that queries bowling data based on specific search parameters
 * pertaining to opposing teams.
 *
 * @property repository The repository used to access bowling statistics.
 */
class GetByOpponentsUseCase(val repository: BowlingRepository) {
    /**
     * Invokes the use case to fetch bowling primary details based on the provided validated search parameters.
     *
     * @param searchParameters The validated search parameters containing criteria such as match type,
     * opponents, date range, and other filtering options for querying bowling primary records.
     * @return A `DatabaseResult` containing a list of `BowlingPrimary` results along with their count.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingPrimary> {
        return repository.getByOpponents(searchParameters)
    }
}

/**
 * Use case for retrieving individual bowling statistics organized on an innings-by-innings basis.
 *
 * This class interacts with the provided `BowlingRepository` to fetch data corresponding to the
 * specified search parameters. It encapsulates the logic required to query and return bowling
 * records grouped per innings.
 *
 * @constructor Initializes the use case with a repository for accessing bowling data.
 * @param repository The `BowlingRepository` instance used to retrieve the data.
 */
class GetIndividualInningsByInningsUseCase(val repository: BowlingRepository) {
    /**
     * Invokes the repository method to retrieve bowling innings by innings data
     * using the provided search parameters.
     *
     * @param searchParameters The validated search parameters to filter the data.
     * @return A `DatabaseResult` containing a list of `BowlingInningsByInnings`
     *         matching the search criteria.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingInningsByInnings> {
        return repository.getByIndividualInningsByInnings(searchParameters)
    }
}

/**
 * Use case for retrieving bowling statistics aggregated on a per-match basis for an individual player.
 * This operation fetches data from a repository using validated search parameters.
 *
 * @param repository The data source that provides access to the bowling statistics.
 */
class GetIndividualMatchTotalsUseCase(val repository: BowlingRepository) {
    /**
     * Invokes the use case to retrieve individual player bowling performance records aggregated on a per-match basis.
     *
     * The search parameters are passed to the repository to fetch the relevant data, including details like
     * match type, opponents, date range, sorting, and paging configurations.
     *
     * @param searchParameters The validated search parameters used for querying individual match totals.
     * @return A DatabaseResult containing a list of BowlingInningsByInnings objects representing
     *         individual player performances match by match and the total count of such records.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<BowlingInningsByInnings> {
        return repository.getByIndividualMatchTotals(searchParameters)
    }
}

