package com.example.orderservice.test

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

@TestConfiguration(proxyBeanMethods = false)
class IntegrationTestConfiguration {

    @Bean
    @ServiceConnection
    fun postgres() = PostgreSQLContainer("postgres:16.3-alpine")

}
