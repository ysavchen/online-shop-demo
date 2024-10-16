package com.example.orderservice.config

import com.example.orderservice.api.rest.model.Order
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.util.StdDateFormat.DATE_FORMAT_STR_ISO8601
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

@Configuration
@ConfigurationPropertiesScan("com.example.orderservice")
class PropertiesConfiguration

@Configuration
@ImportRuntimeHints(AppRuntimeHints::class)
class ApplicationConfiguration(private val appProperties: ApplicationProperties) {

    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(appProperties.timezone))
    }
}

@EnableCaching
@Configuration(proxyBeanMethods = false)
class CacheConfiguration {

    @Bean
    fun redisTemplate(
        objectMapper: ObjectMapper,
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<UUID, Order> {
        val serializer = Jackson2JsonRedisSerializer(objectMapper, Order::class.java)
        return RedisTemplate<UUID, Order>().apply {
            setConnectionFactory(connectionFactory)
            setValueSerializer(serializer)
        }
    }
}

@Configuration(proxyBeanMethods = false)
class JsonConfiguration {

    private val offsetDateTimeSerializer = OffsetDateTimeSerializer(
        OffsetDateTimeSerializer.INSTANCE,
        false,
        DateTimeFormatter.ofPattern(DATE_FORMAT_STR_ISO8601),
        JsonFormat.Shape.STRING
    )

    @Bean
    fun objectMapper(): ObjectMapper = JsonMapper.builder()
        .addModules(
            KotlinModule.Builder().build(),
            JavaTimeModule().addSerializer(offsetDateTimeSerializer),
        )
        .defaultTimeZone(TimeZone.getDefault())
        .defaultDateFormat(SimpleDateFormat(DATE_FORMAT_STR_ISO8601))
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