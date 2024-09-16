package com.example.deliveryservice.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import java.util.*

@Configuration
@ImportRuntimeHints(AppRuntimeHints::class)
class ApplicationConfiguration {

    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
}