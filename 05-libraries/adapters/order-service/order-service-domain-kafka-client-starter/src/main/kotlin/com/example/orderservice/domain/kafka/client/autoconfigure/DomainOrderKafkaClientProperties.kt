package com.example.orderservice.domain.kafka.client.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.context.properties.bind.DefaultValue

internal const val propertiesPrefix = "application.clients.order-service"

@ConfigurationProperties(propertiesPrefix, ignoreUnknownFields = false)
data class DomainOrderKafkaClientProperties(

    @NestedConfigurationProperty
    val kafka: KafkaProperties
)

data class KafkaProperties(
    @NestedConfigurationProperty
    val connection: KafkaConnectionProperties,

    @NestedConfigurationProperty
    val domain: DomainProperties
)

data class DomainProperties(
    @NestedConfigurationProperty
    val producer: KafkaProducerProperties?,

    @NestedConfigurationProperty
    val consumer: KafkaConsumerProperties?
)

data class KafkaConnectionProperties(
    /**
     * bootstrap-servers: http://localhost:9092
     */
    val bootstrapServers: Set<String>,
)

data class KafkaProducerProperties(
    /**
     * topic: order-service.domain
     */
    val topic: String,

    @DefaultValue("true")
    val enabled: Boolean
)

data class KafkaConsumerProperties(
    val groupId: String,
    /**
     * topics: order-service.domain
     */
    val topics: Set<String>,

    @DefaultValue("true")
    val enabled: Boolean
)