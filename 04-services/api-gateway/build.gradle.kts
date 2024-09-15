plugins {
    java
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.graalvm.buildtools.native") version "0.10.2"
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
}

extra["springCloudVersion"] = "2023.0.3"

dependencies {
    // Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.github.loki4j:loki-logback-appender:1.5.2")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.bootBuildImage {
    imageName = "$dockerHubRepository/${rootProject.name}:$version"

    //Fix https://github.com/spring-projects/spring-boot/issues/41199
    docker {
        host = "//./pipe/dockerDesktopLinuxEngine"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
