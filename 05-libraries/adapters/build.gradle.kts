plugins {
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.2.21"
}

val springBootVersion by extra("4.0.0")
val modelVersion by extra("1.0.0")
val springCloudVersion by extra("2025.1.0")

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
            freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
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
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-configuration-processor")
        implementation("org.springframework.boot:spring-boot-autoconfigure-processor")
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