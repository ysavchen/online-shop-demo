package com.example.deliveryservice.kafka.client.model

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
    JsonSubTypes.Type(value = CreateDeliveryRequest::class, name = MessageTypeName.CREATE_DELIVERY_REQUEST)
)
sealed class RequestDeliveryMessage(
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

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "@type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = DeliveryCreatedResponse::class, name = MessageTypeName.DELIVERY_CREATED_RESPONSE)
)
sealed class ResponseDeliveryMessage(
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
data class Meta(val service: String, val type: MessageType, val version: Int)

enum class MessageType(@JsonValue val messageTypeName: String) {
    CREATE_DELIVERY_REQUEST(MessageTypeName.CREATE_DELIVERY_REQUEST),
    DELIVERY_CREATED_RESPONSE(MessageTypeName.DELIVERY_CREATED_RESPONSE)
}

object MessageTypeName {
    const val CREATE_DELIVERY_REQUEST = "CREATE_DELIVERY_REQUEST"
    const val DELIVERY_CREATED_RESPONSE = "DELIVERY_CREATED_RESPONSE"
}

data class CreateDeliveryRequest(
    override val data: CreateDeliveryRequestData,
    override val meta: Meta = Meta(
        service = "order-service", type = MessageType.CREATE_DELIVERY_REQUEST, version = 1
    )
) : RequestDeliveryMessage(data = data, meta = meta)

data class DeliveryCreatedResponse(
    override val data: Delivery,
    override val meta: Meta = Meta(
        service = "delivery-service", type = MessageType.DELIVERY_CREATED_RESPONSE, version = 1
    )
) : ResponseDeliveryMessage(data = data, meta = meta)