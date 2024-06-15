package com.example.bookservice.service

import java.util.*

class BookNotFoundException(id: UUID) : RuntimeException("Book not found by $id")