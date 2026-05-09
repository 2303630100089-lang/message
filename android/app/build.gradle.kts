plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.pulsesphere.app"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.pulsesphere"
    minSdk = 26
    targetSdk = 35
    versionCode = 1
    versionName = "0.1.0"
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.15"
  }

  packaging {
    resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
  }
}

dependencies {
  implementation(project(":core:designsystem"))
  implementation(project(":core:domain"))
  implementation(project(":core:data"))
  implementation(project(":feature:auth"))
  implementation(project(":feature:chat"))
  implementation(project(":feature:feed"))
  implementation(project(":feature:profile"))

  implementation(platform(libs.compose.bom))
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime)
  implementation(libs.androidx.activity.compose)
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.tooling.preview)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)
  implementation(libs.coroutines.android)
  debugImplementation(libs.compose.ui.tooling)
}
