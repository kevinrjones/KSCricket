package com.knowledgespike.feature.partnershiprecords.domain.usecase

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.partnershiprecords.domain.model.PartnershipIndividualRecordDetails
import com.knowledgespike.feature.partnershiprecords.domain.model.PartnershipPrimary
import com.knowledgespike.feature.partnershiprecords.domain.repository.PartnershipRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * A container class that aggregates multiple use cases related to partnership records.
 *
 * This class provides access to various operations for retrieving partnership record data such as
 * overall partnership records, series-specific data, year-wise records, and other granular information.
 *
 * Use cases included:
 * - Retrieving individual overall partnership records
 * - Fetching partnership data by series
 * - Accessing data filtered by the start year of matches
 * - Getting season-specific partnership records
 * - Retrieving partnership records for specific grounds
 * - Fetching partnership data based on host countries
 * - Retrieving records categorized by opponents
 * - Accessing innings-by-innings partnership records
 * - Fetching wicket-specific innings-by-innings partnership data
 * - Obtaining partnership match totals records
 */
data class PartnershipRecordsUseCases(
    val getIndividualOverallPartnershipRecords: GetIndividualOverallPartnershipUseCase,
    val getIndividualSeriesUseCase: GetIndividualSeriesUseCase,
    val getIndividualByYearOfMatchStartUseCase: GetIndividualByYearOfMatchStartUseCase,
    val getIndividualSeasonUseCase: GetIndividualSeasonUseCase,
    val getIndividualGroundsUseCase: GetIndividualGroundsUseCase,
    val getByHostCountryUseCase: GetByHostCountryUseCase,
    val getByOpponentsUseCase: GetByOpponentsUseCase,
    val getIndividualInningsByInningsRecords: GetIndividualInningsByInningsUseCase,
    val getIndividualInningsByInningsForWicketsRecords: GetIndividualInningsByInningsForWicketUseCase,
    val gatIndividualMatchTotalsRecords: GetIndividualMatchTotalsUseCase,
)

/**
 * Use case class responsible for retrieving overall partnership statistics.
 *
 * This class interacts with the `PartnershipRepository` to fetch partnership data
 * based on the provided search criteria. It leverages the repository's methods to apply
 * complex filters and retrieve aggregated partnership results.
 *
 * @param repository The repository instance used to interact with partnership data.
 */
