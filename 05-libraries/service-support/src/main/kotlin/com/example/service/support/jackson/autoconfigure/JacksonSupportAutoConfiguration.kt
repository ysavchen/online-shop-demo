package com.example.service.support.jackson.autoconfigure

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import tools.jackson.core.StreamWriteFeature
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.MapperFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.util.StdDateFormat.DATE_FORMAT_STR_ISO8601
import tools.jackson.datatype.jsr310.JavaTimeModule
import tools.jackson.module.kotlin.KotlinModule
import java.text.SimpleDateFormat
import java.util.*

@AutoConfiguration
class JacksonSupportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun jsonMapper(): JsonMapper = JsonMapper.builder()
        .addModules(KotlinModule.Builder().build(), JavaTimeModule())
        .defaultTimeZone(TimeZone.getDefault())
        .defaultDateFormat(SimpleDateFormat(DATE_FORMAT_STR_ISO8601))
        .enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .disable(
            DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS,
            DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
            DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,
            DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
        )
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build()
}