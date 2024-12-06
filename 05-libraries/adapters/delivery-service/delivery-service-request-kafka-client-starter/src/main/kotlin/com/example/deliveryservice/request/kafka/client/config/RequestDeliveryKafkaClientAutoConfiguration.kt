package com.example.deliveryservice.request.kafka.client.config

import com.example.deliveryservice.kafka.client.model.ReplyDeliveryMessage
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.request.kafka.client.ReplyingDeliveryKafkaProducer
import com.example.deliveryservice.request.kafka.client.ReplyingDeliveryKafkaProducerImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.CommonLoggingErrorHandler
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.time.Instant
import java.util.*

@AutoConfiguration(before = [KafkaAutoConfiguration::class])
@EnableConfigurationProperties(RequestDeliveryKafkaClientProperties::class)
@Import(RequestDeliveryKafkaProducerConfiguration::class)
class RequestDeliveryKafkaClientAutoConfiguration

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["kafka.replying.producer.request.topic"])
class RequestDeliveryKafkaProducerConfiguration(private val properties: RequestDeliveryKafkaClientProperties) {
    @Bean
    @ConditionalOnMissingBean(name = ["requestDeliveryKafkaProducerFactory"])
    fun requestDeliveryKafkaProducerFactory(objectMapper: ObjectMapper): ProducerFactory<UUID, RequestDeliveryMessage> {
        val bootstrapServers = properties.kafka.connection.bootstrapServers.toList()
        return DefaultKafkaProducerFactory(
            mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers),
            UUIDSerializer(),
            JsonSerializer(jacksonTypeRef<RequestDeliveryMessage>(), objectMapper).apply {
                isAddTypeInfo = false
            },
            true
        )
    }

    @Bean
    @ConditionalOnMissingBean(name = ["replyDeliveryKafkaConsumerFactory"])
    fun replyDeliveryKafkaConsumerFactory(objectMapper: ObjectMapper): ConsumerFactory<UUID, ReplyDeliveryMessage> {
        val postfix = Instant.now().epochSecond
        return DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
                ConsumerConfig.GROUP_ID_CONFIG to properties.kafka.replying.producer.reply.groupIdPrefix + "-" + postfix,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to OffsetResetStrategy.LATEST.name.lowercase(),
                ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG to "false"
            ),
            ErrorHandlingDeserializer(UUIDDeserializer()).apply { isForKey = true },
            ErrorHandlingDeserializer(
                JsonDeserializer(jacksonTypeRef<ReplyDeliveryMessage>(), objectMapper, false)
            ).apply { isForKey = false }
        )
    }

    @Bean
    @ConditionalOnMissingBean(name = ["replyDeliveryKafkaListenerContainer"])
    fun replyDeliveryKafkaListenerContainer(
        replyDeliveryKafkaConsumerFactory: ConsumerFactory<UUID, ReplyDeliveryMessage>
    ): ConcurrentMessageListenerContainer<UUID, ReplyDeliveryMessage> {
        val topics = properties.kafka.replying.producer.reply.topics.toTypedArray()
        val containerProperties = ContainerProperties(*topics).apply {
            isObservationEnabled = true
        }

        return ConcurrentMessageListenerContainer(
            replyDeliveryKafkaConsumerFactory,
            containerProperties
        ).apply {
            commonErrorHandler = CommonLoggingErrorHandler()
        }
    }

    @Bean
    @ConditionalOnMissingBean(name = ["replyingDeliveryKafkaTemplate"])
    fun replyingDeliveryKafkaTemplate(
        requestDeliveryKafkaProducerFactory: ProducerFactory<UUID, RequestDeliveryMessage>,
        replyDeliveryKafkaListenerContainer: ConcurrentMessageListenerContainer<UUID, ReplyDeliveryMessage>
    ): ReplyingKafkaTemplate<UUID, RequestDeliveryMessage, ReplyDeliveryMessage> =
        ReplyingKafkaTemplate(requestDeliveryKafkaProducerFactory, replyDeliveryKafkaListenerContainer)
            .apply {
                defaultTopic = properties.kafka.replying.producer.request.topic
                setDefaultReplyTimeout(properties.kafka.replying.producer.reply.timeout)
                setBinaryCorrelation(false)
                setObservationEnabled(true)
            }

    @Bean
    @ConditionalOnMissingBean(name = ["replyingDeliveryKafkaProducer"])
    fun replyingDeliveryKafkaProducer(
        replyingDeliveryKafkaTemplate: ReplyingKafkaTemplate<UUID, RequestDeliveryMessage, ReplyDeliveryMessage>
    ): ReplyingDeliveryKafkaProducer =
        ReplyingDeliveryKafkaProducerImpl(
            enabled = properties.kafka.replying.producer.enabled,
            requestTopic = properties.kafka.replying.producer.request.topic,
            kafkaTemplate = replyingDeliveryKafkaTemplate
        )
}