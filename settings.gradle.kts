pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    versionCatalogs {
        create("versionLibs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Mini c-app"
include(":app")
include(":core")
include(":data_user")
include(":data_product")
include(":data_order")
