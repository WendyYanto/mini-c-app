import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.plugin.use.PluginDependency
import java.util.Optional

internal val Project.libs
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

private fun Optional<Provider<PluginDependency>>.pluginId(): String {
    if (!isPresent) throw IllegalArgumentException("Plugin can't be empty : $this")
    val pluginDependency = get().get()
    return pluginDependency.pluginId
}

internal fun Project.pluginFromVersionCatalog(plugin: String) =
    libs.findPlugin(plugin).pluginId()

internal fun Optional<Provider<MinimalExternalModuleDependency>>.asString(): String {
    val dependency = get().get()
    val group = dependency.module.group
    val name = dependency.module.name
    val version = dependency.version
    return "$group:$name:$version"
}

internal fun DependencyHandlerScope.implementation(dependency: Optional<Provider<MinimalExternalModuleDependency>>) {
    add("implementation", dependency.get())
}

internal fun DependencyHandlerScope.implementation(dependency: Dependency) {
    add("implementation", dependency)
}

internal fun DependencyHandlerScope.implementation(dependency: Project) {
    add("implementation", dependency)
}

internal fun DependencyHandlerScope.anvil(dependency: Project) {
    add("anvil", dependency)
}

internal fun DependencyHandlerScope.ksp(dependency: Project) {
    add("ksp", dependency)
}