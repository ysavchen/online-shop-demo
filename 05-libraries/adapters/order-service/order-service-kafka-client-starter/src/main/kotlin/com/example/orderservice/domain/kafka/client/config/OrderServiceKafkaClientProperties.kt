package com.example.orderservice.domain.kafka.client.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties("application.client.order-service", ignoreUnknownFields = false)
data class OrderServiceKafkaClientProperties(
    @NestedConfigurationProperty
    val kafka: KafkaProperties
)

data class KafkaProperties(
    @NestedConfigurationProperty
    val connection: KafkaConnectionProperties,
    @NestedConfigurationProperty
    val producer: KafkaProducerProperties
)

data class KafkaConnectionProperties(
    val bootstrapServers: Set<String>,
)

data class KafkaProducerProperties(
    val topic: String
)