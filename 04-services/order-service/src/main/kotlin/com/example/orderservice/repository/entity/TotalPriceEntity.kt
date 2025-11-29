package com.example.orderservice.repository.entity

import com.example.online.shop.model.PriceValue
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class TotalPriceEntity(
    @Column(name = "total_price", columnDefinition = "NUMERIC", nullable = false)
    val value: PriceValue,

    @Column(name = "total_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    val currency: CurrencyEntity
)

enum class CurrencyEntity {
    RUB, EUR
}