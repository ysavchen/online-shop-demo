package com.example.bookservice.repository.entity

import com.example.online.shop.model.PriceValue
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class PriceEntity(
    @Column(name = "price", columnDefinition = "NUMERIC")
    var value: PriceValue?,

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    var currency: CurrencyEntity?
)
