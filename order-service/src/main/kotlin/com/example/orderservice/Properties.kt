package com.example.orderservice

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties("spring.application")
data class AppProperties(
    @DefaultValue("UTC")
    val timezone: String
)