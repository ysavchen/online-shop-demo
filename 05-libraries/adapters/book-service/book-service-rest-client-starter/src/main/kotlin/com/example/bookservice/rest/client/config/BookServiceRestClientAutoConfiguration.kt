package com.example.bookservice.rest.client.config

import com.example.bookservice.rest.client.BookServiceRestClient
import com.example.bookservice.rest.client.BookServiceRestClientImpl
import io.netty.channel.ChannelOption
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@AutoConfiguration
@EnableConfigurationProperties(BookServiceRestClientProperties::class)
class BookServiceRestClientAutoConfiguration(private val properties: BookServiceRestClientProperties) {

    @Bean
    @ConditionalOnMissingBean(BookServiceRestClient::class)
    fun bookServiceRestClient(bookServiceHttpClient: HttpClient): BookServiceRestClient =
        BookServiceRestClientImpl(
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