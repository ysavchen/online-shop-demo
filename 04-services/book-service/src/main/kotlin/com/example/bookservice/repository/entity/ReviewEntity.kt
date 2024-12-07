package com.example.bookservice.repository.entity

import com.example.online.shop.model.Title
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "reviews")
data class ReviewEntity(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "title")
    val title: Title?,

    @Column(name = "review_text")
    val reviewText: String?,

    @Column(name = "author", nullable = false)
    val author: String,

    @Column(name = "rating", nullable = false)
    val rating: BigDecimal,

    @Column(name = "book_fk", nullable = false)
    val bookFk: UUID
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReviewEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}