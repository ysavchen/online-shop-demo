package com.example.orderservice.repository.entity

import com.example.online.shop.model.PriceValue
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class ItemPriceEntity(
    @Column(name = "price", columnDefinition = "NUMERIC", nullable = false)
    val value: PriceValue,

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    val currency: ItemCurrencyEntity
)

enum class ItemCurrencyEntity {
    RUB, EUR
}