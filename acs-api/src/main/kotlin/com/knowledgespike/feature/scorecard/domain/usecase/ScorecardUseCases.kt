package com.knowledgespike.feature.scorecard.domain.usecase

import arrow.core.Either
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.values.CaId
import com.knowledgespike.feature.scorecard.domain.model.ScorecardDto
import com.knowledgespike.feature.scorecard.domain.repository.ScorecardRepository

/**
 * Parent class containing use case instances for retrieving scorecard details
 * based on either a match identifier or a combination of team names and match date.
 *
 * @property getScorecardWithIdUseCase Use case to fetch scorecard details using a unique match identifier.
 * @property getScorecardWithNamesUseCase Use case to fetch scorecard details using team names and match date.
 */
data class ScorecardUseCases(
    val getScorecardWithIdUseCase: GetScorecardWithIdUseCase,
    val getScorecardWithNamesUseCase: GetScorecardWithNamesUseCase
)

/**
 * Use case class for retrieving the scorecard of a match using its unique identifier.
 *
 * The primary responsibility of this use case is to facilitate the fetching of match scorecard
 * details from a repository based on a given match identifier (`CaId`).
 *
 * @property repository An instance of `ScorecardRepository` used to access match scorecard details.
 */
class GetScorecardWithIdUseCase(val repository: ScorecardRepository) {
    /**
     * Retrieves the scorecard for a specific match based on its unique identifier.
     *
     * @param matchId The unique identifier of the match for which the scorecard is being requested.
     * @return An `Either` instance with an `Error` if the retrieval fails, or a `ScorecardDto` containing detailed match information if successful.
     */
    operator fun invoke(matchId: CaId) : Either<Error, ScorecardDto> {
        return repository.getScorecard(matchId)
    }
}

/**
 * Use case for retrieving a scorecard for a match based on the names of the home team,
 * the away team, and the match date.
 *
 * This use case interacts with the `ScorecardRepository` to fetch detailed scorecard
 * information, represented by `ScorecardDto`.
 *
 * @property repository An instance of `ScorecardRepository` used to fetch the scorecard data.
 */
class GetScorecardWithNamesUseCase(val repository: ScorecardRepository) {
    /**
     * Invokes the use case to retrieve the scorecard of a match based on the provided team names and match date.
     *
     * @param homeTeamName The name of the home team participating in the match.
     * @param awayTeamName The name of the away team participating in the match.
     * @param date The date of the match, typically formatted as "yyyy-MM-dd".
     * @return An `Either` containing an `Error` object in case of failure or a `ScorecardDto` with the match details in case of success.
     */
    operator fun invoke(homeTeamName: String, awayTeamName: String, date: String) : Either<Error, ScorecardDto> {
        return repository.getScorecard(homeTeamName, awayTeamName, date)
    }
}
