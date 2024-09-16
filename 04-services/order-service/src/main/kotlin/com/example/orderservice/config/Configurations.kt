package com.example.orderservice.config

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import java.util.*

@Configuration
@ConfigurationPropertiesScan("com.example.orderservice")
class PropertiesConfiguration

@Configuration
@ImportRuntimeHints(AppRuntimeHints::class)
class ApplicationConfiguration(private val applicationProperties: ApplicationProperties) {

    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(applicationProperties.timezone))
    }
}