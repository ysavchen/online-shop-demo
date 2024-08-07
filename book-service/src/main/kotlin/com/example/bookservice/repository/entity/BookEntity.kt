package com.example.bookservice.repository.entity

import io.hypersistence.utils.hibernate.type.array.StringArrayType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "books")
data class BookEntity(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "title", nullable = false)
    val title: String,

    @Type(StringArrayType::class)
    @Column(name = "authors", columnDefinition = "text[]", nullable = false)
    val authors: Array<String>,

    @Column(name = "description")
    val description: String?,

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    val genre: GenreEntity,

    @Column(name = "release_date")
    var releaseDate: LocalDate?,

    @Column(name = "quantity")
    var quantity: Int,

    @Column(name = "price", columnDefinition = "NUMERIC")
    var price: BigDecimal?,

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    var currency: CurrencyEntity?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (!authors.contentEquals(other.authors)) return false
        if (genre != other.genre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + authors.contentHashCode()
        result = 31 * result + genre.hashCode()
        return result
    }
}

enum class CurrencyEntity {
    RUB, EUR
}

enum class GenreEntity {
    HEALTH, TRAVEL, FICTION
}