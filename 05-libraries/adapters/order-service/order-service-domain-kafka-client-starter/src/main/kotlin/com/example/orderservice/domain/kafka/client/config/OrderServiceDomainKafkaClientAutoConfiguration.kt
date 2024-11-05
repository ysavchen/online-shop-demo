package com.example.orderservice.domain.kafka.client.config

import com.example.orderservice.domain.kafka.client.OrderServiceDomainKafkaConsumer
import com.example.orderservice.domain.kafka.client.OrderServiceDomainKafkaProducerImpl
import com.example.orderservice.domain.kafka.client.model.Order
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*

@AutoConfiguration(before = [KafkaAutoConfiguration::class])
@EnableConfigurationProperties(OrderServiceKafkaClientProperties::class)
class OrderServiceDomainKafkaClientAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.producer"])
    class OrderServiceDomainKafkaProducerConfiguration(private val properties: OrderServiceKafkaClientProperties) {
        @Bean
        @ConditionalOnMissingBean(name = ["orderServiceDomainKafkaProducerFactory"])
        fun orderServiceDomainKafkaProducerFactory(objectMapper: ObjectMapper): ProducerFactory<UUID, Order> =
            DefaultKafkaProducerFactory(
                mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList()),
                UUIDSerializer(),
                JsonSerializer(jacksonTypeRef<Order>(), objectMapper),
                true
            )

        @Bean
        @ConditionalOnMissingBean(name = ["orderServiceDomainKafkaTemplate"])
        fun orderServiceDomainKafkaTemplate(
            orderServiceDomainKafkaProducerFactory: ProducerFactory<UUID, Order>
        ): KafkaTemplate<UUID, Order> {
            val topic = properties.kafka.producer?.topic
                ?: throw IllegalArgumentException("Kafka producer is not defined in application.yml")
            return KafkaTemplate(orderServiceDomainKafkaProducerFactory).apply {
                defaultTopic = topic
                setObservationEnabled(true)
            }
        }

        @Bean
        @ConditionalOnMissingBean(name = ["orderServiceDomainKafkaProducer"])
        fun orderServiceDomainKafkaProducer(orderServiceDomainKafkaTemplate: KafkaTemplate<UUID, Order>) =
            OrderServiceDomainKafkaProducerImpl(orderServiceDomainKafkaTemplate)

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean(OrderServiceDomainKafkaConsumer::class)
    @ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.consumer"])
    class OrderServiceDomainKafkaConsumerConfiguration(private val properties: OrderServiceKafkaClientProperties) {
        @Bean
        @ConditionalOnMissingBean(name = ["notificationUserAuthoritiesKafkaConsumerFactory"])
        fun orderServiceDomainKafkaConsumerFactory(objectMapper: ObjectMapper): ConsumerFactory<UUID, Order> {
            val groupId = properties.kafka.consumer?.groupId
                ?: throw IllegalArgumentException("Kafka consumer is not defined in application.yml")
            return DefaultKafkaConsumerFactory(
                mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
                    ConsumerConfig.GROUP_ID_CONFIG to groupId,
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to OffsetResetStrategy.EARLIEST
                ),
                ErrorHandlingDeserializer(UUIDDeserializer()),
                ErrorHandlingDeserializer(
                    JsonDeserializer(jacksonTypeRef<Order>(), objectMapper, false)
                        .apply { setRemoveTypeHeaders(false) }
                )
            )
        }

        @Bean
        @ConditionalOnMissingBean(name = ["orderServiceDomainKafkaListenerContainer"])
        fun orderServiceDomainKafkaListenerContainer(
            consumer: OrderServiceDomainKafkaConsumer,
            orderServiceDomainKafkaConsumerFactory: ConsumerFactory<UUID, Order>
        ): MessageListenerContainer {
            val topics = properties.kafka.consumer?.topics?.toTypedArray()
                ?: throw IllegalArgumentException("Kafka consumer is not defined in application.yml")
            val containerProperties = ContainerProperties(*topics)
                .apply {
                    messageListener = consumer
                    isObservationEnabled = true
                }

            return ConcurrentMessageListenerContainer(
                orderServiceDomainKafkaConsumerFactory,
                containerProperties
            )
        }
    }
}