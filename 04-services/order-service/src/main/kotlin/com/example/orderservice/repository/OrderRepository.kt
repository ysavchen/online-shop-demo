package com.example.orderservice.repository

import com.example.orderservice.api.rest.model.OrderSearchRequest
import com.example.orderservice.repository.entity.OrderEntity
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface OrderRepository : JpaRepository<OrderEntity, UUID>, JpaSpecificationExecutor<OrderEntity> {

    companion object {

        fun searchSpec(request: OrderSearchRequest): Specification<OrderEntity> = userIdEqual(request.userId)

        private fun userIdEqual(userId: UUID): Specification<OrderEntity> =
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>(OrderEntity::userId.name), userId)
            }
    }
}