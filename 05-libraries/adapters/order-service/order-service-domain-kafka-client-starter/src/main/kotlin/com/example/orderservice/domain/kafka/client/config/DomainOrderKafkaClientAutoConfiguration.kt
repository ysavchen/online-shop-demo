package com.example.orderservice.domain.kafka.client.config

import com.example.orderservice.domain.kafka.client.DomainOrderKafkaConsumer
import com.example.orderservice.domain.kafka.client.DomainOrderKafkaProducerImpl
import com.example.orderservice.domain.kafka.client.model.DomainEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
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
@EnableConfigurationProperties(DomainOrderKafkaClientProperties::class)
class DomainOrderKafkaClientAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.producer.topic"])
    class DomainOrderKafkaProducerConfiguration(private val properties: DomainOrderKafkaClientProperties) {
        @Bean
        @ConditionalOnMissingBean(name = ["domainOrderKafkaProducerFactory"])
        fun domainOrderKafkaProducerFactory(objectMapper: ObjectMapper): ProducerFactory<UUID, DomainEvent> =
            DefaultKafkaProducerFactory(
                mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList()),
                UUIDSerializer(),
                JsonSerializer(jacksonTypeRef<DomainEvent>(), objectMapper),
                true
            )

        @Bean
        @ConditionalOnMissingBean(name = ["domainOrderKafkaTemplate"])
        fun domainOrderKafkaTemplate(domainOrderKafkaProducerFactory: ProducerFactory<UUID, DomainEvent>) =
            KafkaTemplate(domainOrderKafkaProducerFactory).apply {
                defaultTopic = properties.kafka.producer!!.topic
                setObservationEnabled(true)
            }

        @Bean
        @ConditionalOnMissingBean(name = ["domainOrderKafkaProducer"])
        fun domainOrderKafkaProducer(domainOrderKafkaTemplate: KafkaTemplate<UUID, DomainEvent>) =
            DomainOrderKafkaProducerImpl(domainOrderKafkaTemplate)

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean(DomainOrderKafkaConsumer::class)
    @ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.consumer.topics"])
    @ConditionalOnExpression("#{\${$propertiesPrefix.kafka.consumer.enabled:true}}")
    class DomainOrderKafkaConsumerConfiguration(private val properties: DomainOrderKafkaClientProperties) {
        @Bean
        @ConditionalOnMissingBean(name = ["domainOrderKafkaConsumerFactory"])
        fun domainOrderKafkaConsumerFactory(objectMapper: ObjectMapper): ConsumerFactory<UUID, DomainEvent> {
            return DefaultKafkaConsumerFactory(
                mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
                    ConsumerConfig.GROUP_ID_CONFIG to properties.kafka.consumer!!.groupId,
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to OffsetResetStrategy.EARLIEST.name.lowercase()
                ),
                ErrorHandlingDeserializer(UUIDDeserializer()),
                ErrorHandlingDeserializer(
                    JsonDeserializer(jacksonTypeRef<DomainEvent>(), objectMapper, false)
                )
            )
        }

        @Bean
        @ConditionalOnMissingBean(name = ["domainOrderKafkaListenerContainer"])
        fun domainOrderKafkaListenerContainer(
            consumer: DomainOrderKafkaConsumer,
            domainOrderKafkaConsumerFactory: ConsumerFactory<UUID, DomainEvent>
        ): MessageListenerContainer {
            val topics = properties.kafka.consumer!!.topics.toTypedArray()
            val containerProperties = ContainerProperties(*topics).apply {
                messageListener = consumer
                isObservationEnabled = true
            }

            return ConcurrentMessageListenerContainer(
                domainOrderKafkaConsumerFactory,
                containerProperties
            )
        }
    }
}