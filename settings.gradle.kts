pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Finance Tracker Client"
include(":app")
include(":data:authorize")
include(":core:common")
include(":features:authorize")
include(":features:root")
include(":features:bank_accounts")
include(":data:bank_accounts")
include(":core:remote")
include(":core:dependencies")
