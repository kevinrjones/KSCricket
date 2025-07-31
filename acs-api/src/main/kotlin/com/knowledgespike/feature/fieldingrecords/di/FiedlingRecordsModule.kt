package com.knowledgespike.feature.fieldingrecords.di

import com.knowledgespike.feature.fieldingrecords.data.repository.JooqFieldingRepository
import com.knowledgespike.feature.fieldingrecords.domain.repository.FieldingRepository
import com.knowledgespike.feature.fieldingrecords.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Dependency injection module for fielding records-related use cases and repositories.
 *
 * This module provides necessary bindings for fielding-related use cases and repository
 * implementations, enabling their usage across the application. Use cases provided include:
 * - Overall individual fielding statistics
 * - Fielding statistics for a series
 * - Fielding statistics by year of match start
 * - Fielding statistics for a season
 * - Fielding statistics by grounds
 * - Fielding statistics by host country
 * - Fielding statistics by opponents
 * - Innings-by-innings fielding records
 * - Match totals for fielding statistics
 *
 * The `FieldingRepository` is implemented by the `JooqFieldingRepository`, which provides
 * access to the data through database operations.
 */
val fieldingRecordsModule = module {
    singleOf(::FieldingRecordsUseCases)
    singleOf(::GetIndividualOverallFieldingUseCase)
    singleOf(::GetIndividualSeriesUseCase)
    singleOf(::GetIndividualByYearOfMatchStartUseCase)
    singleOf(::GetIndividualSeasonUseCase)
    singleOf(::GetIndividualGroundsUseCase)
    singleOf(::GetByHostCountryUseCase)
    singleOf(::GetByOpponentsUseCase)
    singleOf(::GetIndividualInningsByInningsUseCase)
    singleOf(::GetIndividualMatchTotalsUseCase)


    singleOf(::JooqFieldingRepository) bind FieldingRepository::class

}