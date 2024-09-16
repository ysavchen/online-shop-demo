package com.example.orderservice.repository

import com.example.orderservice.repository.entity.IdempotencyKeyEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IdempotencyKeyRepository : JpaRepository<IdempotencyKeyEntity, UUID>