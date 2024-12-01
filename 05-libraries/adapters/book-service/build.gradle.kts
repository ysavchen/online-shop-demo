project("book-service-rest-client-starter") {
    dependencies {
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("com.fasterxml.jackson.module:jackson-module-kotlin")
        api("io.projectreactor.kotlin:reactor-kotlin-extensions")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        api("org.springframework.boot:spring-boot-starter")
        api("org.springframework.boot:spring-boot-starter-webflux")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}