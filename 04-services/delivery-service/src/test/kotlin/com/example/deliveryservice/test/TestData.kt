package com.example.deliveryservice.test

import com.example.deliveryservice.kafka.client.model.Address
import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequest
import com.example.deliveryservice.kafka.client.model.Type
import com.example.deliveryservice.repository.entity.AddressEntity
import com.example.deliveryservice.repository.entity.DeliveryEntity
import com.example.deliveryservice.repository.entity.StatusEntity
import com.example.deliveryservice.repository.entity.TypeEntity
import com.example.online.shop.model.Building
import com.example.online.shop.model.City
import com.example.online.shop.model.Country
import com.example.online.shop.model.Street
import org.apache.commons.lang3.RandomStringUtils
import java.time.OffsetDateTime
import java.util.*

object DeliveryTestData {

    private val randomString = RandomStringUtils.insecure()

    fun createDeliveryRequest() = CreateDeliveryRequest(
        type = nextValue<Type>(),
        address = address(),
        orderId = UUID.randomUUID()
    )

    fun deliveryEntity() = DeliveryEntity(
        type = nextValue<TypeEntity>(),
        date = randomLocalDate(),
        address = addressEntity(),
        status = nextValue<StatusEntity>(),
        orderId = UUID.randomUUID(),
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )

    fun address() = Address(
        country = Country(randomString.nextAlphabetic(10)),
        city = City(randomString.nextAlphabetic(10)),
        street = Street(randomString.nextAlphabetic(10)),
        building = Building(randomString.nextNumeric(2).toString())
    )

    fun addressEntity() = AddressEntity(
        country = Country(randomString.nextAlphabetic(10)),
        city = City(randomString.nextAlphabetic(10)),
        street = Street(randomString.nextAlphabetic(10)),
        building = Building(randomString.nextNumeric(2).toString())
    )
}