package com.example.orderservice.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Embeddable
data class ItemPriceEntity(
    @Column(name = "price", columnDefinition = "NUMERIC", nullable = false)
    val value: BigDecimal,

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    val currency: ItemCurrencyEntity
)

enum class ItemCurrencyEntity {
    RUB, EUR
}