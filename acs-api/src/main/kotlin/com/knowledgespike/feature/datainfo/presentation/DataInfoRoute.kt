package com.knowledgespike.feature.datainfo.presentation

import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.feature.datainfo.domain.usecase.DataInfoUseCases
import com.knowledgespike.feature.datainfo.domain.usecase.GetLastDataDataAddedUseCase
import com.knowledgespike.feature.frontpage.domain.usecase.FrontPageUseCases
import com.knowledgespike.feature.frontpage.domain.usecase.GetRecentMatchesUseCase
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private const val OVERALL_ROUTE = "/api/datainfo/getlastdatematchesadded"

fun Application.routeDataInfo() {
    val dataInfoUseCases by inject<DataInfoUseCases>()

    routing {
        authenticate(AUTH_JWT_READ) {
            route(OVERALL_ROUTE) {
                get() {
                    handleDataInfoUseCase(
                        getLastDataDataAddedUseCase = dataInfoUseCases.getLastDataDataAddedUseCase,
                        call = call
                    )
                }
            }
        }
    }
}


private suspend fun handleDataInfoUseCase(
    getLastDataDataAddedUseCase: GetLastDataDataAddedUseCase,
    call: ApplicationCall
) {
    val data = getLastDataDataAddedUseCase()
    call.respond(HttpStatusCode.OK, Envelope.success(data))
}
