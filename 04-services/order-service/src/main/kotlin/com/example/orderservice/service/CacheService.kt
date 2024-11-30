package com.example.orderservice.service

import com.example.orderservice.api.rest.model.Order
import com.example.orderservice.config.CacheConfiguration.Companion.CACHE_NAME_PREFIX
import com.example.orderservice.config.CacheConfiguration.Companion.ENTRY_TTL_MINUTES
import com.example.orderservice.config.CacheConfiguration.Companion.ORDER_CACHE_NAME
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class CacheService(private val redisTemplate: RedisTemplate<String, Order>) {

    fun set(order: Order) {
        redisTemplate.opsForValue().set(
            "$CACHE_NAME_PREFIX$ORDER_CACHE_NAME::${order.id}",
            order,
            Duration.ofMinutes(ENTRY_TTL_MINUTES)
        )
    }
}