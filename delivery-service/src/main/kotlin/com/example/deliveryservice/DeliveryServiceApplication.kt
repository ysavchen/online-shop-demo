package com.example.deliveryservice;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints

@SpringBootApplication
@ImportRuntimeHints(AppRuntimeHints::class)
class DeliveryServiceApplication

fun main(args: Array<String>) {
    runApplication<DeliveryServiceApplication>(*args)
}
