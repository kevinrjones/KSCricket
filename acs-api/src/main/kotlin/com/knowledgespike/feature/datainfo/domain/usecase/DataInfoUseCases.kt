package com.knowledgespike.feature.datainfo.domain.usecase

import com.knowledgespike.feature.datainfo.domain.repository.DataInfoRepository
import kotlinx.datetime.LocalDateTime

data class DataInfoUseCases(
    val getLastDataDataAddedUseCase: GetLastDataDataAddedUseCase
)


class GetLastDataDataAddedUseCase(val repository: DataInfoRepository) {

    operator fun invoke() : String? {
        return repository.getLatestDateAddedToDatabase()
    }

}