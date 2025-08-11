package com.knowledgespike.feature.frontpage.di

import com.knowledgespike.feature.frontpage.data.repository.JooqFrontPageRepository
import com.knowledgespike.feature.frontpage.domain.repository.FrontPageRepository
import com.knowledgespike.feature.frontpage.domain.usecase.FrontPageUseCases
import com.knowledgespike.feature.frontpage.domain.usecase.GetRecentMatchesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val frontPageModule = module {
    singleOf(::FrontPageUseCases)
    singleOf(::GetRecentMatchesUseCase)

    singleOf(::JooqFrontPageRepository) bind FrontPageRepository::class
}