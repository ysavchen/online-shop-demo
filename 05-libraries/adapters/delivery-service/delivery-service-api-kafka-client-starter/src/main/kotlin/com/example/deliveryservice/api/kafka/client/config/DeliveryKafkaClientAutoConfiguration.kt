package com.example.deliveryservice.api.kafka.client.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

@AutoConfiguration(before = [KafkaAutoConfiguration::class])
@EnableConfigurationProperties(DeliveryKafkaClientProperties::class)
@Import(
    RequestDeliveryKafkaProducerConfiguration::class,
    RequestDeliveryKafkaConsumerConfiguration::class,
    ResponseDeliveryKafkaProducerConfiguration::class,
    ResponseDeliveryKafkaConsumerConfiguration::class
)
class DeliveryKafkaClientAutoConfiguration