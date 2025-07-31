package com.knowledgespike.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

/**
 * Configures HTTP-specific features of the application.
 *
 * This method installs the `Compression` plugin to enable response compression,
 * improving communication efficiency between the server and clients.
 * Additionally, it provides a Swagger UI interface accessible at the specified path
 * for exploring and testing the application's API in a developer-friendly way.
 *
 * The Swagger UI route is defined as "openapi", enabling users to access the API
 * documentation and interact with API endpoints directly from their browser.
 */
fun Application.configureHTTP() {
    install(Compression)
    routing {
        swaggerUI(path = "openapi")
    }
}
