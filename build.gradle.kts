// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    id("scabbard.gradle") version "0.5.0"
    id("com.squareup.anvil") version "2.4.9"
}

scabbard {
    enabled = true
    fullBindingGraphValidation = true
}

allprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.github.kittinunf.result" && requested.name == "result" && requested.version == "3.0.0") {
                useVersion("3.0.1")
                because("Transitive dependency of Scabbard, currently not available on mavenCentral()")
            }
        }
    }
}