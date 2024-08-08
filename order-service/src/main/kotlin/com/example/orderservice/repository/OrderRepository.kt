package com.example.orderservice.repository

import com.example.orderservice.repository.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<OrderEntity, UUID>