package com.example.orderservice;

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import

@Import(PostgresConfiguration::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

    @Test
    fun contextLoads() {
    }

}