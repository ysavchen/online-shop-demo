package com.example.deliveryservice.request.kafka.client.config

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import com.example.deliveryservice.request.kafka.client.RequestDeliveryKafkaProducerImpl
import com.example.deliveryservice.request.kafka.client.ResponseDeliveryKafkaConsumer
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
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.CommonLoggingErrorHandler
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*

@AutoConfiguration(before = [KafkaAutoConfiguration::class])
@EnableConfigurationProperties(RequestDeliveryKafkaClientProperties::class)
@Import(
    RequestDeliveryKafkaProducerConfiguration::class,
    ResponseDeliveryKafkaConsumerConfiguration::class
)
class RequestDeliveryKafkaClientAutoConfiguration

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.request.producer.topic"])
class RequestDeliveryKafkaProducerConfiguration(private val properties: RequestDeliveryKafkaClientProperties) {
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
            defaultTopic = properties.kafka.request.producer.topic
            setObservationEnabled(true)
        }

    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaProducer"])
    fun requestDeliveryKafkaProducer(requestDeliveryKafkaTemplate: KafkaTemplate<UUID, RequestDeliveryMessage>) =
        RequestDeliveryKafkaProducerImpl(
            kafkaTemplate = requestDeliveryKafkaTemplate,
            enabled = properties.kafka.request.producer.enabled
        )
}

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(ResponseDeliveryKafkaConsumer::class)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.response.consumer.topics"])
@ConditionalOnExpression("#{\${$propertiesPrefix.kafka.response.consumer.enabled:true}}")
class ResponseDeliveryKafkaConsumerConfiguration(private val properties: RequestDeliveryKafkaClientProperties) {
    @Bean
    @ConditionalOnMissingBean(name = ["responseDeliveryKafkaConsumerFactory"])
    fun responseDeliveryKafkaConsumerFactory(objectMapper: ObjectMapper): ConsumerFactory<UUID, ResponseDeliveryMessage> {
        return DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
                ConsumerConfig.GROUP_ID_CONFIG to properties.kafka.response!!.consumer!!.groupId,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to OffsetResetStrategy.EARLIEST.name.lowercase()
            ),
            ErrorHandlingDeserializer(UUIDDeserializer()).apply { isForKey = true },
            ErrorHandlingDeserializer(
                JsonDeserializer(jacksonTypeRef<ResponseDeliveryMessage>(), objectMapper, false)
            ).apply { isForKey = false }
        )
    }

    @Bean
    @ConditionalOnMissingBean(name = ["responseDeliveryKafkaListenerContainer"])
    fun responseDeliveryKafkaListenerContainer(
        consumer: ResponseDeliveryKafkaConsumer,
        domainOrderKafkaConsumerFactory: ConsumerFactory<UUID, ResponseDeliveryMessage>
    ): MessageListenerContainer {
        val topics = properties.kafka.response!!.consumer!!.topics.toTypedArray()
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