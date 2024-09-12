plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("scabbard.gradle") version "0.5.0"
}

android {
    namespace = "com.dev.wenn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dev.wenn"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            isJniDebuggable = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
    implementation(project(":data_user"))
    implementation(project(":data_product"))
    implementation(project(":data_order"))
    implementation(project(":data_misc"))
    implementation(project(":domain_cart"))

    implementation(project(":feature_cart"))
}