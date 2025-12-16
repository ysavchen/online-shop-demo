package com.example.bookservice.repository.entity

import com.example.online.shop.model.Description
import com.example.online.shop.model.Isbn
import com.example.online.shop.model.Quantity
import com.example.online.shop.model.Title
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UuidGenerator
import org.hibernate.type.SqlTypes
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "books")
data class BookEntity(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @NaturalId
    @Column(name = "isbn", nullable = false, unique = true)
    val isbn: Isbn,

    @Column(name = "title", nullable = false)
    val title: Title,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "authors", columnDefinition = "text[]", nullable = false)
    val authors: Array<String>,

    @Column(name = "description")
    val description: Description?,

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    val genre: GenreEntity,

    @Column(name = "release_date")
    var releaseDate: LocalDate?,

    @Column(name = "quantity", nullable = false)
    var quantity: Quantity,

    @Embedded
    var price: PriceEntity
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookEntity

        return isbn == other.isbn
    }

    override fun hashCode(): Int {
        return isbn.hashCode()
    }
}

enum class CurrencyEntity {
    RUB, EUR
}

enum class GenreEntity {
    HEALTH, TRAVEL, FICTION
}