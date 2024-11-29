project("delivery-service-api-kafka-client") {
    dependencies {
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("com.fasterxml.jackson.module:jackson-module-kotlin")
        api("org.springframework.kafka:spring-kafka")
        testImplementation("org.springframework.kafka:spring-kafka-test")
    }
}

project("delivery-service-api-kafka-client-starter") {
    dependencies {
        api(project(":delivery-service:delivery-service-api-kafka-client"))
        api("org.springframework.kafka:spring-kafka")
        api("org.springframework.boot:spring-boot-starter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}