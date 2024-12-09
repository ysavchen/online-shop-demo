package com.example.deliveryservice.test

import com.example.deliveryservice.kafka.client.model.ReplyDeliveryMessage
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.reply.kafka.client.config.ReplyDeliveryKafkaClientProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
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
    fun testConsumerConfig(objectMapper: ObjectMapper) = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.kafka.connection.bootstrapServers.toList(),
        ConsumerConfig.GROUP_ID_CONFIG to properties.kafka.replying.consumer.request.groupId,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to OffsetResetStrategy.EARLIEST.name.lowercase(),
        ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG to false,
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to UUIDDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer(
            jacksonTypeRef<ReplyDeliveryMessage>(), objectMapper, false
        )
    )

    @Bean
    fun testKafkaConsumer(testConsumerConfig: Map<String, Any>): KafkaConsumer<UUID, ReplyDeliveryMessage> =
        KafkaConsumer<UUID, ReplyDeliveryMessage>(testConsumerConfig).also {
            it.subscribe(listOf(properties.kafka.replying.consumer.reply.topic))
        }

    @Bean
    fun testKafkaTemplate(objectMapper: ObjectMapper): KafkaTemplate<UUID, RequestDeliveryMessage> {
        val bootstrapServers = properties.kafka.connection.bootstrapServers.toList()
        val producerFactory = DefaultKafkaProducerFactory(
            mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers),
            UUIDSerializer(),
            JsonSerializer(jacksonTypeRef<RequestDeliveryMessage>(), objectMapper).apply {
                isAddTypeInfo = false
            },
            true
        )
        return KafkaTemplate(producerFactory)
    }
}