package com.example.bookservice.rest.client.config

import com.example.bookservice.rest.client.BookServiceRestClient
import com.example.bookservice.rest.client.BookServiceRestClientImpl
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.netty.channel.ChannelOption
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.netty.http.client.HttpClient

@AutoConfiguration
@EnableConfigurationProperties(BookServiceRestClientProperties::class)
class BookServiceRestClientAutoConfiguration(private val properties: BookServiceRestClientProperties) {

    companion object {
        private const val CIRCUIT_BREAKER_ID = "bookServiceCircuitBreaker"
    }

    @Bean
    @ConditionalOnMissingBean(BookServiceRestClient::class)
    fun bookServiceRestClient(
        bookServiceHttpClient: HttpClient,
        bookServiceCircuitBreaker: ReactiveCircuitBreaker
    ): BookServiceRestClient =
        BookServiceRestClientImpl(
            webClient = WebClient.builder()
                .baseUrl(properties.http.baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(ReactorClientHttpConnector(bookServiceHttpClient))
                .build(),
            circuitBreaker = bookServiceCircuitBreaker
        )

    @Bean
    fun bookServiceCircuitBreaker(circuitBreakerFactory: ReactiveResilience4JCircuitBreakerFactory): ReactiveCircuitBreaker =
        circuitBreakerFactory.create(CIRCUIT_BREAKER_ID)

    @Bean
    fun bookServiceCircuitBreakerCustomizer(): Customizer<ReactiveResilience4JCircuitBreakerFactory> {
        val timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(properties.http.responseTimeout).build()
        val circuitBreakerConfig = CircuitBreakerConfig.custom()
            .ignoreExceptions(WebClientResponseException.NotFound::class.java)
            .build()

        return Customizer { factory ->
            factory.configure(
                { builder -> builder.timeLimiterConfig(timeLimiterConfig).circuitBreakerConfig(circuitBreakerConfig) },
                CIRCUIT_BREAKER_ID
            )
        }
    }

    @Bean
    fun bookServiceHttpClient(): HttpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.http.connectionTimeout.toMillis().toInt())
            .responseTimeout(properties.http.responseTimeout)

}