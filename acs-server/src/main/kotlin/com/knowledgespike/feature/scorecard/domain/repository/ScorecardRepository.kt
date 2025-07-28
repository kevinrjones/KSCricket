package com.knowledgespike.feature.scorecard.domain.repository

import arrow.core.Either
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.values.CaId
import com.knowledgespike.feature.scorecard.domain.model.ScorecardDto

/**
 * Repository interface for fetching scorecard details of a match.
 * Provides methods to retrieve a scorecard using either a `CaId` or a combination of team names and a match date.
 */
interface ScorecardRepository {
    /**
     * Retrieves the scorecard details for a specific match based on a given match identifier.
     *
     * @param caId The unique identifier representing the match.
     * @return Either an error describing the failure reason or a `ScorecardDto` containing detailed match information.
     */
    fun getScorecard(caId: CaId): Either<Error, ScorecardDto>
    /**
     * Retrieves the scorecard for a match based on the provided team names and date.
     *
     * @param homeTeamName The name of the home team playing in the match.
     * @param awayTeamName The name of the away team playing in the match.
     * @param date The date of the match, typically formatted as "yyyy-MM-dd".
     * @return Either an error or the detailed scorecard information encapsulated in a `ScorecardDto`.
     */
    fun getScorecard(homeTeamName: String, awayTeamName: String, date: String): Either<Error, ScorecardDto>
}