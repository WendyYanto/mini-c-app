import org.gradle.api.provider.Property

interface AnvilConfigExtension {
    val generateDaggerFactories: Property<Boolean>
}
