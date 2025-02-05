plugins {
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "1.9.25"
}

group = "com.example"
version = "1.0.0"

val springBootVersion by extra("3.4.1")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.apache.commons:commons-lang3")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        mavenLocal()
    }

    publications {
        create<MavenPublication>("online-shop-model") {
            from(components["java"])
        }
    }
}