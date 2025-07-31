rootProject.name = "acs-server"


buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.gradle.toolchains:foojay-resolver:0.7.0")
    }
}
