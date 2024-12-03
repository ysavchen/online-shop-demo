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

    companion object {
        private const val KEY_PREFIX = "$CACHE_NAME_PREFIX$ORDER_CACHE_NAME"
        private val entryTtl = Duration.ofMinutes(ENTRY_TTL_MINUTES)
    }

    fun set(order: Order) {
        val key = "$KEY_PREFIX::${order.id}"
        redisTemplate.opsForValue().set(key, order, entryTtl)
    }
}