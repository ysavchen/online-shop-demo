package com.example.orderservice.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
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

@Configuration
class JsonConfiguration {

    @Bean
    fun objectMapper() = JsonMapper.builder()
        .addModules(KotlinModule.Builder().build(), JavaTimeModule())
        .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .disable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS
        )
        .disable(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,
            DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
        )
        .build()
}