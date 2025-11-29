package com.example.bookservice.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Embeddable
data class PriceEntity(
    @Column(name = "price", columnDefinition = "NUMERIC(9, 2)")
    var value: BigDecimal?,

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    var currency: CurrencyEntity?
)
