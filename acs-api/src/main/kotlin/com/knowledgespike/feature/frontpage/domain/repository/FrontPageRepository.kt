package com.knowledgespike.feature.frontpage.domain.repository

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.frontpage.domain.model.MatchDetails

interface FrontPageRepository {
    fun getRecentMatches(): DatabaseResult<MatchDetails>
}