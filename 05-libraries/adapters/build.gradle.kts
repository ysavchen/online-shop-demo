plugins {
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.25"
}

allprojects {
    group = "com.example"
    version = "1.0.0"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }
}

configure(subprojects.filter { it.name.contains("client") }) {
    apply {
        plugin("org.gradle.maven-publish")
        plugin("io.spring.dependency-management")
    }

    dependencies {
        testImplementation(kotlin("test-junit5"))
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.4")
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
            create<MavenPublication>("adapters") {
                from(components["java"])
            }
        }
    }
}