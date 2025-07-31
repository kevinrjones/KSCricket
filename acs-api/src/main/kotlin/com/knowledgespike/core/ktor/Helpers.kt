package com.knowledgespike.core.ktor

import arrow.core.NonEmptyList
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.json.Envelope
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*


/**
 * Retrieves the value of a query parameter from the request, or returns a default value
 * if the parameter is not present or cannot be converted to an integer.
 *
 * @param param The name of the query parameter to retrieve.
 * @param default The default value to return if the parameter is not present or invalid.
 * @return The integer value of the query parameter, or the default value if not present or invalid.
 */
fun ApplicationCall.getQueryParamOrDefault(param: String, default: Int): Int =
    this.request.queryParameters[param]?.toIntOrNull() ?: default

/**
 * Handles validation errors by responding with a bad request status and an error message.
 *
 * @param errors A non-empty list of validation errors that occurred.
 * @param call The application call to respond to with the error message.
 */
suspend fun handleValidationErrors(errors: NonEmptyList<Error>, call: ApplicationCall) {
    val message = errors.map { it.message }.joinToString(", ")
    call.application.log.error("Validation errors: $message")
    call.respond(HttpStatusCode.BadRequest, Envelope.failure("Some of the data was not valid: $message"))
}

