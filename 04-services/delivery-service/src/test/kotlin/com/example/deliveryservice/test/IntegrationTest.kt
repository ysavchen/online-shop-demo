package com.example.deliveryservice.test

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.aot.DisabledInAotMode
import org.testcontainers.containers.PostgreSQLContainer

@DisabledInAotMode
@ActiveProfiles("junit")
@Import(IntegrationTestConfiguration::class)
@SpringBootTest
annotation class IntegrationTest

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    @ServiceConnection
    fun postgres() = PostgreSQLContainer("postgres:16.3-alpine")

}