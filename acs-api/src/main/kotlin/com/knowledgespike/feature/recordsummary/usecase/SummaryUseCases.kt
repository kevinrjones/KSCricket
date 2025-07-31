package com.knowledgespike.feature.recordsummary.usecase

/**
 * Encapsulates use cases related to match summaries.
 *
 * @property summaryUseCase A use case instance for retrieving match summaries based on criteria such as
 * match type, teams, venue, and host country. This use case interacts with a repository to gather data
 * and generates a structured summary of the match details.
 */
data class SummaryUseCases(val summaryUseCase: GetSummaryUseCase)