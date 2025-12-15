package com.example.orderservice.config

import com.example.orderservice.api.rest.model.Order
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.retry.support.RetryTemplate
import tools.jackson.databind.json.JsonMapper
import java.time.Duration

@Configuration
@ConfigurationPropertiesScan("com.example.orderservice")
class PropertiesConfiguration

@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(AppRuntimeHints::class)
class ApplicationConfiguration {

    @Bean
    fun retryTemplate(): RetryTemplate = RetryTemplate.defaultInstance()

}

@EnableCaching
@Configuration(proxyBeanMethods = false)
class CacheConfiguration(jsonMapper: JsonMapper) {

    companion object {
        const val CACHE_NAME_PREFIX: String = "order-service::"
        const val ORDER_CACHE_NAME: String = "orders"
        const val ENTRY_TTL_MINUTES: Long = 10
    }

    private val redisKeySerializer = JacksonJsonRedisSerializer(jsonMapper, String::class.java)
    private val redisValueSerializer = JacksonJsonRedisSerializer(jsonMapper, Order::class.java)

    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig()
            .prefixCacheNameWith(CACHE_NAME_PREFIX)
            .serializeKeysWith(SerializationPair.fromSerializer(redisKeySerializer))
            .serializeValuesWith(SerializationPair.fromSerializer(redisValueSerializer))
            .entryTtl(Duration.ofMinutes(ENTRY_TTL_MINUTES))
            .disableCachingNullValues()

    /**
     * Configuration for @Cacheable, @CachePut annotations
     */
    @Bean
    fun redisCacheManager(
        redisCacheConfiguration: RedisCacheConfiguration,
        redisConnectionFactory: RedisConnectionFactory
    ): RedisCacheManager =
        RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .initialCacheNames(setOf(ORDER_CACHE_NAME))
            .build()

    /**
     * Configuration for RedisTemplate
     */
    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory) =
        RedisTemplate<String, Order>().apply {
            connectionFactory = redisConnectionFactory
            keySerializer = redisKeySerializer
            valueSerializer = redisValueSerializer
        }
}