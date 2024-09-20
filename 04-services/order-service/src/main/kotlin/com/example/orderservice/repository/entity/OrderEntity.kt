package com.example.orderservice.repository.entity

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
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: StatusEntity,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    val items: Set<ItemEntity>,

    @Column(name = "total_quantity", nullable = false)
    val totalQuantity: Int,

    @Embedded
    val totalPrice: TotalPriceEntity,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime
)

data class ItemEntity(
    val id: UUID,
    val category: ItemCategoryEntity,
    val quantity: Int,
    val price: ItemPriceEntity
)

data class ItemPriceEntity(
    val value: BigDecimal,
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
    IN_PROGRESS,  //заказ взят в обработку
    DECLINED,  //отклонен системой, например, из-за недостатка средств
    CANCELLED, //отменен пользователем
    COMPLETED
}

enum class CurrencyEntity {
    RUB, EUR
}