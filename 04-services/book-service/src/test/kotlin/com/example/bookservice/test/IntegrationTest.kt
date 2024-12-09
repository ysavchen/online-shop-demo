package com.example.bookservice.test

import com.example.orderservice.domain.kafka.client.config.DomainOrderKafkaClientProperties
import com.example.orderservice.domain.kafka.client.model.DomainEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.aot.DisabledInAotMode
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)

@DisabledInAotMode
@AutoConfigureMockMvc
@ActiveProfiles("junit")
@EmbeddedKafka(
    topics = ["\${application.clients.order-service.kafka.domain.consumer.topics}"],
    bootstrapServersProperty = "application.clients.order-service.kafka.connection.bootstrap-servers"
)
@Import(IntegrationTestConfiguration::class, TestKafkaConfiguration::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
annotation class IntegrationTest

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    @ServiceConnection
    fun postgres() = PostgreSQLContainer("postgres:17.0-alpine")

}

@TestConfiguration
class TestKafkaConfiguration(private val properties: DomainOrderKafkaClientProperties) {

    @Bean
    fun testKafkaTemplate(objectMapper: ObjectMapper): KafkaTemplate<UUID, DomainEvent> {
        val bootstrapServers = properties.kafka.connection.bootstrapServers.toList()
        val producerFactory = DefaultKafkaProducerFactory(
            mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers),
            UUIDSerializer(),
            JsonSerializer(jacksonTypeRef<DomainEvent>(), objectMapper).apply {
                isAddTypeInfo = false
            },
            true
        )
        return KafkaTemplate(producerFactory)
    }
}