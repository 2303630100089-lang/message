pluginManagement {
  repositories {
    google()
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

rootProject.name = "pulsesphere-android"

include(
  ":app",
  ":core:common",
  ":core:designsystem",
  ":core:data",
  ":core:domain",
  ":feature:auth",
  ":feature:chat",
  ":feature:feed",
  ":feature:profile"
)
