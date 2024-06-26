package com.example.bookservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookRepository : JpaRepository<BookEntity, UUID>