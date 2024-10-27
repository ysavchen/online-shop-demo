package com.example.bookservice.rest.client.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("application.client.book-service", ignoreUnknownFields = false)
data class BookServiceClientProperties(
    val http: HttpClientProperties
)

data class HttpClientProperties(
    val baseUrl: String,
    val connectionTimeout: Duration = Duration.ofSeconds(3),
    val responseTimeout: Duration = Duration.ofSeconds(3)
)