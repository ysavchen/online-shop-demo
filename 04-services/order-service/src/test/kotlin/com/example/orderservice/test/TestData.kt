package com.example.orderservice.test

import com.example.bookservice.rest.client.model.Book
import com.example.bookservice.rest.client.model.Currency
import com.example.bookservice.rest.client.model.Genre
import com.example.bookservice.rest.client.model.Price
import com.example.online.shop.model.*
import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.entity.*
import com.example.orderservice.test.DeliveryTestData.deliveryRequest
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object BookTestData {

    fun book() = Book(
        id = UUID.randomUUID(),
        isbn = Isbn.valueOf("9781525826689"),
        title = Title.valueOf(randomAlphabetic(15)),
        authors = listOf(Author(randomAlphabetic(10))),
        genre = nextValue<Genre>(),
        releaseDate = randomLocalDate(),
        quantity = Quantity(randomNumeric(3).toInt()),
        price = Price(PriceValue(randomPrice()), nextValue<Currency>())
    )
}

object OrderTestData {

    fun orderEntity(
        status: StatusEntity = nextValue<StatusEntity>(),
        orderItemEntities: Set<OrderItemEntity> = setOf(orderItemEntity())
    ) = OrderEntity(
        userId = UUID.randomUUID(),
        status = status,
        totalQuantity = Quantity(orderItemEntities.sumOf { it.quantity.formattedValue }),
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
        quantity = Quantity(randomNumeric(3).toInt()),
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
        delivery = deliveryRequest()
    )

    fun orderItem(
        currency: ItemCurrency = nextValue<ItemCurrency>()
    ) = OrderItem(
        id = UUID.randomUUID(),
        category = nextValue<ItemCategory>(),
        quantity = Quantity(randomNumeric(3).toInt()),
        price = ItemPrice(
            value = PriceValue(randomPrice()),
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
}

object DeliveryTestData {

    fun deliveryRequest() = DeliveryRequest(
        type = nextValue<DeliveryType>(),
        address = deliveryAddress()
    )

    fun delivery() = Delivery(
        id = UUID.randomUUID(),
        type = nextValue<DeliveryType>(),
        date = randomLocalDate(),
        address = deliveryAddress(),
        status = nextValue<DeliveryStatus>()
    )

    fun deliveryAddress() = DeliveryAddress(
        country = Country(randomAlphabetic(15)),
        city = City(randomAlphabetic(15)),
        street = Street(randomAlphabetic(15)),
        building = Building(randomNumeric(3))
    )
}