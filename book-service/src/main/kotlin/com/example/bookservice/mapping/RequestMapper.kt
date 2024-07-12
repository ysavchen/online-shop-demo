package com.example.bookservice.mapping

import com.example.bookservice.api.rest.model.PageRequestParams
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction

object RequestMapper {

    internal fun PageRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(this.orderBy),
            this.sortBy
        )
}