package com.example.bookservice

import java.util.*

class BookNotFoundException(id: UUID) : RuntimeException("Book not found by $id")
class UnsupportedSortingException(sortBy: String) : RuntimeException("Sorting by $sortBy is not supported")