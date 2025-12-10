plugins {
    java  //fix for plugin org.hibernate.orm
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.11.3" apply false
    id("org.hibernate.orm") version "7.1.8.Final"
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.jpa") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
}

group = "com.example"
version = "1.0.0"
val dockerHubRepository = "ysavchen"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("tools.jackson.module:jackson-module-kotlin")

    // Postgres
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.13.2")

    // Reliability
    implementation("org.springframework.retry:spring-retry:2.0.12")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    // Kotlin wrapper for slf4j-api
    // SLF4J and Logback comes pre-configured with Spring Boot
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.13")
    implementation("com.github.loki4j:loki-logback-appender:2.0.1")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    // Dependency is needed for latency visualization
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    // Model
    implementation("com.example:online-shop-model:1.0.0")

    // Integration
    implementation("com.example:order-service-domain-kafka-client-starter:1.0.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

hibernate {
    enhancement {
        enableAssociationManagement = true
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    imageName = "$dockerHubRepository/${rootProject.name}:$version"
    environment = mapOf("BPE_SPRING_PROFILES_ACTIVE" to "docker")
}