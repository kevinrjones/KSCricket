package com.knowledgespike.feature.battingrecords.di

import com.knowledgespike.feature.battingrecords.data.repository.JooqBattingRepository
import com.knowledgespike.feature.battingrecords.domain.repository.BattingRepository
import com.knowledgespike.feature.battingrecords.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin DI module responsible for providing dependencies related to batting records.
 *
 * This module binds use case classes and repositories required for fetching and processing
 * detailed batting statistics. The use cases interact with the underlying `BattingRepository`
 * to perform various operations such as filtering batting data by individual statistics,
 * year, match, season, grounds, host countries, opponents, and more.
 *
 * Components provided by this module include:
 * - Use cases for different query operations on batting records, such as individual overall
 *   stats, series-based stats, year-based performance, innings-based details, season-based stats,
 *   ground-based insights, host country filtering, opponent filtering, and match totals.
 * - Koin single bindings for `BattingRepository` implemented by `JooqBattingRepository`.
 */
val battingRecordsModule = module {
    singleOf(::BattingRecordsUseCases)
    singleOf(::GetIndividualOverallBattingUseCase)
    singleOf(::GetIndividualSeriesUseCase)
    singleOf(::GetIndividualByYearOfMatchStartUseCase)
    singleOf(::GetIndividualSeasonUseCase)
    singleOf(::GetIndividualGroundsUseCase)
    singleOf(::GetByHostCountryUseCase)
    singleOf(::GetByOpponentsUseCase)
    singleOf(::GetIndividualInningsByInningsUseCase)
    singleOf(::GetIndividualMatchTotalsUseCase)


    singleOf(::JooqBattingRepository) bind BattingRepository::class
}
