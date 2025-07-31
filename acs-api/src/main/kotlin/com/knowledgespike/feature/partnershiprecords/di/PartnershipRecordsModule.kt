package com.knowledgespike.feature.partnershiprecords.di

import com.knowledgespike.feature.partnershiprecords.data.repository.JooqPartnershipsRepository
import com.knowledgespike.feature.partnershiprecords.domain.repository.PartnershipRepository
import com.knowledgespike.feature.partnershiprecords.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module defining dependencies for partnership records management.
 *
 * This module provides various use cases related to the retrieval and management
 * of partnership records, allowing for efficient querying and manipulation of partnership data.
 *
 * Available use cases include fetching overall partnership statistics, querying partnership data
 * by series, season, grounds, host country, opponents, innings-by-innings records, and match totals.
 * These use cases interact with a `PartnershipRepository` to access the required data and execute
 * the corresponding logic.
 *
 * Additionally, this module binds a `JooqPartnershipsRepository` as the implementation for the
 * `PartnershipRepository` interface, ensuring proper dependency injection for the repository where
 * required.
 */
val partnershipRecordsModule = module {
    singleOf(::PartnershipRecordsUseCases)
    singleOf(::GetIndividualOverallPartnershipUseCase)
    singleOf(::GetIndividualSeriesUseCase)
    singleOf(::GetIndividualByYearOfMatchStartUseCase)
    singleOf(::GetIndividualSeasonUseCase)
    singleOf(::GetIndividualGroundsUseCase)
    singleOf(::GetByHostCountryUseCase)
    singleOf(::GetByOpponentsUseCase)
    singleOf(::GetIndividualInningsByInningsUseCase)
    singleOf(::GetIndividualInningsByInningsForWicketUseCase)
    singleOf(::GetIndividualMatchTotalsUseCase)


    singleOf(::JooqPartnershipsRepository) bind PartnershipRepository::class

}