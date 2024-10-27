project("book-service-rest-client") {
    dependencies {
        api("org.springframework:spring-webflux")
        api("com.fasterxml.jackson.module:jackson-module-kotlin")
        api("io.projectreactor.kotlin:reactor-kotlin-extensions")
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        testImplementation("io.projectreactor:reactor-test")
    }
}

project("book-service-rest-client-starter") {
    dependencies {
        api(project(":book-service:book-service-rest-client"))
        api("org.springframework.boot:spring-boot-starter")
        api("org.springframework.boot:spring-boot-starter-webflux")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}