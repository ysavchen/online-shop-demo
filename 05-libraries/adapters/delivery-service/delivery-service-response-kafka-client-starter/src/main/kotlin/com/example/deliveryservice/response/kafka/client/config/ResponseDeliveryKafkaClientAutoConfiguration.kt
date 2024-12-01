package com.example.deliveryservice.response.kafka.client.config

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import com.example.deliveryservice.response.kafka.client.*
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
@EnableConfigurationProperties(ResponseDeliveryKafkaClientProperties::class)
@Import(
    RequestDeliveryKafkaConsumerConfiguration::class,
    ResponseDeliveryKafkaProducerConfiguration::class,
    ReplyingDeliveryKafkaConsumerConfiguration::class
)
class ResponseDeliveryKafkaClientAutoConfiguration

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.replying.consumer.request.topics"])
class RequestDeliveryKafkaConsumerConfiguration(private val properties: ResponseDeliveryKafkaClientProperties) {
    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaConsumerFactory"])
    fun requestDeliveryKafkaConsumerFactory(objectMapper: ObjectMapper): ConsumerFactory<UUID, RequestDeliveryMessage> {
        return DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
                ConsumerConfig.GROUP_ID_CONFIG to properties.kafka.replying.consumer.request.groupId,
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
        val topics = properties.kafka.replying.consumer.request.topics.toTypedArray()
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

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.replying.consumer.reply.topic"])
class ResponseDeliveryKafkaProducerConfiguration(private val properties: ResponseDeliveryKafkaClientProperties) {
    @Bean
    @ConditionalOnMissingBean(name = ["responseDeliveryKafkaProducerFactory"])
    fun responseDeliveryKafkaProducerFactory(objectMapper: ObjectMapper): ProducerFactory<UUID, ResponseDeliveryMessage> {
        val bootstrapServers = properties.kafka.connection.bootstrapServers.toList()
        return DefaultKafkaProducerFactory(
            mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers),
            UUIDSerializer(),
            JsonSerializer(jacksonTypeRef<ResponseDeliveryMessage>(), objectMapper),
            true
        )
    }

    @Bean
    @ConditionalOnMissingBean(name = ["responseDeliveryKafkaTemplate"])
    fun responseDeliveryKafkaTemplate(responseDeliveryKafkaProducerFactory: ProducerFactory<UUID, ResponseDeliveryMessage>) =
        KafkaTemplate(responseDeliveryKafkaProducerFactory).apply {
            defaultTopic = properties.kafka.replying.consumer.reply.topic
            setObservationEnabled(true)
        }

    @Bean
    @ConditionalOnMissingBean(name = ["responseDeliveryKafkaProducer"])
    fun responseDeliveryKafkaProducer(responseDeliveryKafkaTemplate: KafkaTemplate<UUID, ResponseDeliveryMessage>) =
        ResponseDeliveryKafkaProducerImpl(
            replyTopic = properties.kafka.replying.consumer.reply.topic,
            kafkaTemplate = responseDeliveryKafkaTemplate
        )
}

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(ReplyingDeliveryKafkaConsumer::class)
class ReplyingDeliveryKafkaConsumerConfiguration(private val properties: ResponseDeliveryKafkaClientProperties) {

    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaConsumer"])
    fun requestDeliveryKafkaConsumer(
        replyingDeliveryKafkaConsumer: ReplyingDeliveryKafkaConsumer,
        responseDeliveryKafkaProducer: ResponseDeliveryKafkaProducer
    ): RequestDeliveryKafkaConsumer =
        RequestDeliveryKafkaConsumerImpl(
            properties.kafka.replying.consumer.enabled,
            replyingDeliveryKafkaConsumer,
            responseDeliveryKafkaProducer
        )
}