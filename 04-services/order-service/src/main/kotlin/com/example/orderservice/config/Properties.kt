package com.example.orderservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties("application")
data class ApplicationProperties(

    @NestedConfigurationProperty
    val features: Features
)

data class Features(
    @NestedConfigurationProperty
    val bookValidation: BookValidation
)

data class BookValidation(
    @DefaultValue("true")
    val enabled: Boolean
)