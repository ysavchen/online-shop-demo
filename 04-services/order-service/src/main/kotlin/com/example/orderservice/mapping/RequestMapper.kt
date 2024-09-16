package com.example.orderservice.mapping

import com.example.orderservice.api.rest.UnsupportedSortingException
import com.example.orderservice.api.rest.OrderRequestParams
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction

object RequestMapper {

    internal fun OrderRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(this.orderBy),
            parseOrderSortBy(this.sortBy)
        )

    private fun parseOrderSortBy(sortBy: String): String =
        when (sortBy) {
            "created_at" -> "createdAt"
            else -> throw UnsupportedSortingException(sortBy)
        }

}