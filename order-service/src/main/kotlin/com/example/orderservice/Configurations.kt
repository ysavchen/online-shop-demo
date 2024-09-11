package com.example.orderservice

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@ConfigurationPropertiesScan("com.example.orderservice")
class PropertiesConfiguration

@Configuration
class ApplicationConfiguration(private val applicationProperties: ApplicationProperties) {

    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(applicationProperties.timezone))
    }
}