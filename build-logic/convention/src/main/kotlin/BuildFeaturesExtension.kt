import com.squareup.anvil.plugin.AnvilExtension
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import javax.inject.Inject

abstract class BuildFeaturesExtension @Inject constructor(
    val objectFactory: ObjectFactory,
    val project: Project
) {

    private fun Project.configureDi(
        buildFeatures: BuildFeatures
    ) {
        if (buildFeatures.useMetro) {
            pluginManager.apply(pluginFromVersionCatalog("metro"))
            val metroExtension = project.extensions.getByType(MetroPluginExtension::class.java)

            with(metroExtension) {
                enabled.set(true)
                debug.set(true)

                interop {
                    contributesTo.add("com/squareup/anvil/annotations/ContributesTo")
                    contributesBinding.add("com/squareup/anvil/annotations/ContributesBinding")

                    mapKey.add("dagger/MapKey")

                    // custom
                    inject.add("com/dev/annotation/MetroInject")
                }
            }

            dependencies {
                ksp(project(":annotation_processor"))
                implementation(project(":annotation"))
                implementation(libs.findLibrary("dagger.core"))
                implementation(libs.findLibrary("anvil.annotations"))
            }
            return
        }

        if (buildFeatures.useAnvil) {
            pluginManager.apply(pluginFromVersionCatalog("anvil"))

            val anvilExtension = project.extensions.getByType(AnvilExtension::class.java)
            anvilExtension.trackSourceFiles.set(true)
            anvilExtension.generateDaggerFactories.set(buildFeatures.generateDaggerFactories)

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

    fun include(action: Action<BuildFeatures>) {
        val buildFeatures = objectFactory.newInstance(BuildFeatures::class.java)
        action.execute(buildFeatures)

        project.configureDi(buildFeatures)
    }
}

abstract class BuildFeatures {
    abstract var generateDaggerFactories: Boolean
    abstract var useAnvil: Boolean
    abstract var useMetro: Boolean
}