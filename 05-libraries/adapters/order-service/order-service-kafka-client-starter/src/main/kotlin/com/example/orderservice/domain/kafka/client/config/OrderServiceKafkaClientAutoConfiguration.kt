package com.example.orderservice.domain.kafka.client.config

import com.example.orderservice.domain.kafka.client.OrderServiceDomainKafkaProducerImpl
import com.example.orderservice.domain.kafka.client.config.OrderServiceKafkaClientAutoConfiguration.OrderServiceDomainKafkaProducerConfiguration
import com.example.orderservice.domain.kafka.client.model.Order
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*

@AutoConfiguration(before = [KafkaAutoConfiguration::class])
@EnableConfigurationProperties(OrderServiceKafkaClientProperties::class)
@Import(OrderServiceDomainKafkaProducerConfiguration::class)
class OrderServiceKafkaClientAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    class OrderServiceDomainKafkaProducerConfiguration(private val properties: OrderServiceKafkaClientProperties) {
        @Bean
        @ConditionalOnMissingBean
        fun orderServiceDomainKafkaProducerFactory(objectMapper: ObjectMapper): ProducerFactory<UUID, Order> =
            DefaultKafkaProducerFactory(
                mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers),
                UUIDSerializer(),
                JsonSerializer(jacksonTypeRef<Order>(), objectMapper),
                true
            )

        @Bean
        @ConditionalOnMissingBean
        fun orderServiceDomainKafkaTemplate(orderServiceDomainKafkaProducerFactory: ProducerFactory<UUID, Order>) =
            KafkaTemplate(orderServiceDomainKafkaProducerFactory).apply {
                defaultTopic = properties.kafka.producer.topic
                setObservationEnabled(true)
            }

        @Bean
        @ConditionalOnMissingBean
        fun orderServiceDomainKafkaProducer(orderServiceDomainKafkaTemplate: KafkaTemplate<UUID, Order>) =
            OrderServiceDomainKafkaProducerImpl(orderServiceDomainKafkaTemplate)

    }
}