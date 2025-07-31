package com.knowledgespike.feature.recordsummary.presentation

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.fold
import arrow.core.raise.zipOrAccumulate
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.core.type.values.CountryId
import com.knowledgespike.core.type.values.GroundId
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.core.type.values.TeamId
import com.knowledgespike.feature.recordsummary.usecase.SummaryUseCases
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Configures the routing for the "records summary" API endpoint in the application.
 *
 * This method defines an authenticated route under the `/api` path to handle HTTP
 * GET requests for retrieving summary data for records based on various parameters.
 *
 * The endpoint requires the following path parameters:
 * - `matchType`: The type of the match (e.g., test match, one-day international).
 * - `teamId`: The ID of the team.
 * - `opponentsId`: The ID of the opposing team.
 * - `groundId`: The ID of the ground where the match took place.
 * - `hostCountryId`: The ID of the host country.
 *
 * The parameters are validated using `validateParameters`, which returns either
 * validated data or a list of errors. If validation fails, the response is a
 * `400 Bad Request` with the validation errors. Otherwise, the validated data is
 * processed using the `summaryUseCase` to fetch the required summary information.
 *
 * The successful response contains the requested summary in a `200 OK`, while any
 * issues in processing are responded to as `400 Bad Request` with appropriate error
 * details.
 *
 * Authentication is required via the scope identified by `AUTH_JWT_READ`.
 */
fun Application.recordSummaryRoute() {

    // todo: All these parameters have to be validated, same for the other controllers
    val summaryUseCase by inject<SummaryUseCases>()
    routing {
        authenticate(AUTH_JWT_READ) {
            route("/api") {
                get("/recordssummary/{matchType}/{teamId}/{opponentsId}/{groundId}/{hostCountryId}") {
                    val matchType = call.parameters["matchType"]!!
                    val teamId = call.parameters["teamId"]!!.toInt()
                    val opponentsId = call.parameters["opponentsId"]!!.toInt()
                    val groundId = call.parameters["groundId"]!!.toInt()
                    val hostCountryId = call.parameters["hostCountryId"]!!.toInt()

                    val maybeValidated: Either<NonEmptyList<Error>, ValdidatedTypes> =
                        validateParameters(matchType, teamId, opponentsId, groundId, hostCountryId)



                    maybeValidated.fold(
                        ifLeft = {
                            val env = Envelope.failure(it.toString())
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        ifRight = {
                            fold(
                                block = {
                                    summaryUseCase.summaryUseCase(
                                        it.matchType,
                                        it.teamId,
                                        it.opponentsId,
                                        it.groundId,
                                        it.countryId
                                    )
                                },
                                recover = {
                                    val env = Envelope.failure(it.toString())
                                    call.respond(HttpStatusCode.BadRequest, env)
                                },
                                transform = { summary ->
                                    val env = Envelope.success(summary)
                                    call.respond(HttpStatusCode.OK, env)
                                },
                            )
                        })
                }
            }
        }
    }
}

/**
 * Validates the provided parameters for use in constructing a `ValdidatedTypes` instance.
 *
 * This function ensures that the input parameters conform to the expected constraints and types.
 * If all parameters are valid, it produces a `ValdidatedTypes` object.
 * If any parameter is invalid, it provides a collection of errors detailing the issues.
 *
 * @param matchType The type of the match, expected to be a non-null string matching allowed match types.
 * @param teamId The ID of the team, expected to be a non-negative integer.
 * @param opponentsId The ID of the opposing team, expected to be a non-negative integer.
 * @param groundId The ID of the ground, expected to be a non-negative integer.
 * @param hostCountryId The ID of the host country, expected to be a non-negative integer.
 * @return Either a collection of validation errors (`NonEmptyList<Error>`) if validation fails,
 *         or a `ValdidatedTypes` instance containing the validated parameters if validation succeeds.
 */
internal fun validateParameters(
    matchType: String?,
    teamId: Int?,
    opponentsId: Int?,
    groundId: Int?,
    hostCountryId: Int?,
): Either<NonEmptyList<Error>, ValdidatedTypes> =


    either {
        zipOrAccumulate(
            { MatchType(matchType) },
            { TeamId(teamId) },
            { TeamId(opponentsId) },
            { GroundId(groundId) },
            { CountryId(hostCountryId) },
        ) { matchType, teamId, opponentsId, groundId, countryId ->
            ValdidatedTypes(matchType, teamId, opponentsId, groundId, countryId)
        }
    }

/**
 * Represents a collection of validated parameters used in specific domain operations.
 *
 * This data class encapsulates multiple validated types including:
 * - MatchType: A specific type of match, which must adhere to predefined valid types.
 * - TeamId: The identifier for a team, ensuring it is valid.
 * - OpponentsId: The identifier for the opposing team, which is also validated.
 * - GroundId: The identifier for the ground where the match takes place, ensuring it is valid.
 * - CountryId: The identifier for the host country, which is validated to ensure correctness.
 *
 * Each component of this class represents a type-safe and validated input, ensuring
 * reliable operation within the application. Instances of this class are typically
 * created after validating parameters through a designated validation function.
 */
internal data class ValdidatedTypes(
    val matchType: MatchType,
    val teamId: TeamId,
    val opponentsId: TeamId,
    val groundId: GroundId,
    val countryId: CountryId
)