package com.example.orderservice.mapping

import com.example.orderservice.api.rest.OrderRequestParams
import com.example.orderservice.api.rest.UnsupportedOrderingException
import com.example.orderservice.api.rest.UnsupportedSortingException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction

object RequestMapper {

    internal fun OrderRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(parseOrderBy(this.orderBy)),
            parseSortBy(this.sortBy)
        )

    private fun parseSortBy(sortBy: String): String =
        when (sortBy) {
            "created_at" -> "createdAt"
            else -> throw UnsupportedSortingException(sortBy)
        }

    private fun parseOrderBy(orderBy: String): String =
        when (orderBy) {
            "asc", "ASC" -> "ASC"
            "desc", "DESC" -> "DESC"
            else -> throw UnsupportedOrderingException(orderBy)
        }

}