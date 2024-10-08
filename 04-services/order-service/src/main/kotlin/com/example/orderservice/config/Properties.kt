package com.example.orderservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties("application", ignoreUnknownFields = false)
data class ApplicationProperties(
    @DefaultValue("UTC")
    val timezone: String
)