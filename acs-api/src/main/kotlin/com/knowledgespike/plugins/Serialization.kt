package com.knowledgespike.plugins

import com.knowledgespike.core.serialization.csv
import com.knowledgespike.core.serialization.xls
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

/**
 * Configures content serialization for the application by installing the ContentNegotiation plugin.
 * This method supports multiple serialization formats including JSON, CSV, and XLS.
 *
 * - JSON serialization supports options like pretty printing and lenient parsing to make the data output
 *   more readable and flexible.
 * - CSV serialization allows customization by specifying a separator. The default separator is overridden to `|`.
 * - XLS serialization enables handling data in Microsoft Excel-compatible format.
 *
 * These configurations ensure the application can handle diverse content formats for both incoming
 * and outgoing data, expanding its compatibility with different data exchange needs.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {

        json(Json {
            prettyPrint = true
            isLenient = true
        })
        csv(separator = "|")
        xls()
    }
}


