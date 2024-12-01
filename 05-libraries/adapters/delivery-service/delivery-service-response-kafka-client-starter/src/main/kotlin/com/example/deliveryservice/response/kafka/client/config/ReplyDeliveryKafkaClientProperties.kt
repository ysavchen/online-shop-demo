package com.example.deliveryservice.response.kafka.client.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

internal const val propertiesPrefix = "application.clients.delivery-service"

@ConfigurationProperties(propertiesPrefix, ignoreUnknownFields = false)
data class ReplyDeliveryKafkaClientProperties(

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
    val consumer: KafkaConsumerProperties
)

data class KafkaConsumerProperties(
    @NestedConfigurationProperty
    val request: RequestProperties,

    @NestedConfigurationProperty
    val reply: ReplyProperties,

    val enabled: Boolean = true
)

data class RequestProperties(
    val groupId: String,
    /**
     * topic: delivery-service.request
     */
    val topics: Set<String>
)

data class ReplyProperties(
    /**
     * topic: delivery-service.reply
     */
    val topic: String
)