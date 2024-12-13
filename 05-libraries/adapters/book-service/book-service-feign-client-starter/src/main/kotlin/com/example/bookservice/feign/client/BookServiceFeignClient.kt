package com.example.bookservice.feign.client

import com.example.bookservice.feign.client.model.Book
import feign.Param
import feign.RequestLine
import org.springframework.cloud.openfeign.FeignClient
import java.util.*

@FeignClient
interface BookServiceFeignClient {

    @RequestLine("GET /api/v1/books/{bookId}")
    fun getBookById(@Param("bookId") bookId: UUID): Book

}