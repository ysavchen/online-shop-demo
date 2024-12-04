package com.example.bookservice.repository

import com.example.bookservice.repository.entity.ProcessedRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProcessedRequestRepository : JpaRepository<ProcessedRequestEntity, UUID>