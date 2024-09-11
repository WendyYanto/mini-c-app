plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
//    id("scabbard.gradle") version "0.5.0"
    id("com.squareup.anvil") version "2.4.9"
}

android {
    namespace = "com.dev.feature_cart_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dev.feature_cart_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.ktx)
    implementation(libs.appcompact)
    implementation(libs.dagger.core)
    implementation(libs.material.component)
    kapt(libs.dagger.compiler)

    implementation(project(":core"))
    implementation(project(":domain_cart"))

    implementation(project(":data_product"))
    implementation(project(":data_order"))

    implementation(project(":feature_cart"))
}