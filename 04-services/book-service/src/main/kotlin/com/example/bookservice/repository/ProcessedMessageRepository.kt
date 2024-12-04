package com.example.bookservice.repository

import com.example.bookservice.repository.entity.ProcessedMessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProcessedMessageRepository : JpaRepository<ProcessedMessageEntity, UUID>