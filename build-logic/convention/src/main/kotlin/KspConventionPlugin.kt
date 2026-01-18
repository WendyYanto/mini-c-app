import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class KspConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(pluginFromVersionCatalog("google.ksp"))

            dependencies {
                ksp(project(":ksp_annotation_processor"))
                implementation(project(":annotation"))
            }
        }
    }
}
