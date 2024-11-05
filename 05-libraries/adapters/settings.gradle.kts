rootProject.name = "adapters"

include(
    "book-service:book-service-rest-client",
    "book-service:book-service-rest-client-starter",

    "order-service:order-service-domain-kafka-client",
    "order-service:order-service-domain-kafka-client-starter"
)