class GetIndividualOverallPartnershipUseCase(val repository: PartnershipRepository) {
    /**
     * Invokes a search operation using the provided validated search parameters and retrieves the resulting
     * partnership data from the database.
     *
     * @param searchParameters The validated search parameters used to perform the search in the database.
     * @return A result wrapper containing the partnership primary data retrieved from the database.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipPrimary> {
        return repository.getOverallPartnership(searchParameters)
    }
}

/**
 * Use case for retrieving partnership records specific to a series using the given search parameters.
 *
 * This class interacts with the PartnershipRepository to query database records
 * filtered by series criteria, based on the validated search parameters.
 *
 * @property repository The repository instance used to access partnership data.
 */
class GetIndividualSeriesUseCase(val repository: PartnershipRepository) {
    /**
     * Executes a search operation on the repository using the provided validated search parameters.
     *
     * @param searchParameters The validated search parameters used to query the repository.
     * @return A `DatabaseResult` containing the result of the query for `PartnershipPrimary` entities.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipPrimary> {
        return repository.getBySeries(searchParameters)
    }
}

/**
 * Use case for retrieving individual partnership records filtered by the year in which matches started.
 *
 * This class facilitates interaction with the `PartnershipRepository` to query partnership
 * data based on the year matches began, using the provided search criteria encapsulated
 * in `ValidatedSearchParameters`.
 *
 * @property repository An instance of `PartnershipRepository` responsible for performing
 * the actual database query for partnership records.
 */
class GetIndividualByYearOfMatchStartUseCase(val repository: PartnershipRepository) {
    /**
     * Invokes a database query to retrieve partnership data based on the validated search parameters provided.
     *
     * @param searchParameters the validated search parameters to filter the query
     * @return a database result containing the primary partnership data matching the search criteria
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipPrimary> {
        return repository.getByYearOfMatchStart(searchParameters)
    }
}

/**
 * Use case for retrieving partnership statistics filtered by a specific cricket season.
 *
 * This class acts as a bridge between the application logic and the data repository. It invokes
 * the appropriate repository method to fetch partnership records based on validated search parameters,
 * specifically filtering data by season.
 *
 * @param repository The repository instance used to access the partnership data.
 */
class GetIndividualSeasonUseCase(val repository: PartnershipRepository) {
    /**
     * Invokes a search in the repository using the provided search parameters.
     *
     * @param searchParameters An instance of ValidatedSearchParameters containing the criteria for the search.
     * @return A DatabaseResult object containing the result of the search for PartnershipPrimary.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipPrimary> {
        return repository.getBySeason(searchParameters)
    }
}

/**
 * Use case class responsible for retrieving partnership data filtered by grounds.
 * It leverages the `PartnershipRepository` to fetch records that match the specified criteria.
 *
 * @constructor Creates an instance of `GetIndividualGroundsUseCase` and requires a reference to a `PartnershipRepository`.
 * @property repository The repository used to access partnership data from the database.
 */
class GetIndividualGroundsUseCase(val repository: PartnershipRepository) {
    /**
     * Invokes a search operation using the provided validated search parameters.
     *
     * @param searchParameters the validated search parameters to be used for querying data.
     * @return a result containing the primary partnership data matching the search criteria.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipPrimary> {
        return repository.getByGrounds(searchParameters)
    }
}

/**
 * Use case for retrieving partnership records filtered by the host country.
 *
 * This class interacts with the PartnershipRepository to fetch data
 * based on criteria specified in the provided search parameters.
 *
 * @property repository The repository used to query the partnership records by host country.
 */
class GetByHostCountryUseCase(val repository: PartnershipRepository) {
    /**
     * Invokes the retrieval of a partnership primary entry from the database based on the provided search parameters.
     *
     * @param searchParameters the validated set of parameters to filter the search results
     * @return a database result containing the partnership primary that matches the search parameters
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipPrimary> {
        return repository.getByHostCountry(searchParameters)
    }
}

/**
 * Use case to fetch partnership records filtered by opposing teams based on given search parameters.
 *
 * This use case acts as a layer between the domain logic and the data layer, delegating
 * the task of retrieving partnership data to the `PartnershipRepository`. It utilizes validated
 * search parameters to query partnership records from the repository.
 *
 * @property repository The repository responsible for accessing partnership data.
 */
class GetByOpponentsUseCase(val repository: PartnershipRepository) {
    /**
     * Executes a database query using the provided validated search parameters to retrieve
     * a result containing primary partnership data.
     *
     * @param searchParameters The validated parameters defining the search criteria for fetching
     * partnership primary data from the database.
     * @return A result encapsulating partnership primary data retrieved from the database query.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipPrimary> {
        return repository.getByOpponents(searchParameters)
    }
}

/**
 * Use case to fetch partnership individual record details on an innings-by-innings basis.
 *
 * This class acts as a bridge between the application layer and the data layer,
 * encapsulating the logic to retrieve detailed partnership records per innings.
 *
 * @property repository The repository interface for accessing partnership data.
 */
class GetIndividualInningsByInningsUseCase(val repository: PartnershipRepository) {
    /**
     * Invokes the use case to retrieve partnership individual record details on an innings-by-innings basis.
     *
     * @param searchParameters The validated search parameters defining criteria such as match type, teams, venue,
     * date range, sorting options, and other configurations for querying partnership records.
     * @return A [DatabaseResult] containing a list of [PartnershipIndividualRecordDetails] along with the total
     * count of results matching the specified search criteria.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipIndividualRecordDetails> {
        return repository.getInningsByInnings(searchParameters)
    }
}

/**
 * Use case for retrieving individual partnership records on an innings-by-innings basis
 * filtered by a specific wicket.
 *
 * This use case invokes the `getInningsByInningsForWicket` method provided by the `PartnershipRepository`
 * and passes validated search parameters to query the relevant data from the database.
 *
 * @property repository An instance of `PartnershipRepository` used to fetch individual partnership records.
 */
class GetIndividualInningsByInningsForWicketUseCase(val repository: PartnershipRepository) {
    /**
     * Executes a use case to retrieve partnership details for individual innings filtered by a specific wicket.
     *
     * @param searchParameters The validated search parameters used to query the database. These parameters include details such as match type, teams, venue, date range, sorting preferences
     * , paging configurations, and the specific wicket for filtering.
     * @return A DatabaseResult containing a list of PartnershipIndividualRecordDetails. Each record includes information such as player details, runs scored, wickets, innings, match
     *  details, and opposing team information.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipIndividualRecordDetails> {
        return repository.getInningsByInningsForWicket(searchParameters)
    }
}

/**
 * Use case for retrieving individual match partnership totals.
 *
 * This class interacts with a given `PartnershipRepository` to fetch detailed records
 * of partnerships for individual matches based on the provided search parameters.
 *
 * @property repository The `PartnershipRepository` implementation used to query data.
 */
class GetIndividualMatchTotalsUseCase(val repository: PartnershipRepository) {
    /**
     * Invokes the process of retrieving individual partnership records based on the provided search parameters.
     *
     * @param searchParameters The validated parameters specifying the criteria for querying partnership details,
     * such as match type, opponents, venue, date range, sorting preferences, and paging configurations.
     * @return A DatabaseResult containing a list of PartnershipIndividualRecordDetails that match the given criteria,
     * along with the total count of records retrieved.
     */
    operator fun invoke(searchParameters: ValidatedSearchParameters) : DatabaseResult<PartnershipIndividualRecordDetails> {
        return repository.getByMatchTotals(searchParameters)
    }
}

