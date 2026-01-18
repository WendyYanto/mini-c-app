plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(libs.compiler.api)
    implementation(libs.compiler.utils)
    implementation(libs.google.auto.service)
    kapt(libs.google.auto.service)

    implementation(project(":annotation"))
    implementation(libs.kotlinpoet)
    implementation(libs.dagger.core)
}