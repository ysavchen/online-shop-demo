package com.example.bookservice.repository

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "authors")
data class AuthorEntity(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String
)