package com.example.bookservice.repository

import com.example.bookservice.api.rest.model.BookSearchRequest
import com.example.bookservice.repository.entity.BookEntity
import com.example.bookservice.repository.entity.BookEntity_
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.math.BigDecimal
import java.util.*

interface BookRepository : JpaRepository<BookEntity, UUID>, JpaSpecificationExecutor<BookEntity> {

    companion object {

        fun searchSpec(request: BookSearchRequest?): Specification<BookEntity> =
            titleLikeIgnoreCase(request?.query)
                .and(genreEqualIgnoreCase(request?.genre))
                .and(minPriceGreaterThanOrEqualTo(request?.minPrice))
                .and(maxPriceLessThanOrEqualTo(request?.maxPrice))

        private fun titleLikeIgnoreCase(title: String?): Specification<BookEntity> =
            Specification { root, _, cb ->
                if (title.isNullOrEmpty()) null
                else cb.like(cb.lower(root.get(BookEntity_.title).`as`(String::class.java)), "%${title.lowercase()}%")
            }

        private fun genreEqualIgnoreCase(genre: String?): Specification<BookEntity> =
            Specification { root, _, cb ->
                if (genre.isNullOrEmpty()) null
                else cb.equal(cb.lower(root.get(BookEntity_.genre).`as`(String::class.java)), genre.lowercase())
            }

        private fun minPriceGreaterThanOrEqualTo(minPrice: BigDecimal?): Specification<BookEntity> =
            Specification { root, _, cb ->
                minPrice?.let {
                    cb.greaterThanOrEqualTo(root.get(BookEntity_.price).`as`(BigDecimal::class.java), minPrice)
                }
            }

        private fun maxPriceLessThanOrEqualTo(maxPrice: BigDecimal?): Specification<BookEntity> =
            Specification { root, _, cb ->
                maxPrice?.let {
                    cb.lessThanOrEqualTo(root.get(BookEntity_.price).`as`(BigDecimal::class.java), maxPrice)
                }
            }
    }
}