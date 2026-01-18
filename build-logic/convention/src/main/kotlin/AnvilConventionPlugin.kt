import com.squareup.anvil.plugin.AnvilExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AnvilConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.squareup.anvil")

            val anvilExtension = project.extensions.getByType(AnvilExtension::class.java)
            anvilExtension.trackSourceFiles.set(true)

            val anvilConfigExtension = extensions.create<AnvilConfigExtension>("anvilConfig")
            anvilConfigExtension.generateDaggerFactories.convention(false)

            afterEvaluate {
                anvilExtension.generateDaggerFactories.set(anvilConfigExtension.generateDaggerFactories)
            }

            // Reference #1: https://github.com/square/anvil/issues/733
            // Reference #2: https://www.zacsweers.dev/preparing-for-k2/
            // Latest Anvil doesn't support K2 compiler yet due to its compiler behaviour changes
            // Without enabling this, our code generator doesn't work and not producing any result in build/anvil
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    progressiveMode.set(false)
                    languageVersion.set(KotlinVersion.KOTLIN_1_9)
                }
            }
            tasks.withType<KaptGenerateStubsTask>().configureEach {
                compilerOptions {
                    progressiveMode.set(false)
                    languageVersion.set(KotlinVersion.KOTLIN_1_9)
                }
            }

            dependencies {
                anvil(project(":annotation_processor"))
                implementation(project(":annotation"))
                implementation(libs.findLibrary("dagger.core"))
            }
        }
    }
}
