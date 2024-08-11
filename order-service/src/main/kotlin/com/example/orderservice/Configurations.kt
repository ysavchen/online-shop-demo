package com.example.orderservice

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class AppConfiguration(private val appProperties: AppProperties) {

    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(appProperties.timezone))
    }
}