import org.gradle.api.provider.Property

interface AppAnvilExtension {
    val generateDaggerFactories: Property<Boolean>
}
