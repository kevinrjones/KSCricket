import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.21"

    alias(libs.plugins.ktorPlugin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.versionUpdate)
    alias(libs.plugins.catalogUpdate)
    alias(libs.plugins.jooq)

}


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.jooq.codeGen)
        classpath(libs.mariadb)
    }
}


kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll("-Xcontext-receivers", "-Xconsistent-data-class-copy-visibility")
//        freeCompilerArgs.add("-Xcontext-receivers")
//        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }
    // replaces
//    kotlinOptions {
//        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers -Xconsistent-data-class-copy-visibility"
//    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

group = "com.kstats"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    // this flag (development) is set in build.properties
    // development =true
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.ktor.client.coreJvm)

    implementation(libs.ktor.client.apacheJvm)

    implementation(libs.hikariCP)
    implementation(libs.ktor.server.coreJvm)
    implementation(libs.ktor.server.hostCcmmon)
    implementation(libs.ktor.server.statusPages)
    implementation(libs.ktor.server.resourcesJvm)
    implementation(libs.ktor.server.authJvm)
    implementation(libs.ktor.server.jwtJvm)
    implementation(libs.ktor.server.csrf.jvm)
    implementation(libs.ktor.server.compressionJvm)
    implementation(libs.ktor.server.swaggerJvm)
    implementation(libs.ktor.server.callLoggingJvm)
    implementation(libs.ktor.server.callIdJvm)
    implementation(libs.ktor.server.metrics.micrometerJvm)
    implementation(libs.ktor.server.metricsJvm)
    implementation(libs.ktor.server.contetnNegotiationJvm)
    implementation(libs.ktor.server.kotlinx.jsonJvm)
    implementation(libs.ktor.server.nettyJvm)
    implementation(libs.micrometer.registry.prometheus)
    implementation(libs.ktor.networkTlsCertificate)
    implementation(libs.koin)
    implementation(libs.koin.logger)
    implementation(libs.mariadb)
    implementation(libs.kotlinxDatetime)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.jooq)
    implementation(libs.jooq.codeGen)
    implementation(libs.jooq.meta)

    implementation(libs.bundles.apache.poi)


    implementation(libs.logback)
    implementation(libs.logback.logstash)


    testImplementation(libs.ktor.server.testHost)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(kotlin("test"))
}



fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}
sourceSets {
    this.main {
        val generatedDir= layout.projectDirectory.dir("generated/jooq/kotlin")
        java.srcDir(generatedDir)
    }
}
jooq {
    val output = layout.buildDirectory.dir(".")
//    val dir = "${layout.projectDirectory}/../"
    configuration {
        basedir = "${output.get()}"
        jdbc {
            driver = "org.mariadb.jdbc.Driver"
            url = "jdbc:mariadb://localhost:3306/cricketarchive"
            user = "cricketarchive"
            password = "p4ssw0rd"
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                inputSchema = "cricketarchive"
                name = "org.jooq.meta.mariadb.MariaDBDatabase"
            }


            target {
                packageName = "com.knowledgespike.db"
                directory = "generated/jooq/kotlin"
                isClean = true
            }
        }
    }
}

tasks.compileKotlin {
    dependsOn(tasks["jooqCodegen"])
}
