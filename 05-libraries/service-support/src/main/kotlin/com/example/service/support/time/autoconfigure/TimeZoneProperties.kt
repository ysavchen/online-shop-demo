package com.example.service.support.time.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

internal const val propertiesPrefix = "application"

@ConfigurationProperties(propertiesPrefix)
data class TimeZoneProperties(

    @DefaultValue("Europe/Moscow")
    val timezone: String
)