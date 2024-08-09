package com.example.orderservice.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Embeddable
data class TotalPriceEntity(
    @Column(name = "total_price", columnDefinition = "NUMERIC")
    val value: BigDecimal,

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    val currency: CurrencyEntity
)