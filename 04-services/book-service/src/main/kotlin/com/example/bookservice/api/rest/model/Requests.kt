package com.example.bookservice.api.rest.model

import java.beans.ConstructorProperties

const val IDEMPOTENCY_KEY = "Idempotency-Key"

data class BookRequestParams
@ConstructorProperties("page", "page_size", "sort_by", "order_by")
constructor(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "title",
    val orderBy: String = "asc"
)

data class ReviewRequestParams
@ConstructorProperties("page", "page_size", "sort_by", "order_by")
constructor(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "rating",
    val orderBy: String = "desc"
)