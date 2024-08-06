package com.example.orderservice.repository

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "user_id")
    val userId: UUID,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    val status: StatusEntity,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    val items: Set<ItemEntity>,

    @Column(name = "total_quantity")
    val totalQuantity: Int,

    @Column(name = "total_price", columnDefinition = "NUMERIC")
    val totalPrice: BigDecimal,

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    val currency: CurrencyEntity,

    @Column(name = "created_at")
    val createdAt: OffsetDateTime,

    @Column(name = "updated_at")
    val updatedAt: OffsetDateTime
)

data class ItemEntity(
    val id: UUID,
    val category: ItemCategoryEntity,
    val quantity: Int,
    val price: BigDecimal,
    val currency: ItemCurrencyEntity
)

enum class ItemCategoryEntity {
    BOOK
}

enum class ItemCurrencyEntity {
    RUB, EUR
}

enum class StatusEntity {
    CREATED,
    IN_PROGRESS,
    DECLINED,  //отклонен системой, например, из-за недостатка средств
    CANCELLED, //отменен пользователем
    COMPLETED
}

enum class CurrencyEntity {
    RUB, EUR
}