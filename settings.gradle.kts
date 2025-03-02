pluginManagement {
    repositories {
        google()  // 🔥 Asegurar que Google esté sin restricciones
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CooperativaApp"
include(":app")
