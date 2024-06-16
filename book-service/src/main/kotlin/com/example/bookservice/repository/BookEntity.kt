package com.example.bookservice.repository

import io.hypersistence.utils.hibernate.type.array.StringArrayType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
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
    val authors: List<String>,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "price", columnDefinition = "NUMERIC", nullable = false)
    val price: BigDecimal
)