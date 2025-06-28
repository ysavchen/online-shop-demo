project("book-service-rest-client-starter") {
    dependencies {
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("com.fasterxml.jackson.module:jackson-module-kotlin")
        api("io.projectreactor.kotlin:reactor-kotlin-extensions")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        api("org.springframework.boot:spring-boot-starter")
        api("org.springframework.boot:spring-boot-starter-webflux")
        api("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project("book-service-feign-client-starter") {
    dependencies {
        api("com.fasterxml.jackson.module:jackson-module-kotlin")
        api("io.github.openfeign:feign-jackson:13.6")
        api("io.github.openfeign:feign-kotlin:13.6")
        api("org.springframework.boot:spring-boot-starter")
        api("org.springframework.cloud:spring-cloud-starter-openfeign")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}