package com.example.bookservice.api.rest.model

data class Paging(
    val page: Int = 1,
    val pageSize: Int = 10
)

data class Sorting(
    val sortBy: String = "title",
    val orderBy: String = "asc"
)