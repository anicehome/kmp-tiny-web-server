rootProject.name = "kmp-tiny-web-server"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":core:base")
include(":core:database")
include(":core:datastore")
include(":core:domain")
include(":core:server")
include(":core:system")
include(":core:resource")
include(":feature:base")
include(":feature:file")
include(":feature:home")
include(":feature:service")
include(":feature:setting")