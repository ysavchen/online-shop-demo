package com.example.orderservice

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan("com.example.orderservice")
class PropertiesConfiguration

@ConfigurationProperties("spring.application")
data class AppProperties(
    val timezone: String
)