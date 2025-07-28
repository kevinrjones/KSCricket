package com.knowledgespike.feature.bowlingrecords.di

import com.knowledgespike.feature.bowlingrecords.data.repository.JooqBowlingRepository
import com.knowledgespike.feature.bowlingrecords.domain.repository.BowlingRepository
import com.knowledgespike.feature.bowlingrecords.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides a Koin DI module definition for managing dependencies related to bowling records.
 *
 * This module includes use cases for various aspects of querying and processing individual
 * bowling records, as well as the repository implementation for accessing the relevant data.
 *
 * Dependencies registered in this module:
 * - Use cases for querying bowling records (overall statistics, series records, records by year, season, grounds,
 *   host country, opponents, innings-by-innings, and match totals).
 * - `BowlingRepository` implementation for accessing and managing bowling data.
 */
val bowlingRecordsModule = module {
    singleOf(::BowlingRecordsUseCases)
    singleOf(::GetIndividualOverallBowlingUseCase)
    singleOf(::GetIndividualSeriesUseCase)
    singleOf(::GetIndividualByYearOfMatchStartUseCase)
    singleOf(::GetIndividualSeasonUseCase)
    singleOf(::GetIndividualGroundsUseCase)
    singleOf(::GetByHostCountryUseCase)
    singleOf(::GetByOpponentsUseCase)
    singleOf(::GetIndividualInningsByInningsUseCase)
    singleOf(::GetIndividualMatchTotalsUseCase)


    singleOf(::JooqBowlingRepository) bind BowlingRepository::class
}
