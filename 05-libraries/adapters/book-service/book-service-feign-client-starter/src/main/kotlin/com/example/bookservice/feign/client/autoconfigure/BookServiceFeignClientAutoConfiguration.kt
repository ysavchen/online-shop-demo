package com.example.bookservice.feign.client.autoconfigure

import com.example.bookservice.feign.client.BookServiceFeignClient
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Feign
import feign.Logger.Level
import feign.Request
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.util.concurrent.TimeUnit

@AutoConfiguration
@EnableConfigurationProperties(BookServiceFeignClientProperties::class)
@Import(FeignClientsConfiguration::class)
class BookServiceFeignClientAutoConfiguration(private val properties: BookServiceFeignClientProperties) {

    private val connectionTimeout: Long = properties.http.connectionTimeout.toSeconds()
    private val responseTimeout: Long = properties.http.responseTimeout.toSeconds()

    @Bean
    @ConditionalOnMissingBean(BookServiceFeignClient::class)
    fun bookServiceFeignClient(objectMapper: ObjectMapper): BookServiceFeignClient =
        Feign.builder()
            .options(Request.Options(connectionTimeout, TimeUnit.SECONDS, responseTimeout, TimeUnit.SECONDS, true))
            .encoder(JacksonEncoder(objectMapper))
            .decoder(JacksonDecoder(objectMapper))
            .requestInterceptor { request ->
                request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                request.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            }
            .logLevel(Level.FULL)
            .target(BookServiceFeignClient::class.java, properties.http.baseUrl)

}