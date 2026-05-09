plugins {
    kotlin("jvm")
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
}
