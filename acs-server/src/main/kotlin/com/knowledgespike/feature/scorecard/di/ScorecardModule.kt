package com.knowledgespike.feature.scorecard.di

import com.knowledgespike.feature.scorecard.data.repository.JooqScorecardRepository
import com.knowledgespike.feature.scorecard.domain.repository.ScorecardRepository
import com.knowledgespike.feature.scorecard.domain.usecase.GetScorecardWithIdUseCase
import com.knowledgespike.feature.scorecard.domain.usecase.GetScorecardWithNamesUseCase
import com.knowledgespike.feature.scorecard.domain.usecase.ScorecardUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides dependency injection module for scorecard-related use cases and repository.
 *
 * This module includes dependencies for:
 * - `ScorecardUseCases`, which aggregates and provides access to specific use cases
 *   for interacting with scorecards.
 * - `GetScorecardWithIdUseCase`, which allows retrieval of a scorecard by its unique identifier.
 * - `GetScorecardWithNamesUseCase`, which allows retrieval of a scorecard using team names
 *   and a specific match date.
 * - `ScorecardRepository`, represented by `JooqScorecardRepository`, which handles the actual
 *   data interaction for scorecards.
 */
val scorecardModule = module {
    singleOf(::ScorecardUseCases)
    singleOf(::GetScorecardWithIdUseCase)
    singleOf(::GetScorecardWithNamesUseCase)


    singleOf(::JooqScorecardRepository) bind ScorecardRepository::class
}
