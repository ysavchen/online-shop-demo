plugins {
    java
    id("io.gatling.gradle") version "3.14.9.5"
}

group = "org.example"
version = "1.0.0"

val javaVersion = 21

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

repositories {
    mavenCentral()
}