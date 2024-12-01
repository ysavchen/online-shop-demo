project("delivery-service-model") {
    dependencies {
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("com.fasterxml.jackson.module:jackson-module-kotlin")
    }
}

project("delivery-service-request-kafka-client-starter") {
    dependencies {
        api(project(":delivery-service:delivery-service-model"))
        api("org.springframework.kafka:spring-kafka")
        api("org.springframework.kafka:spring-kafka-test")
        api("org.springframework.boot:spring-boot-starter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project("delivery-service-reply-kafka-client-starter") {
    dependencies {
        api(project(":delivery-service:delivery-service-model"))
        api("org.springframework.kafka:spring-kafka")
        api("org.springframework.kafka:spring-kafka-test")
        api("org.springframework.boot:spring-boot-starter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}