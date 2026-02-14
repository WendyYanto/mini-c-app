plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.dagger.core)
    implementation(libs.metro.runtime)
}