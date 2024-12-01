project("delivery-service-model") {
    dependencies {
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("com.fasterxml.jackson.module:jackson-module-kotlin")
    }
}

project("delivery-service-request-kafka-client") {
    dependencies {
        api(project(":delivery-service:delivery-service-model"))
        api("org.springframework.kafka:spring-kafka")
        testImplementation("org.springframework.kafka:spring-kafka-test")
    }
}

project("delivery-service-request-kafka-client-starter") {
    dependencies {
        api(project(":delivery-service:delivery-service-request-kafka-client"))
        api("org.springframework.kafka:spring-kafka")
        api("org.springframework.boot:spring-boot-starter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project("delivery-service-reply-kafka-client") {
    dependencies {
        api(project(":delivery-service:delivery-service-model"))
        api("org.springframework.kafka:spring-kafka")
        testImplementation("org.springframework.kafka:spring-kafka-test")
    }
}

project("delivery-service-reply-kafka-client-starter") {
    dependencies {
        api(project(":delivery-service:delivery-service-reply-kafka-client"))
        api("org.springframework.kafka:spring-kafka")
        api("org.springframework.boot:spring-boot-starter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}