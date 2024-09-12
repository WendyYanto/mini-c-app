plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("com.squareup.anvil") version "2.4.9"
}

android {
    namespace = "com.dev.feature.cart"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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

    sourceSets {
        getByName("main") {
            java {
                srcDirs("build/anvil/")
            }
        }
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
    implementation(project(":data_misc"))

    implementation(project(":annotation"))
    anvil(project(":anvil_compiler_api"))
}