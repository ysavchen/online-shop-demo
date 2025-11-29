package com.example.orderservice.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Embeddable
data class TotalPriceEntity(
    @Column(name = "total_price", columnDefinition = "NUMERIC(9, 2)", nullable = false)
    val value: BigDecimal,

    @Column(name = "total_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    val currency: CurrencyEntity
)

enum class CurrencyEntity {
    RUB, EUR
}