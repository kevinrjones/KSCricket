package com.knowledgespike.plugins

import com.knowledgespike.feature.battingrecords.di.battingRecordsModule
import com.knowledgespike.feature.bowlingrecords.di.bowlingRecordsModule
import com.knowledgespike.feature.datainfo.di.dataInfoModule
import com.knowledgespike.feature.fieldingrecords.di.fieldingRecordsModule
import com.knowledgespike.feature.frontpage.di.frontPageModule
import com.knowledgespike.feature.partnershiprecords.di.partnershipRecordsModule
import com.knowledgespike.feature.player.di.playerModule
import com.knowledgespike.feature.recordsearch.di.mainSearchModule
import com.knowledgespike.feature.recordsummary.di.recordSummaryModule
import com.knowledgespike.feature.scorecard.di.scorecardModule
import com.knowledgespike.feature.teamrecords.di.teamRecordsModule
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

/**
 * Configures and loads the database connection pool using the provided JDBC URL, username, and password.
 * This method sets up the connection pool through the `DataSource` class and registers it as a singleton
 * in the dependency injection container.
 *
 * @param jdbcUrl The JDBC URL for the database connection.
 * @param userName The username for accessing the database.
 * @param password The password for the corresponding database username.
 */
fun loadDatabase(jdbcUrl: String,
                 userName: String,
                 password: String) = module {
    single { DataSource(jdbcUrl, userName, password) }
}

/**
 * Configures the Koin dependency injection framework for the application.
 *
 * This method installs the Koin plugin into the Ktor application and sets up
 * the required dependency injection modules. It includes modules for database
 * configuration, core application logic, and various domain-specific features
 * (e.g., player, scorecard, records, etc.).
 *
 * @param jdbcUrl The JDBC connection URL for the database.
 * @param userName The username used for database authentication.
 * @param password The password used for database authentication.
 */
fun Application.configureKoin(jdbcUrl: String, userName: String, password: String) {
    install(Koin) {
        slf4jLogger()

        modules(
            loadDatabase(jdbcUrl, userName, password),
            mainSearchModule,
            recordSummaryModule,
            battingRecordsModule,
            bowlingRecordsModule,
            fieldingRecordsModule,
            teamRecordsModule,
            partnershipRecordsModule,
            scorecardModule,
            playerModule,
            frontPageModule,
            dataInfoModule
        )
    }
}