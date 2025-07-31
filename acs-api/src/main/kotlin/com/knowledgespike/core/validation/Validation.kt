package com.knowledgespike.core.validation

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.core.type.values.MatchType.Companion.invoke
import com.knowledgespike.core.type.values.TeamId
import com.knowledgespike.core.type.values.TeamId.Companion.invoke
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters

/**
 * Validates the input parameters, processes them, and returns a result encapsulating either a list of errors or validated search parameters.
 *
 * @param matchType A nullable string representing the match type to be validated.
 * @param matchSubType A nullable string representing the match subtype to be validated.
 * @param teamId A nullable integer representing the team ID to be validated.
 * @param opponentsId A nullable integer representing the opponent's team ID to be validated.
 * @return Either a non-empty list of errors if validation fails, or a valid `ValidatedSearchParameters` object if validation succeeds.
 */
fun validateParameters(
    matchType: String?,
    matchSubType: String?,
    teamId: Int?,
    opponentsId: Int?,
): Either<NonEmptyList<Error>, ValidatedSearchParameters> = either {
    zipOrAccumulate(
        { MatchType(matchType) },
        { MatchType(matchSubType) },
        { TeamId(teamId) },
        { TeamId(opponentsId) },
    ) { matchType, matchSubType, teamId, opponentsId ->
        ValidatedSearchParameters(
            matchType = matchType,
            teamId = teamId,
            opponentsId = opponentsId,
            matchSubType = matchSubType
        )
    }
}