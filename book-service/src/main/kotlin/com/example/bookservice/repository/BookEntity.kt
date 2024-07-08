package com.example.bookservice.repository

import io.hypersistence.utils.hibernate.type.array.StringArrayType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "books")
data class BookEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "title", nullable = false)
    val title: String,

    @Type(StringArrayType::class)
    @Column(name = "authors", columnDefinition = "text[]", nullable = false)
    val authors: Array<String>,

    @Column(name = "description", nullable = false)
    val description: String?,

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    val genre: GenreEntity,

    @Column(name = "price", columnDefinition = "NUMERIC", nullable = false)
    val price: BigDecimal?,

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    val currency: CurrencyEntity?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (!authors.contentEquals(other.authors)) return false
        if (description != other.description) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + authors.contentHashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + price.hashCode()
        return result
    }
}

enum class CurrencyEntity {
    RUB
}

enum class GenreEntity {
    HEALTH, TRAVEL, FICTION
}