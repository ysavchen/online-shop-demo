package com.example.orderservice.repository

import com.example.orderservice.repository.entity.OrderItemEntity
import com.example.orderservice.repository.entity.OrderItemId
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItemEntity, OrderItemId>