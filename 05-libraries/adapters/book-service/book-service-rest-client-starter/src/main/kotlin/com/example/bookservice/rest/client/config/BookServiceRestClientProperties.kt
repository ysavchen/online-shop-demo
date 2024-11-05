package com.example.bookservice.rest.client.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.context.properties.bind.DefaultValue
import java.time.Duration

@ConfigurationProperties("application.clients.book-service", ignoreUnknownFields = false)
data class BookServiceRestClientProperties(

    @NestedConfigurationProperty
    val http: HttpClientProperties
)

data class HttpClientProperties(
    @DefaultValue("http://localhost:8090")
    val baseUrl: String,
    @DefaultValue("3000")
    val connectionTimeout: Duration,
    @DefaultValue("3000")
    val responseTimeout: Duration
)