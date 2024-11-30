package com.example.orderservice.test

import com.example.bookservice.rest.client.model.Book
import com.example.bookservice.rest.client.model.Currency
import com.example.bookservice.rest.client.model.Genre
import com.example.bookservice.rest.client.model.Price
import com.example.online.shop.model.Isbn
import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.entity.*
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object BookTestData {

    fun book() = Book(
        id = UUID.randomUUID(),
        isbn = Isbn.valueOf("9781525826689"),
        title = randomAlphabetic(15),
        authors = listOf(randomAlphabetic(10)),
        genre = nextValue<Genre>(),
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = Price(randomPrice(), nextValue<Currency>())
    )
}

object OrderTestData {

    fun orderEntity(
        status: StatusEntity = nextValue<StatusEntity>(),
        orderItemEntities: Set<OrderItemEntity> = setOf(orderItemEntity())
    ) = OrderEntity(
        userId = UUID.randomUUID(),
        status = status,
        totalQuantity = orderItemEntities.sumOf { it.quantity },
        totalPrice = TotalPriceEntity(
            value = orderItemEntities.sumOf { it.price.value },
            currency = nextValue<CurrencyEntity>()
        ),
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    ).apply { addItems(orderItemEntities) }

    fun orderItemEntity() = OrderItemEntity(
        orderItemId = OrderItemId(UUID.randomUUID()),
        category = nextValue<ItemCategoryEntity>(),
        quantity = randomNumeric(3).toInt(),
        price = ItemPriceEntity(
            value = randomPrice(),
            currency = nextValue<ItemCurrencyEntity>()
        )
    )

    fun createOrderRequest(
        items: Set<OrderItem> = setOf(orderItem())
    ) = CreateOrderRequest(
        userId = UUID.randomUUID(),
        items = items,
        delivery = delivery()
    )

    fun orderItem(
        currency: ItemCurrency = nextValue<ItemCurrency>()
    ) = OrderItem(
        id = UUID.randomUUID(),
        category = nextValue<ItemCategory>(),
        quantity = randomNumeric(3).toInt(),
        price = ItemPrice(
            value = randomPrice(),
            currency = currency
        )
    )

    fun orderItem(book: Book) = OrderItem(
        id = book.id,
        category = ItemCategory.BOOKS,
        quantity = book.quantity,
        price = ItemPrice(
            value = book.price!!.value,
            currency = ItemCurrency.valueOf(book.price!!.currency.name)
        )
    )

    fun delivery() = Delivery(
        type = nextValue<DeliveryType>(),
        address = Address(
            country = randomAlphabetic(15),
            city = randomAlphabetic(15),
            street = randomAlphabetic(15),
            building = randomNumeric(3)
        )
    )
}