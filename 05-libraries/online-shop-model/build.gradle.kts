plugins {
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.2.21"
}

group = "com.example"
version = "1.0.0"

val javaVersion = 21
val springBootVersion by extra("4.0.1")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("tools.jackson.module:jackson-module-kotlin")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20240325.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

kotlin {
    jvmToolchain(javaVersion)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
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