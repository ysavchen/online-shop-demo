package com.example.orderservice.repository

import com.example.orderservice.repository.entity.ProcessedRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProcessedRequestRepository : JpaRepository<ProcessedRequestEntity, UUID>