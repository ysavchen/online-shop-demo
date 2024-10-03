package com.example.apigateway;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class AppRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // must be registered by Spring Cloud Gateway, bug fix
        hints.resources().registerPattern("META-INF/scripts/request_rate_limiter.lua");
    }
}