package com.example.orderservice.domain.kafka.client.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonValue

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "@type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = OrderCreatedEvent::class, name = EventTypeName.ORDER_CREATED_EVENT_NAME),
    JsonSubTypes.Type(value = OrderUpdatedEvent::class, name = EventTypeName.ORDER_UPDATED_EVENT_NAME)
)
sealed class DomainEvent(
    open val data: Data,
    open val meta: Meta
) {
    /**
     * typeName is used by Jackson to deserialize Json to subtype
     */
    @get:JsonProperty("@type")
    val typeName: String
        get() = meta.type.name
}

interface Data
data class Meta(val service: String, val type: EventType, val version: Int)

enum class EventType(@JsonValue val eventTypeName: String) {
    ORDER_CREATED_EVENT(EventTypeName.ORDER_CREATED_EVENT_NAME),
    ORDER_UPDATED_EVENT(EventTypeName.ORDER_UPDATED_EVENT_NAME)
}

object EventTypeName {
    const val ORDER_CREATED_EVENT_NAME = "ORDER_CREATED_EVENT"
    const val ORDER_UPDATED_EVENT_NAME = "ORDER_UPDATED_EVENT"
}

data class OrderCreatedEvent(
    override val data: Order,
    override val meta: Meta = Meta(service = "order-service", type = EventType.ORDER_CREATED_EVENT, version = 1)
) : DomainEvent(data = data, meta = meta)

data class OrderUpdatedEvent(
    override val data: Order,
    override val meta: Meta = Meta(service = "order-service", type = EventType.ORDER_UPDATED_EVENT, version = 1)
) : DomainEvent(data = data, meta = meta)