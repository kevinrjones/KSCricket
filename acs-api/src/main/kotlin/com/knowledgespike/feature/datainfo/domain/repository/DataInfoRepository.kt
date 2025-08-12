package com.knowledgespike.feature.datainfo.domain.repository

import kotlinx.datetime.LocalDateTime

interface DataInfoRepository {
    fun getLatestDateAddedToDatabase(): String?
}