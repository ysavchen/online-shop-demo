package com.example.bookservice.repository

import com.example.bookservice.api.rest.model.BookSearchRequest
import com.example.bookservice.api.rest.model.Genre
import com.example.bookservice.repository.entity.BookEntity
import com.example.bookservice.repository.entity.BookEntity_
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal
import java.util.*

interface BookRepository : JpaRepository<BookEntity, UUID>, JpaSpecificationExecutor<BookEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        SELECT b FROM BookEntity b
        WHERE b.id = :id
        """
    )
    fun findByIdWithPessimisticWrite(id: UUID): BookEntity?

    companion object {

        fun searchSpec(request: BookSearchRequest?): Specification<BookEntity> =
            titleLikeIgnoreCase(request?.query?.formattedValue)
                .and(genreEqualIgnoreCase(request?.genre))
                .and(minPriceGreaterThanOrEqualTo(request?.minPrice?.formattedValue))
                .and(maxPriceLessThanOrEqualTo(request?.maxPrice?.formattedValue))

        private fun titleLikeIgnoreCase(title: String?): Specification<BookEntity> =
            Specification { root, _, cb ->
                if (title.isNullOrBlank()) null
                else cb.like(cb.lower(root.get(BookEntity_.title).`as`(String::class.java)), "%${title.lowercase()}%")
            }

        private fun genreEqualIgnoreCase(genre: Genre?): Specification<BookEntity> =
            Specification { root, _, cb ->
                genre?.let {
                    cb.equal(cb.lower(root.get(BookEntity_.genre).`as`(String::class.java)), genre.name.lowercase())
                }
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