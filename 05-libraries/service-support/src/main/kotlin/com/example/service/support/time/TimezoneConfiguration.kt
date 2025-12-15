package com.example.service.support.time

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class TimezoneConfiguration {

    companion object {
        private const val SERVER_TIMEZONE = "Europe/Moscow"
    }

    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(SERVER_TIMEZONE))
    }
}