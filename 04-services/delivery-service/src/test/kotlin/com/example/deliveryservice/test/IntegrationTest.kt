package com.example.deliveryservice.test

import com.example.deliveryservice.kafka.client.model.ReplyDeliveryMessage
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.reply.kafka.client.config.ReplyDeliveryKafkaClientProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.aot.DisabledInAotMode
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)

@DisabledInAotMode
@ActiveProfiles("junit")
@EmbeddedKafka(
    topics = [
        "\${application.clients.delivery-service.kafka.replying.consumer.request.topics}",
        "\${application.clients.delivery-service.kafka.replying.consumer.reply.topic}"
    ],
    bootstrapServersProperty = "application.clients.delivery-service.kafka.connection.bootstrap-servers"
)
@Import(IntegrationTestConfiguration::class, TestKafkaConfiguration::class)
@SpringBootTest
annotation class IntegrationTest

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    @ServiceConnection
    fun postgres() = PostgreSQLContainer("postgres:17.0-alpine")

}

@TestConfiguration
class TestKafkaConfiguration(private val properties: ReplyDeliveryKafkaClientProperties) {

    @Bean
    fun testConsumerFactory(objectMapper: ObjectMapper): ConsumerFactory<UUID, ReplyDeliveryMessage> =
        DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
                ConsumerConfig.GROUP_ID_CONFIG to "test-consumer",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to OffsetResetStrategy.EARLIEST.toString(),
                ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG to false,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true
            ),
            ErrorHandlingDeserializer(UUIDDeserializer()).apply { isForKey = true },
            ErrorHandlingDeserializer(
                JsonDeserializer(jacksonTypeRef<ReplyDeliveryMessage>(), objectMapper, false)
            ).apply { isForKey = false }
        )

    @Bean
    fun testMessageListenerContainer(
        testConsumerFactory: ConsumerFactory<UUID, ReplyDeliveryMessage>
    ): ConcurrentMessageListenerContainer<UUID, ReplyDeliveryMessage> {
        val topics = arrayOf(properties.kafka.replying.consumer.reply.topic)
        val containerProperties = ContainerProperties(*topics)
        return ConcurrentMessageListenerContainer(testConsumerFactory, containerProperties)
    }

    @Bean
    fun testProducerFactory(objectMapper: ObjectMapper): ProducerFactory<UUID, RequestDeliveryMessage> {
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
    fun testKafkaTemplate(
        testMessageListenerContainer: ConcurrentMessageListenerContainer<UUID, ReplyDeliveryMessage>,
        testProducerFactory: ProducerFactory<UUID, RequestDeliveryMessage>
    ): ReplyingKafkaTemplate<UUID, RequestDeliveryMessage, ReplyDeliveryMessage> =
        ReplyingKafkaTemplate(testProducerFactory, testMessageListenerContainer)

}