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
    open val meta: RequestMeta
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
    JsonSubTypes.Type(value = DeliveryDataReply::class, name = MessageTypeName.DELIVERY_DATA_REPLY),
    JsonSubTypes.Type(value = ClientErrorReply::class, name = MessageTypeName.CLIENT_ERROR_REPLY)
)
sealed class ReplyDeliveryMessage(
    open val data: Data,
    open val meta: ReplyMeta
) {
    /**
     * typeName is used by Jackson to deserialize Json to subtype
     */
    @get:JsonProperty("@type")
    val typeName: String
        get() = meta.type.name
}

interface Data
data class RequestMeta(val service: String, val type: MessageType, val version: Int)
data class ReplyMeta(val service: String, val type: MessageType, val statusCode: StatusCode, val version: Int)

enum class MessageType(@JsonValue val messageTypeName: String) {
    CREATE_DELIVERY_REQUEST(MessageTypeName.CREATE_DELIVERY_REQUEST),
    DELIVERY_DATA_REPLY(MessageTypeName.DELIVERY_DATA_REPLY),
    CLIENT_ERROR_REPLY(MessageTypeName.CLIENT_ERROR_REPLY)
}

object MessageTypeName {
    const val CREATE_DELIVERY_REQUEST = "CREATE_DELIVERY_REQUEST"
    const val DELIVERY_DATA_REPLY = "DELIVERY_DATA_REPLY"
    const val CLIENT_ERROR_REPLY = "CLIENT_ERROR_REPLY"
}

enum class StatusCode {
    SUCCESS, ERROR
}

data class CreateDeliveryRequest(
    override val data: CreateDelivery,
    override val meta: RequestMeta = RequestMeta(
        service = "order-service",
        type = MessageType.CREATE_DELIVERY_REQUEST,
        version = 1
    )
) : RequestDeliveryMessage(data = data, meta = meta)

data class GetDeliveryByIdRequest(
    override val data: GetDeliveryById,
    override val meta: RequestMeta = RequestMeta(
        service = "order-service",
        type = MessageType.CREATE_DELIVERY_REQUEST,
        version = 1
    )
) : RequestDeliveryMessage(data = data, meta = meta)

data class DeliveryDataReply(
    override val data: Delivery,
    override val meta: ReplyMeta = ReplyMeta(
        service = "delivery-service",
        type = MessageType.DELIVERY_DATA_REPLY,
        statusCode = StatusCode.SUCCESS,
        version = 1
    )
) : ReplyDeliveryMessage(data = data, meta = meta)

data class ClientErrorReply(
    override val data: ServiceError,
    override val meta: ReplyMeta = ReplyMeta(
        service = "delivery-service",
        type = MessageType.CLIENT_ERROR_REPLY,
        statusCode = StatusCode.ERROR,
        version = 1
    )
) : ReplyDeliveryMessage(data = data, meta = meta)