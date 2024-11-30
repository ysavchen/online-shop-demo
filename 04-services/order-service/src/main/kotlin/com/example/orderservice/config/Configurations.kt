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
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.retry.support.RetryTemplate
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.*

@Configuration
@ConfigurationPropertiesScan("com.example.orderservice")
class PropertiesConfiguration

@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(AppRuntimeHints::class)
class ApplicationConfiguration(private val appProperties: ApplicationProperties) {

    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(appProperties.timezone))
    }

    @Bean
    fun retryTemplate() = RetryTemplate()
}

@EnableCaching
@Configuration(proxyBeanMethods = false)
class CacheConfiguration(objectMapper: ObjectMapper) {

    companion object {
        const val CACHE_NAME_PREFIX: String = "order-service::"
        const val ORDER_CACHE_NAME: String = "orders"
        const val ENTRY_TTL_MINUTES: Long = 10
    }

    private val redisKeySerializer = Jackson2JsonRedisSerializer(objectMapper, String::class.java)
    private val redisValueSerializer = Jackson2JsonRedisSerializer(objectMapper, Order::class.java)

    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig()
            .prefixCacheNameWith(CACHE_NAME_PREFIX)
            .serializeKeysWith(SerializationPair.fromSerializer(redisKeySerializer))
            .serializeValuesWith(SerializationPair.fromSerializer(redisValueSerializer))
            .entryTtl(Duration.ofMinutes(ENTRY_TTL_MINUTES))

    @Bean
    fun redisCacheManager(
        redisCacheConfiguration: RedisCacheConfiguration,
        redisConnectionFactory: RedisConnectionFactory
    ): RedisCacheManager =
        RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .initialCacheNames(setOf(ORDER_CACHE_NAME))
            .build()

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory) =
        RedisTemplate<String, Order>().apply {
            connectionFactory = redisConnectionFactory
            keySerializer = redisKeySerializer
            valueSerializer = redisValueSerializer
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