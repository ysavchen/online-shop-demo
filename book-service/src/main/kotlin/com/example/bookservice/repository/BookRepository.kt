package com.example.bookservice.repository

import com.example.bookservice.repository.entity.BookEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookRepository : JpaRepository<BookEntity, UUID>