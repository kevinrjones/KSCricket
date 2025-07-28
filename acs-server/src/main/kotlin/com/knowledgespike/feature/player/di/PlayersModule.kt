package com.knowledgespike.feature.player.di

import com.knowledgespike.feature.player.data.repository.JooqPlayerRepository
import com.knowledgespike.feature.player.domain.repository.PlayerRepository
import com.knowledgespike.feature.player.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module that provides dependency injection configuration for player-related use cases
 * and repository implementations. This module registers the following components:
 *
 * - Use case classes: `PlayerUseCases`, `GetPlayerBiographyUseCase`, `GetPlayerOverallUseCase`,
 *   `GetPlayerBattingDetailsUseCase`, `GetPlayerBowlingDetailsUseCase`, and `FindPlayerUseCase`,
 *   which encapsulate the business logic for accessing player data.
 *
 * - Repository implementation: `JooqPlayerRepository`, which provides data access and retrieval
 *   functionality for player-related operations using jOOQ.
 *
 * Dependencies registered in this module include:
 * - `PlayerUseCases`: An aggregated collection of all player-related use cases.
 * - Use cases for retrieving player biography, overall performance, batting details, and bowling
 *   details, as well as for searching players.
 * - `JooqPlayerRepository`, bound to the `PlayerRepository` interface, offering database-based
 *   persistence and querying functions.
 */
val playerModule = module {
    singleOf(::PlayerUseCases)
    singleOf(::GetPlayerBiographyUseCase)
    singleOf(::GetPlayerOverallUseCase)
    singleOf(::GetPlayerBattingDetailsUseCase)
    singleOf(::GetPlayerBowlingDetailsUseCase)
    singleOf(::FindPlayerUseCase)


    singleOf(::JooqPlayerRepository) bind PlayerRepository::class
}
