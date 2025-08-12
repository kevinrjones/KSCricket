package com.knowledgespike.feature.datainfo.di

import com.knowledgespike.feature.datainfo.data.repository.JooqDataInfoRepository
import com.knowledgespike.feature.datainfo.domain.repository.DataInfoRepository
import com.knowledgespike.feature.datainfo.domain.usecase.DataInfoUseCases
import com.knowledgespike.feature.datainfo.domain.usecase.GetLastDataDataAddedUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataInfoModule = module {
    singleOf(::DataInfoUseCases)
    singleOf(::GetLastDataDataAddedUseCase)

    singleOf(::JooqDataInfoRepository) bind DataInfoRepository::class
}