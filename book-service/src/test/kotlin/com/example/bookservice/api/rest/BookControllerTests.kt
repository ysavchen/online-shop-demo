package com.example.bookservice.api.rest

import com.example.bookservice.PostgresConfiguration
import com.example.bookservice.TestData.bookEntity
import com.example.bookservice.repository.BookRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.aot.DisabledInAotMode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@Import(PostgresConfiguration::class)
@AutoConfigureMockMvc
@DisabledInAotMode
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookRepository: BookRepository

    @Test
    fun `search books`() {
        mockMvc.post("/api/v1/books/search") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    fun `get book by id`() {
        val bookEntity = bookRepository.save(bookEntity())

        mockMvc.get("/api/v1/books/${bookEntity.id}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

}