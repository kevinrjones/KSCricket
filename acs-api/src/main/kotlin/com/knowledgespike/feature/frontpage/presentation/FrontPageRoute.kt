package com.knowledgespike.feature.frontpage.presentation

import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.feature.frontpage.domain.usecase.FrontPageUseCases
import com.knowledgespike.feature.frontpage.domain.usecase.GetRecentMatchesUseCase
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private const val OVERALL_ROUTE = "/api/frontpage/getrecentmatches"

fun Application.routeFrontPage() {
    val frontPageUseCases by inject<FrontPageUseCases>()

    routing {
        authenticate(AUTH_JWT_READ) {
            route(OVERALL_ROUTE) {
                get() {
                    handleGetRecentMatchesUseCase(
                        getRecentMatchesUseCase = frontPageUseCases.getRecentMatches,
                        call = call
                    )
                }
            }
        }
    }
}


private suspend fun handleGetRecentMatchesUseCase(
    getRecentMatchesUseCase: GetRecentMatchesUseCase,
    call: ApplicationCall
) {
    val data = getRecentMatchesUseCase()
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}
