package com.example.service.support.time

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import java.util.*

@AutoConfiguration
@EnableConfigurationProperties(TimeZoneProperties::class)
@ConditionalOnProperty(prefix = propertiesPrefix, name = ["timezone"], matchIfMissing = true)
class TimeZoneAutoConfiguration(private val properties: TimeZoneProperties) {

    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(properties.timezone))
    }
}