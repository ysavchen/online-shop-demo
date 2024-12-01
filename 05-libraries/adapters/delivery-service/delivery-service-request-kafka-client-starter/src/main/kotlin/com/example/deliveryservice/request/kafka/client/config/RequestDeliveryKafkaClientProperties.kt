package com.example.deliveryservice.request.kafka.client.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

internal const val propertiesPrefix = "application.clients.delivery-service"

@ConfigurationProperties(propertiesPrefix, ignoreUnknownFields = false)
data class RequestDeliveryKafkaClientProperties(

    @NestedConfigurationProperty
    val kafka: KafkaProperties
)

data class KafkaProperties(
    @NestedConfigurationProperty
    val connection: KafkaConnectionProperties,

    @NestedConfigurationProperty
    val request: RequestProperties,

    @NestedConfigurationProperty
    val response: ResponseProperties
)

data class RequestProperties(
    @NestedConfigurationProperty
    val producer: KafkaProducerProperties,
)

data class ResponseProperties(
    @NestedConfigurationProperty
    val consumer: KafkaConsumerProperties
)

data class KafkaConnectionProperties(
    /**
     * bootstrap-servers: http://localhost:9092
     */
    val bootstrapServers: Set<String>,
)

data class KafkaProducerProperties(
    /**
     * topic: delivery-service.request
     */
    val topic: String,
    val enabled: Boolean = true
)

data class KafkaConsumerProperties(
    val groupId: String,
    /**
     * topic: delivery-service.response
     */
    val topics: Set<String>
)