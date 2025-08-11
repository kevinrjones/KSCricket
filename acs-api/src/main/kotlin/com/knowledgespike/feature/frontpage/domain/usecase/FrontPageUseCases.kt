package com.knowledgespike.feature.frontpage.domain.usecase

import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.feature.frontpage.domain.model.MatchDetails
import com.knowledgespike.feature.frontpage.domain.repository.FrontPageRepository

data class FrontPageUseCases(
    val getRecentMatches: GetRecentMatchesUseCase
)


class GetRecentMatchesUseCase(val repository: FrontPageRepository) {

    operator fun invoke() : DatabaseResult<MatchDetails> {
        return repository.getRecentMatches()
    }

}