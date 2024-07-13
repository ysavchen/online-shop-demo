package com.example.bookservice.service

import java.util.*

class BookNotFoundException(id: UUID) : RuntimeException("Book not found by $id")
class UnsupportedSortingException(sortBy: String) : RuntimeException("Sorting by $sortBy is not supported")
class DuplicateRequestException(key: UUID, entity: String, entityId: UUID) : RuntimeException("Duplicate request with idempotencyKey=$key, $entity already created (id=$entityId)")