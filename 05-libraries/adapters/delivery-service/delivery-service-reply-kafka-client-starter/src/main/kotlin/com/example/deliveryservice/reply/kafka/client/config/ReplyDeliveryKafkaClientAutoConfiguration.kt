package com.example.deliveryservice.reply.kafka.client.config

import com.example.deliveryservice.kafka.client.model.ReplyDeliveryMessage
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.reply.kafka.client.ReplyingDeliveryKafkaConsumer
import com.example.deliveryservice.reply.kafka.client.internal.ReplyDeliveryKafkaProducer
import com.example.deliveryservice.reply.kafka.client.internal.RequestDeliveryKafkaConsumer
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
@EnableConfigurationProperties(ReplyDeliveryKafkaClientProperties::class)
@Import(
    RequestDeliveryKafkaConsumerConfiguration::class,
    ReplyDeliveryKafkaProducerConfiguration::class,
    ReplyingDeliveryKafkaConsumerConfiguration::class
)
class ReplyDeliveryKafkaClientAutoConfiguration

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.replying.consumer.request.topics"])
class RequestDeliveryKafkaConsumerConfiguration(private val properties: ReplyDeliveryKafkaClientProperties) {
    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaConsumerFactory"])
    fun requestDeliveryKafkaConsumerFactory(objectMapper: ObjectMapper): ConsumerFactory<UUID, RequestDeliveryMessage> {
        return DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
                ConsumerConfig.GROUP_ID_CONFIG to properties.kafka.replying.consumer.request.groupId,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to OffsetResetStrategy.EARLIEST.name.lowercase(),
                ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG to "false"
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
        requestDeliveryKafkaConsumerFactory: ConsumerFactory<UUID, RequestDeliveryMessage>
    ): MessageListenerContainer {
        val topics = properties.kafka.replying.consumer.request.topics.toTypedArray()
        val containerProperties = ContainerProperties(*topics).apply {
            messageListener = consumer
            isObservationEnabled = true
        }

        return ConcurrentMessageListenerContainer(
            requestDeliveryKafkaConsumerFactory,
            containerProperties
        ).apply {
            commonErrorHandler = CommonLoggingErrorHandler()
        }
    }
}

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.replying.consumer.reply.topic"])
class ReplyDeliveryKafkaProducerConfiguration(private val properties: ReplyDeliveryKafkaClientProperties) {
    @Bean
    @ConditionalOnMissingBean(name = ["replyDeliveryKafkaProducerFactory"])
    fun replyDeliveryKafkaProducerFactory(objectMapper: ObjectMapper): ProducerFactory<UUID, ReplyDeliveryMessage> {
        val bootstrapServers = properties.kafka.connection.bootstrapServers.toList()
        return DefaultKafkaProducerFactory(
            mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers),
            UUIDSerializer(),
            JsonSerializer(jacksonTypeRef<ReplyDeliveryMessage>(), objectMapper).apply {
                isAddTypeInfo = false
            },
            true
        )
    }

    @Bean
    @ConditionalOnMissingBean(name = ["replyDeliveryKafkaTemplate"])
    fun replyDeliveryKafkaTemplate(replyDeliveryKafkaProducerFactory: ProducerFactory<UUID, ReplyDeliveryMessage>) =
        KafkaTemplate(replyDeliveryKafkaProducerFactory).apply {
            defaultTopic = properties.kafka.replying.consumer.reply.topic
            setObservationEnabled(true)
        }

    @Bean
    @ConditionalOnMissingBean(name = ["replyDeliveryKafkaProducer"])
    fun replyDeliveryKafkaProducer(replyDeliveryKafkaTemplate: KafkaTemplate<UUID, ReplyDeliveryMessage>) =
        ReplyDeliveryKafkaProducer(
            replyTopic = properties.kafka.replying.consumer.reply.topic,
            kafkaTemplate = replyDeliveryKafkaTemplate
        )
}

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(ReplyingDeliveryKafkaConsumer::class)
class ReplyingDeliveryKafkaConsumerConfiguration(private val properties: ReplyDeliveryKafkaClientProperties) {

    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaConsumer"])
    fun requestDeliveryKafkaConsumer(
        replyingKafkaConsumer: ReplyingDeliveryKafkaConsumer,
        replyKafkaProducer: ReplyDeliveryKafkaProducer
    ): RequestDeliveryKafkaConsumer =
        RequestDeliveryKafkaConsumer(
            enabled = properties.kafka.replying.consumer.enabled,
            kafkaConsumer = replyingKafkaConsumer,
            kafkaProducer = replyKafkaProducer
        )
}