package com.example.service.support.jackson

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import tools.jackson.core.StreamWriteFeature
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.MapperFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.cfg.EnumFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.util.StdDateFormat
import tools.jackson.module.kotlin.KotlinModule
import java.util.*

@AutoConfiguration(before = [JacksonAutoConfiguration::class])
@ConditionalOnClass(JsonMapper::class)
class JacksonSupportAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    fun jsonMapper(): JsonMapper = JsonMapper.builder()
        .addModules(KotlinModule.Builder().build())
        .defaultTimeZone(TimeZone.getDefault())
        .defaultDateFormat(StdDateFormat())
        .enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
        .disable(
            DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS,
            DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
            DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,
            DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
        )
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build()
}