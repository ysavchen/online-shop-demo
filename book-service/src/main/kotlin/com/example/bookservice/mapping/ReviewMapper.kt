package com.example.bookservice.mapping

import com.example.bookservice.api.rest.model.Review
import com.example.bookservice.repository.entity.ReviewEntity
import org.springframework.data.domain.Page
import org.springframework.data.web.PagedModel

object ReviewMapper {

    internal fun ReviewEntity.toModel() = Review(
        id = id!!,
        title = title,
        reviewText = reviewText,
        author = author,
        rating = rating
    )

    internal fun Page<ReviewEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )
}