pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(
        RepositoriesMode.FAIL_ON_PROJECT_REPOS
    )

    repositories {
        maven {
            url = uri(
                "https://maven-central.storage-download.googleapis.com/maven2"
            )
        }

        maven {
            url = uri(
                "https://maven.aliyun.com/repository/central"
            )
        }

        google()
        mavenCentral()
    }
}

rootProject.name = "BluKitchen"
include(":app")
