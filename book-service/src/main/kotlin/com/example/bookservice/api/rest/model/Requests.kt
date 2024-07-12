package com.example.bookservice.api.rest.model

data class PageRequestParams(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "title",
    val orderBy: String = "asc"
)