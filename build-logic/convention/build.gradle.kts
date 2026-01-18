import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    compileOnly(libs.anvil.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)

    implementation(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("anvilConvention") {
            id = "minicapp.anvil"
            implementationClass = "AnvilConventionPlugin"
        }
        register("kspConvention") {
            id = "minicapp.ksp"
            implementationClass = "KspConventionPlugin"
        }
    }
}
