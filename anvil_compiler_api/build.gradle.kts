plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    api("com.squareup.anvil:compiler-api:2.4.7")
    implementation("com.squareup.anvil:compiler-utils:2.4.7")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0")
    kapt("com.google.auto.service:auto-service:1.0")

    implementation(project(":annotation"))
}