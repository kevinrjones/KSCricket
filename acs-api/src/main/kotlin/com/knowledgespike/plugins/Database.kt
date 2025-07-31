package com.knowledgespike.plugins

import com.knowledgespike.LoggerDelegate
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.network.sockets.*
import io.ktor.server.application.*
import java.sql.Connection
import java.sql.SQLException

/**
 * Object responsible for managing a database connection pool using HikariCP.
 * Provides access to database connections and allows monitoring of connection pool statistics.
 *
 * This object initializes a HikariCP connection pool with predefined properties such as JDBC URL,
 * username, password, prepared statement caching, and connection pool settings. These configurations
 * ensure optimized performance and efficient connection management.
 *
 * Features:
 * - Exposes a method to log details such as active, idle, and total connections in the pool.
 * - Provides access to individual database connections and the HikariDataSource instance
 *   for advanced use cases.
 *
 * Usage of this class requires proper exception handling for `SQLException` when acquiring
 * connections or interacting with the data source.
 */
class DataSource(jdbcUrl: String, userName: String, password: String) {
    private val log by LoggerDelegate()

    private val config = HikariConfig()
    private var ds: HikariDataSource

    init {
        config.jdbcUrl = jdbcUrl
        config.username = userName
        config.password = password
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        ds = HikariDataSource(config)
    }

    @Suppress("unused")
    fun debugLog(prefix: String) {
        log.debug("$prefix - active connections: ${ds.hikariPoolMXBean?.activeConnections}")
        log.debug("$prefix - idle connections: ${ds.hikariPoolMXBean?.idleConnections}")
        log.debug("$prefix - total connections: ${ds.hikariPoolMXBean?.totalConnections}")
    }

    @Suppress("unused")
    @get:Throws(SQLException::class)
    val connection: Connection
        get() = ds.connection

    @get:Throws(SQLException::class)
    val dataSource: HikariDataSource
        get() = ds
}