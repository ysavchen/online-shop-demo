project("order-service-domain-kafka-client-starter") {
    dependencies {
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("com.fasterxml.jackson.module:jackson-module-kotlin")
        api("org.springframework.boot:spring-boot-starter")
        api("org.springframework.boot:spring-boot-starter-kafka")
        testImplementation("org.springframework.boot:spring-boot-starter-kafka-test")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}