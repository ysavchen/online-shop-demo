package com.example.bookservice.rest.client.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.time.Duration

@ConfigurationProperties("application.clients.book-service", ignoreUnknownFields = false)
data class BookServiceRestClientProperties(

    @NestedConfigurationProperty
    val http: HttpClientProperties
)

data class HttpClientProperties(

    /**
     * base-url: http://localhost:8090
     */
    val baseUrl: String,
    val connectionTimeout: Duration = Duration.ofSeconds(3),
    val responseTimeout: Duration = Duration.ofSeconds(3)
)