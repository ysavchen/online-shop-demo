package com.example.bookservice.repository

import com.example.bookservice.repository.entity.IdempotencyKeyEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IdempotencyKeyRepository : JpaRepository<IdempotencyKeyEntity, UUID>