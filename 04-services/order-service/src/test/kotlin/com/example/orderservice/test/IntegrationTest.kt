package com.example.orderservice.test

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.aot.DisabledInAotMode
import org.testcontainers.containers.PostgreSQLContainer

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)

@DisabledInAotMode
@AutoConfigureMockMvc
@ActiveProfiles("junit")
@Import(IntegrationTestConfiguration::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
annotation class IntegrationTest

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    @ServiceConnection("postgres")
    fun postgres() = PostgreSQLContainer("postgres:17.0-alpine")

    @Bean
    @ServiceConnection("redis")
    fun redis() = RedisContainer("redis:7.4.0-alpine")

}