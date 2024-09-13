plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    api(libs.compiler.api)
    implementation(libs.compiler.utils)
    compileOnly(libs.auto.service.annotations)
    kapt(libs.auto.service)

    implementation(project(":annotation"))
    implementation(libs.kotlinpoet)
    implementation(libs.dagger.core)
}