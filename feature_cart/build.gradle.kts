plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("minicapp.common")
    id("minicapp.ksp")
}

buildFeatures {
    include {
        useAnvil = true
        generateDaggerFactories = true
    }
}

android {
    namespace = "com.dev.feature.cart"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(libs.ktx)
    implementation(libs.appcompact)
    implementation(libs.dagger.core)
    implementation(libs.material.component)

    implementation(project(":core"))
    implementation(project(":domain_cart"))

    implementation(project(":data_product"))
    implementation(project(":data_order"))
}