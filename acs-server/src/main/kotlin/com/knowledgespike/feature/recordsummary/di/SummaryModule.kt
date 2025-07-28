package com.knowledgespike.feature.recordsummary.di

import com.knowledgespike.feature.recordsummary.data.repository.JooqSearchSummaryRepository
import com.knowledgespike.feature.recordsummary.repository.SearchSummaryRepository
import com.knowledgespike.feature.recordsummary.usecase.GetSummaryUseCase
import com.knowledgespike.feature.recordsummary.usecase.SummaryUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module responsible for providing dependencies used in summarizing match records.
 *
 * This module includes the following components:
 * - A singleton instance of `SummaryUseCases` which orchestrates various use cases associated with summaries.
 * - A singleton instance of `GetSummaryUseCase` that encapsulates the logic for retrieving match summaries.
 * - A singleton instance of `JooqSearchSummaryRepository`, which serves as the repository for fetching data related to teams, grounds, and host countries, bound to the `SearchSummary
 * Repository` interface.
 *
 * This module simplifies dependency injection for components related to record summarization.
 */
val recordSummaryModule = module {
    singleOf(::SummaryUseCases)
    singleOf(::GetSummaryUseCase)
    singleOf(::JooqSearchSummaryRepository) bind SearchSummaryRepository::class

}