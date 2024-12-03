package com.example.deliveryservice.repository

import com.example.deliveryservice.repository.entity.ProcessedMessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProcessedMessageRepository : JpaRepository<ProcessedMessageEntity, UUID>