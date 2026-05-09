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
  implementation(libs.spring.boot.websocket)
  implementation(libs.spring.boot.redis)
  implementation(libs.spring.boot.kafka)
  implementation(libs.grpc.netty)
  implementation(libs.grpc.protobuf)
  implementation(libs.grpc.stub)
  implementation(libs.spring.boot.actuator)
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
  useJUnitPlatform()
}
