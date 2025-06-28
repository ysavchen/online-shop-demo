plugins {
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

val springBootVersion by extra("3.4.6")
val modelVersion by extra("1.0.0")
val springCloudVersion by extra("2024.0.1")

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
        mavenLocal()
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }
}

val clientSubprojects = subprojects.filter { it.name.contains("client") || it.name.contains("model") }
configure(clientSubprojects) {
    apply {
        plugin("io.spring.dependency-management")
    }

    dependencies {
        api("com.example:online-shop-model:$modelVersion")
        api(platform("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion"))
        testImplementation(kotlin("test-junit5"))
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}

configure(clientSubprojects) {
    apply {
        plugin("org.gradle.maven-publish")
        plugin("org.jetbrains.kotlin.kapt")
    }

    dependencies {
        kapt("org.springframework.boot:spring-boot-configuration-processor")
        kapt("org.springframework.boot:spring-boot-autoconfigure-processor")
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