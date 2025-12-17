package com.example.service.support.jackson

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.core.StreamWriteFeature
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.MapperFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.util.StdDateFormat
import tools.jackson.module.kotlin.KotlinModule
import java.text.SimpleDateFormat
import java.util.*

@Configuration
class JacksonSupportConfiguration {

    @Bean
    fun jsonMapper(): JsonMapper = JsonMapper.builder()
        .addModules(KotlinModule.Builder().build())
        .defaultTimeZone(TimeZone.getDefault())
        .defaultDateFormat(SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601))
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