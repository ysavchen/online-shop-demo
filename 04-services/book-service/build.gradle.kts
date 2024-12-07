plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.graalvm.buildtools.native") version "0.10.3" apply false
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("kapt") version "1.9.25"
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
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Postgres
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.8.3")
    kapt("org.hibernate.orm:hibernate-jpamodelgen:6.6.1.Final")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql:1.20.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    // Kotlin wrapper for slf4j-api
    // SLF4J and Logback comes pre-configured with Spring Boot
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("com.github.loki4j:loki-logback-appender:1.5.2")
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
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    imageName = "$dockerHubRepository/${rootProject.name}:$version"

    //Fix https://github.com/spring-projects/spring-boot/issues/41199
    docker {
        host = "//./pipe/dockerDesktopLinuxEngine"
    }
}