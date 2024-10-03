package com.example.apigateway.filters;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import static com.example.apigateway.filters.FilterUtils.REQUEST_ID;

@Configuration(proxyBeanMethods = false)
public class FilterConfiguration {

    private static final String APP_NAME = "api-gateway";

    /**
     * Config for Redis Rate Limiter.<p>
     * The default implementation of KeyResolver is the PrincipalNameKeyResolver.<p>
     * It uses Principal.getName() as a key for Redis, i.e. rate limiter works for each user separately.<p>
     * Current implementation changes the behavior to limit all requests for API Gateway.
     */
    @Bean
    KeyResolver keyResolver() {
        return exchange -> Mono.just(APP_NAME);
    }

    /**
     * Sets requestId to response
     */
    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
            String correlationId = FilterUtils.getRequestId(requestHeaders);
            exchange.getResponse().getHeaders().add(REQUEST_ID, correlationId);
        }));
    }

}