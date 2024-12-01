package com.example.deliveryservice.response.kafka.client.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

internal const val propertiesPrefix = "application.clients.delivery-service"

@ConfigurationProperties(propertiesPrefix, ignoreUnknownFields = false)
data class ResponseDeliveryKafkaClientProperties(

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
    val consumer: KafkaConsumerProperties
)

data class ResponseProperties(
    @NestedConfigurationProperty
    val producer: KafkaProducerProperties
)

data class KafkaConnectionProperties(
    /**
     * bootstrap-servers: http://localhost:9092
     */
    val bootstrapServers: Set<String>,
)

data class KafkaProducerProperties(
    /**
     * topic: delivery-service.response
     */
    val topic: String,
    val enabled: Boolean = true
)

data class KafkaConsumerProperties(
    val groupId: String,
    /**
     * topic: delivery-service.request
     */
    val topics: Set<String>,
    val enabled: Boolean = true
)