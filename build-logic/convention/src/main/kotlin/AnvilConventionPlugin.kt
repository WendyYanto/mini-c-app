import com.squareup.anvil.plugin.AnvilExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies

class AnvilConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.squareup.anvil")

            val anvilExtension = project.extensions.getByType(AnvilExtension::class.java)
            anvilExtension.trackSourceFiles.set(true)

            val anvilConfigExtension = extensions.create<AppAnvilExtension>("anvilConfig")
            anvilConfigExtension.generateDaggerFactories.convention(false)

            afterEvaluate {
                anvilExtension.generateDaggerFactories.set(anvilConfigExtension.generateDaggerFactories)
            }

            dependencies {
                anvil(project(":annotation_processor"))
                implementation(project(":annotation"))
            }
        }
    }
}
