package com.example.deliveryservice.api.kafka.client.config

import com.example.deliveryservice.api.kafka.client.RequestDeliveryKafkaConsumer
import com.example.deliveryservice.api.kafka.client.RequestDeliveryKafkaProducerImpl
import com.example.deliveryservice.api.kafka.client.model.RequestDeliveryMessage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.CommonLoggingErrorHandler
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.request.producer.topic"])
class RequestDeliveryKafkaProducerConfiguration(private val properties: DeliveryKafkaClientProperties) {
    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaProducerFactory"])
    fun requestDeliveryKafkaProducerFactory(objectMapper: ObjectMapper): ProducerFactory<UUID, RequestDeliveryMessage> {
        val bootstrapServers = properties.kafka.connection.bootstrapServers.toList()
        return DefaultKafkaProducerFactory(
            mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers),
            UUIDSerializer(),
            JsonSerializer(jacksonTypeRef<RequestDeliveryMessage>(), objectMapper),
            true
        )
    }

    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaTemplate"])
    fun requestDeliveryKafkaTemplate(requestDeliveryKafkaProducerFactory: ProducerFactory<UUID, RequestDeliveryMessage>) =
        KafkaTemplate(requestDeliveryKafkaProducerFactory).apply {
            defaultTopic = properties.kafka.request!!.producer!!.topic
            setObservationEnabled(true)
        }

    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaProducer"])
    fun requestDeliveryKafkaProducer(requestDeliveryKafkaTemplate: KafkaTemplate<UUID, RequestDeliveryMessage>) =
        RequestDeliveryKafkaProducerImpl(
            kafkaTemplate = requestDeliveryKafkaTemplate,
            enabled = properties.kafka.request!!.producer!!.enabled
        )
}

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(RequestDeliveryKafkaConsumer::class)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.request.consumer.topics"])
@ConditionalOnExpression("#{\${$propertiesPrefix.kafka.request.consumer.enabled:true}}")
class RequestDeliveryKafkaConsumerConfiguration(private val properties: DeliveryKafkaClientProperties) {
    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaConsumerFactory"])
    fun requestDeliveryKafkaConsumerFactory(objectMapper: ObjectMapper): ConsumerFactory<UUID, RequestDeliveryMessage> {
        return DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
                ConsumerConfig.GROUP_ID_CONFIG to properties.kafka.request!!.consumer!!.groupId,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to OffsetResetStrategy.EARLIEST.name.lowercase()
            ),
            ErrorHandlingDeserializer(UUIDDeserializer()).apply { isForKey = true },
            ErrorHandlingDeserializer(
                JsonDeserializer(jacksonTypeRef<RequestDeliveryMessage>(), objectMapper, false)
            ).apply { isForKey = false }
        )
    }

    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaListenerContainer"])
    fun requestDeliveryKafkaListenerContainer(
        consumer: RequestDeliveryKafkaConsumer,
        domainOrderKafkaConsumerFactory: ConsumerFactory<UUID, RequestDeliveryMessage>
    ): MessageListenerContainer {
        val topics = properties.kafka.request!!.consumer!!.topics.toTypedArray()
        val containerProperties = ContainerProperties(*topics).apply {
            messageListener = consumer
            isObservationEnabled = true
        }

        return ConcurrentMessageListenerContainer(
            domainOrderKafkaConsumerFactory,
            containerProperties
        ).apply {
            commonErrorHandler = CommonLoggingErrorHandler()
        }
    }
}