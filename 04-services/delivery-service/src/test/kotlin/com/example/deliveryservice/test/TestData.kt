package com.example.deliveryservice.test

import com.example.deliveryservice.kafka.client.model.Address
import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequest
import com.example.deliveryservice.kafka.client.model.Type
import com.example.deliveryservice.repository.entity.AddressEntity
import com.example.deliveryservice.repository.entity.DeliveryEntity
import com.example.deliveryservice.repository.entity.StatusEntity
import com.example.deliveryservice.repository.entity.TypeEntity
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object DeliveryTestData {

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
        country = randomAlphabetic(10),
        city = randomAlphabetic(10),
        street = randomAlphabetic(10),
        building = randomNumeric(2).toString()
    )

    fun addressEntity() = AddressEntity(
        country = randomAlphabetic(10),
        city = randomAlphabetic(10),
        street = randomAlphabetic(10),
        building = randomNumeric(2).toString()
    )
}