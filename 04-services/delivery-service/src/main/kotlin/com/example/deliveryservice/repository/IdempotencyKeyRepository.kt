package com.example.deliveryservice.repository

import com.example.deliveryservice.repository.entity.IdempotencyKeyEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IdempotencyKeyRepository : JpaRepository<IdempotencyKeyEntity, UUID>