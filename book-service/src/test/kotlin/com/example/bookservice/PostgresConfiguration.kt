package com.example.bookservice

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration(proxyBeanMethods = false)
class PostgresConfiguration {

    @Bean
    @ServiceConnection
    fun postgres() = PostgreSQLContainer("postgres:16.3-alpine")

}