package com.knowledgespike.feature.teamrecords.di

import com.knowledgespike.feature.teamrecords.data.repository.JooqTeamRepository
import com.knowledgespike.feature.teamrecords.domain.repository.TeamRepository
import com.knowledgespike.feature.teamrecords.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Dependency injection module responsible for providing use cases and repository implementations
 * related to team records.
 *
 * This module includes bindings for various use cases that enable querying and manipulating
 * team-related data, such as overall team statistics, match results, series data, season information,
 * and specific metrics like extras, target details, and innings performance. It also provides
 * a repository implementation for accessing team data.
 *
 * Features:
 * - Provides `TeamRecordsUseCases` as a container for aggregating all team-related use cases.
 * - Declares individual use case bindings, each encapsulating specific functionalities like:
 *   - Fetching overall team statistics.
 *   - Querying team series and seasonal data.
 *   - Retrieving team records filtered by various parameters, including grounds, opponents, or host countries.
 *   - Calculating advanced metrics such as extras, highest totals chased, and lowest targets defended.
 * - Binds a concrete implementation of `TeamRepository` (`JooqTeamRepository`) for accessing underlying
 *   team data.
 */
val teamRecordsModule = module {
    singleOf(::TeamRecordsUseCases)
    singleOf(::GetOverallTeamUseCase)
    singleOf(::GetTeamSeriesUseCase)
    singleOf(::GetTeamByYearOfMatchStartUseCase)
    singleOf(::GetTeamSeasonUseCase)
    singleOf(::GetTeamGroundsUseCase)
    singleOf(::GetByHostCountryUseCase)
    singleOf(::GetByOpponentsUseCase)
    singleOf(::GetTeamInningsByInningsUseCase)
    singleOf(::GetTeamMatchTotalsUseCase)
    singleOf(::GetTeamMatchResultsUseCase)
    singleOf(::GetTeamOverallExtras)
    singleOf(::GetTeamExtrasByInnings)
    singleOf(::GetTeamHighestTotalChased)
    singleOf(::GetTeamLowestTargetDefended)
    singleOf(::GetTeamLowestTargetDefendedInUnreducedMatch)


    singleOf(::JooqTeamRepository) bind TeamRepository::class
}
