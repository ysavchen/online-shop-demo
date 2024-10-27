package com.example.bookservice.rest.client.config

import com.example.bookservice.rest.client.BookServiceClient
import com.example.bookservice.rest.client.BookServiceClientImpl
import io.netty.channel.ChannelOption
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@EnableConfigurationProperties(BookServiceClientProperties::class)
class BookServiceClientAutoConfiguration(private val properties: BookServiceClientProperties) {

    @Bean
    @ConditionalOnMissingBean(BookServiceClient::class)
    fun bookServiceClient(bookServiceHttpClient: HttpClient): BookServiceClient =
        BookServiceClientImpl(
            WebClient.builder()
                .baseUrl(properties.http.baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(ReactorClientHttpConnector(bookServiceHttpClient))
                .build()
        )

    @Bean
    fun bookServiceHttpClient(): HttpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.http.connectionTimeout.toMillis().toInt())
            .responseTimeout(properties.http.responseTimeout)

}