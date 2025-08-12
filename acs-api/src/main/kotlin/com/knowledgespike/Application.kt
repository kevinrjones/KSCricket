package com.knowledgespike

import com.knowledgespike.feature.battingrecords.presentation.routeBattingRecords
import com.knowledgespike.feature.bowlingrecords.presentation.routeBowlingRecords
import com.knowledgespike.feature.datainfo.presentation.routeDataInfo
import com.knowledgespike.feature.fieldingrecords.presentation.routeFieldingRecords
import com.knowledgespike.feature.frontpage.presentation.routeFrontPage
import com.knowledgespike.feature.partnershiprecords.presentation.routePartnershipRecords
import com.knowledgespike.feature.partnershiprecords.presentation.routePlayers
import com.knowledgespike.feature.partnershiprecords.presentation.routeScorecard
import com.knowledgespike.feature.recordsearch.presentation.routeSearchParameters
import com.knowledgespike.feature.recordsummary.presentation.recordSummaryRoute
import com.knowledgespike.feature.teamrecords.presentation.routeTeamRecords
import com.knowledgespike.plugins.*
import io.ktor.server.application.*
import org.jooq.SQLDialect
import org.slf4j.LoggerFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

val DIALECT = SQLDialect.MARIADB

fun Application.module() {


    // you can add the cacert to the keystore that this JVM is using, or you can use this code
    // setupDevSSL is commented out when I've successfully added the cacert (~/Dropbox/project/certificates/cacert.pem)
    // to the keystore (see the readme)
    // If you get SSL errors then it's possible that the cacert is either not in the keystore or it has expired and
    // needs updating
    if (System.getProperty("io.ktor.development")?.toBoolean() == true) {
        LoggerFactory.getLogger(Application::class.java).debug("Running in development mode")
        // Only for development/testing environments
//        setupDevSSL()
    }


    val jwksUrl = environment.config.property("jwt.jwksUrl").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

    configureKoin(
        environment.config.property("jdbc.url").getString(),
        environment.config.property("jdbc.username").getString(),
        environment.config.property("jdbc.password").getString(),
    )
    configureMonitoring()
    configureSecurity(jwksUrl, issuer, environment.config.property("jwt.realm").getString())
//    configureHTTP()
    configureSerialization()
    routeSearchParameters()
    recordSummaryRoute()
    routeBattingRecords()
    routeBowlingRecords()
    routeFieldingRecords()
    routeTeamRecords()
    routePartnershipRecords()
    routeScorecard()
    routePlayers()
    routeFrontPage()
    routeDataInfo()
    configureRouting()
}

private fun setupDevSSL() {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate>? = null
        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    })

    try {
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}


