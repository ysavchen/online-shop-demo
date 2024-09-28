package com.example.orderservice.mapping

import com.example.orderservice.api.rest.OrderRequestParams
import com.example.orderservice.api.rest.RequestValidationException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction

object RequestMapper {

    fun OrderRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(parseOrderBy(this.orderBy)),
            parseSortBy(this.sortBy)
        )

    private fun parseSortBy(sortBy: String): String =
        when (sortBy) {
            "created_at" -> "createdAt"
            else -> throw RequestValidationException("Sorting by $sortBy is not supported")
        }

    private fun parseOrderBy(orderBy: String): String =
        when (orderBy) {
            "asc", "ASC" -> "ASC"
            "desc", "DESC" -> "DESC"
            else -> throw RequestValidationException("Ordering by $orderBy is not supported")
        }
}