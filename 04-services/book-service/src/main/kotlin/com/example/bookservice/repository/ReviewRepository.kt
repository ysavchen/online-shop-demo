package com.example.bookservice.repository

import com.example.bookservice.api.rest.model.ReviewSearchRequest
import com.example.bookservice.repository.entity.ReviewEntity
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface ReviewRepository : JpaRepository<ReviewEntity, UUID>, JpaSpecificationExecutor<ReviewEntity> {

    companion object {

        fun searchSpec(request: ReviewSearchRequest): Specification<ReviewEntity> = bookIdEqual(request.bookId)

        private fun bookIdEqual(bookId: UUID): Specification<ReviewEntity> =
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>(ReviewEntity::bookFk.name), bookId)
            }
    }
}