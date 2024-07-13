package com.example.bookservice.repository

import com.example.bookservice.repository.entity.ReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReviewRepository : JpaRepository<ReviewEntity, UUID>