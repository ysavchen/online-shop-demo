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
    val replying: ReplyingProperties
)

data class KafkaConnectionProperties(
    /**
     * bootstrap-servers: http://localhost:9092
     */
    val bootstrapServers: Set<String>
)

data class ReplyingProperties(
    @NestedConfigurationProperty
    val producer: KafkaProducerProperties
)

data class KafkaProducerProperties(
    @NestedConfigurationProperty
    val request: RequestProperties,

    @NestedConfigurationProperty
    val reply: ReplyProperties,

    val enabled: Boolean = true
)

data class RequestProperties(
    /**
     * topic: delivery-service.request
     */
    val topic: String
)

data class ReplyProperties(
    val groupIdPrefix: String,
    /**
     * topic: delivery-service.reply
     */
    val topics: Set<String>
)