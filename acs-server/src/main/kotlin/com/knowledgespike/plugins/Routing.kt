package com.knowledgespike.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*

/**
 * Configures routing and exception handling for the application.
 *
 * This method sets up routing-related plugins and global exception handling mechanisms.
 * It installs the `StatusPages` plugin to handle exceptions and provide default responses
 * such as a 500 Internal Server Error with the error message when an unhandled exception occurs.
 * Additionally, it installs the `Resources` plugin to enable support for resource routing.
 *
 * This configuration centralizes application routing and ensures consistent error handling
 * behavior across the application.
 */
fun Application.configureRouting() {

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            this@configureRouting.log.error("Unhandled exception: ${cause.stackTraceToString()}")
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }

        status(HttpStatusCode.InternalServerError, HttpStatusCode.BadRequest) { call, status ->
            this@configureRouting.log.error("${status.value} ${status.description}: ${call.request.httpMethod.value} ${call.request.uri}")
        }

    }
    install(Resources)
}
