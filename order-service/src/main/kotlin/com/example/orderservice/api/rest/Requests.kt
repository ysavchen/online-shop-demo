package com.example.orderservice.api.rest

const val IDEMPOTENCY_KEY = "Idempotency-Key"

data class OrderRequestParams(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "created_at",
    val orderBy: String = "desc"
)