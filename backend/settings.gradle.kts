pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

rootProject.name = "pulsesphere-backend"

include(
  ":services:gateway",
  ":services:auth",
  ":services:messaging",
  ":services:realtime",
  ":services:media",
  ":services:search",
  ":services:notifications",
  ":libs:shared-kernel",
  ":libs:security",
  ":libs:observability"
)
