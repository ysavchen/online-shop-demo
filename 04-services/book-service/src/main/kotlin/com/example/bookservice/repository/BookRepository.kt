package com.example.bookservice.repository

import com.example.bookservice.api.rest.model.BookSearchRequest
import com.example.bookservice.api.rest.model.Genre
import com.example.bookservice.repository.entity.BookEntity
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
            titleLikeIgnoreCase(request?.query?.value)
                .and(genreEqualIgnoreCase(request?.genre))
                .and(minPriceGreaterThanOrEqualTo(request?.minPrice?.value))
                .and(maxPriceLessThanOrEqualTo(request?.maxPrice?.value))

        private fun titleLikeIgnoreCase(title: String?): Specification<BookEntity> =
            Specification { root, _, cb ->
                if (title.isNullOrBlank()) null
                else cb.like(cb.lower(root.get(BookEntity::title.name)), "%${title.lowercase()}%")
            }

        private fun genreEqualIgnoreCase(genre: Genre?): Specification<BookEntity> =
            Specification { root, _, cb ->
                genre?.let {
                    cb.equal(cb.lower(root.get(BookEntity::genre.name)), genre.name.lowercase())
                }
            }

        private fun minPriceGreaterThanOrEqualTo(minPrice: BigDecimal?): Specification<BookEntity> =
            Specification { root, _, cb ->
                minPrice?.let {
                    cb.greaterThanOrEqualTo(root.get(BookEntity::price.name), minPrice)
                }
            }

        private fun maxPriceLessThanOrEqualTo(maxPrice: BigDecimal?): Specification<BookEntity> =
            Specification { root, _, cb ->
                maxPrice?.let {
                    cb.lessThanOrEqualTo(root.get(BookEntity::price.name), maxPrice)
                }
            }
    }
}