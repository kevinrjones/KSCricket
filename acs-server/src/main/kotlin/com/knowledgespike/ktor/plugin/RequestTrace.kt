package com.knowledgespike.ktor.plugin

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import org.slf4j.Logger
import org.slf4j.event.Level


/**
 * Configuration class for request tracing.
 *
 * This class allows setting tracing preferences, such as the log level
 * and the logger instance to be used for logging request trace information.
 *
 * @property level Specifies the logging level for request tracing. Default is `Level.ERROR`.
 * @property logger The logger instance used for emitting trace logs. If null, logging is disabled.
 */
class RequestTraceConfig {
    var level: Level = Level.ERROR

    var logger: Logger? = null
}

/**
 * A Ktor application plugin that provides functionality for logging request and response
 * details for monitoring and debugging purposes.
 *
 * The plugin logs important aspects such as request URLs, request headers, and response headers
 * at various configurable logging levels (`Level.ERROR`, `Level.WARN`, `Level.INFO`, `Level.DEBUG`, `Level.TRACE`).
 * The logging behavior can be customized through the associated `RequestTraceConfig`.
 *
 * This plugin is particularly useful for tracing HTTP requests and responses during application
 * development and debugging, or for monitoring purposes in production environments.
 *
 * Configuration of the plugin includes:
 * - Setting the desired log level to control the verbosity of the logs.
 * - Providing a custom logger implementation or defaulting to the application's logger.
 *
 * The plugin processes each incoming request and outgoing response and logs the relevant information.
 */
val RequestTrace = createApplicationPlugin(name = "RequestTracePlugin", ::RequestTraceConfig) {

    val log = pluginConfig.logger ?: application.log

    fun log(message: String) = when (pluginConfig.level) {
        Level.ERROR -> log.error(message)
        Level.WARN -> log.warn(message)
        Level.INFO -> log.info(message)
        Level.DEBUG -> log.debug(message)
        Level.TRACE -> log.trace(message)
    }



    onCall { call ->
        call.request.origin.apply {
            log("Request URL: $scheme://$localHost:$localPort$uri")
        }
        call.request.headers.apply {
            this.names().forEach {
                log("[REQUEST] $it: ${this[it]}")
            }
        }
        call.response.headers.apply {
            this.allValues().names().forEach {
                log("[RESPONSE] $it: ${this[it]}")
            }
        }
    }
}