package com.example.apigateway.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;

import java.util.UUID;

public class FilterUtils {

    public static final String REQUEST_ID = "X-Request-Id";

    @Nullable
    public static String getRequestId(HttpHeaders requestHeaders) {
        return requestHeaders.getOrEmpty(REQUEST_ID).stream()
                .findFirst().orElse(null);
    }

    public static ServerWebExchange setRequestId(ServerWebExchange exchange, UUID requestId) {
        return exchange.mutate().request(
                exchange.getRequest().mutate()
                        .header(REQUEST_ID, requestId.toString())
                        .build()
        ).build();
    }
}