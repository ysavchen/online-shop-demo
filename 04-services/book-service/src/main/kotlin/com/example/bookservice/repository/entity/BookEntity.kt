package com.example.bookservice.repository.entity

import com.example.online.shop.model.Isbn
import io.hypersistence.utils.hibernate.type.array.StringArrayType
import jakarta.persistence.*
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
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

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

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