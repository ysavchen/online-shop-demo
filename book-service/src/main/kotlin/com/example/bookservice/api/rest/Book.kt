package com.example.bookservice.api.rest

import java.math.BigDecimal
import java.util.*

data class Book(
    val id: UUID,
    val title: String,
    val description: String,
    val price: BigDecimal
)