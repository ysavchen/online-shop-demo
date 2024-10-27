package com.example.bookservice.rest.client.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.time.Duration

@ConfigurationProperties("application.client.book-service", ignoreUnknownFields = false)
data class BookServiceClientProperties(

    @NestedConfigurationProperty
    val http: HttpClientProperties
)

data class HttpClientProperties(
    val baseUrl: String,
    val connectionTimeout: Duration = Duration.ofSeconds(3),
    val responseTimeout: Duration = Duration.ofSeconds(3)
)