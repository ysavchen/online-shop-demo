package com.example.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Order(1)
@Component
public class RequestIdFilter implements GlobalFilter {

    /**
     * Sets requestId for request
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (!isRequestIdPresent(requestHeaders)) {
            exchange = FilterUtils.setRequestId(exchange, UUID.randomUUID());
        }

        return chain.filter(exchange);
    }

    private boolean isRequestIdPresent(HttpHeaders requestHeaders) {
        return FilterUtils.getRequestId(requestHeaders) != null;
    }
}