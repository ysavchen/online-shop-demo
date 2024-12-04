package com.example.orderservice.api.rest.model

import java.beans.ConstructorProperties

const val IDEMPOTENCY_KEY = "Idempotency-Key"

data class OrderRequestParams
@ConstructorProperties("page", "page_size", "sort_by", "order_by")
constructor(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "created_at",
    val orderBy: String = "desc"
)

object EmbeddedParam {
    const val DELIVERY = "delivery"
}