package com.knowledgespike.feature.partnershiprecords.presentation


import arrow.core.raise.either
import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.core.type.values.CaId
import com.knowledgespike.feature.scorecard.domain.usecase.ScorecardUseCases
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import org.koin.ktor.ext.inject

private const val BY_ID_ROUTE = "/api/scorecard/byid"

private const val ROUTE_PARAMETERS = "/{matchId}"

fun Application.routeScorecard() {

    val scorecardUseCases by inject<ScorecardUseCases>()

    routing {
        authenticate(AUTH_JWT_READ) {
            route(BY_ID_ROUTE) {
                get(ROUTE_PARAMETERS) {

                    val maybeCaId = either { CaId(call.parameters["matchId"]) }

                    maybeCaId.fold(
                        ifLeft = { errors ->
                            log.error("errors.message")
                            // todo: log all failures
                            val env = Envelope.failure(errors.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        ifRight = { matchId ->
                            val data = scorecardUseCases.getScorecardWithIdUseCase(matchId)
                            data.fold(
                                ifLeft = {
                                    call.respond(HttpStatusCode.BadRequest, it.message)
                                },
                                ifRight = {
                                    call.respond(HttpStatusCode.OK, Envelope.success(it))
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}
