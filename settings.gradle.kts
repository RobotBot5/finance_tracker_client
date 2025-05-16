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
include(":core:ui")
include(":features:manage_accounts")
include(":features:currency_choose")
include(":data:get_info")
include(":features:icon_choose")
include(":features:categories")
include(":data:categories")
include(":features:manage_categories")
include(":features:create_transfer")
include(":data:transactions")
include(":features:transactions")
include(":data:analytics")
include(":features:analytics")
include(":data:profile")
include(":features:profile")
