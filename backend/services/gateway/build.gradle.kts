plugins {
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
  java
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

dependencies {
  implementation(libs.spring.boot.webflux)
  implementation(libs.spring.boot.security)
  implementation(libs.spring.boot.oauth2.resource)
  implementation(libs.spring.boot.redis)
  implementation(libs.spring.boot.actuator)
  implementation(libs.spring.boot.validation)
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
  useJUnitPlatform()
}
