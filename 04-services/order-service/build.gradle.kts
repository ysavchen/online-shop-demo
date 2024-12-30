plugins {
    java  //fix for plugin org.hibernate.orm
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.4" apply false
    id("org.hibernate.orm") version "6.6.4.Final"
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
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.0")
    kapt("org.hibernate.orm:hibernate-jpamodelgen:6.6.4.Final")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.redis:testcontainers-redis:2.2.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("com.github.loki4j:loki-logback-appender:1.5.2")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // Model
    implementation("com.example:online-shop-model:1.0.0")

    // Integration
    implementation("com.example:book-service-rest-client-starter:1.0.0")
    implementation("com.example:order-service-domain-kafka-client-starter:1.0.0")
    implementation("com.example:delivery-service-request-kafka-client-starter:1.0.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
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
}