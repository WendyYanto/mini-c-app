plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlin.poet.ksp)
    implementation(libs.google.auto.service)
    ksp(libs.auto.service.ksp)

    implementation(project(":annotation"))
}
