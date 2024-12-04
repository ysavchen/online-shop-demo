package com.example.deliveryservice.kafka.client.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "@type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = GetDeliveryByIdRequest::class, name = MessageTypeName.GET_DELIVERY_BY_ID_REQUEST),
    JsonSubTypes.Type(value = GetDeliveryByOrderIdRequest::class, name = MessageTypeName.GET_DELIVERY_BY_ORDER_ID_REQUEST),
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
    JsonSubTypes.Type(value = DeliveryNotFoundErrorReply::class, name = MessageTypeName.DELIVERY_NOT_FOUND_ERROR_REPLY),
    JsonSubTypes.Type(value = DuplicateMessageErrorReply::class, name = MessageTypeName.DUPLICATE_MESSAGE_ERROR_REPLY)
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
    GET_DELIVERY_BY_ID_REQUEST(MessageTypeName.GET_DELIVERY_BY_ID_REQUEST),
    GET_DELIVERY_BY_ORDER_ID_REQUEST(MessageTypeName.GET_DELIVERY_BY_ORDER_ID_REQUEST),
    CREATE_DELIVERY_REQUEST(MessageTypeName.CREATE_DELIVERY_REQUEST),
    DELIVERY_DATA_REPLY(MessageTypeName.DELIVERY_DATA_REPLY),
    DELIVERY_NOT_FOUND_ERROR_REPLY(MessageTypeName.DELIVERY_NOT_FOUND_ERROR_REPLY),
    DUPLICATE_MESSAGE_ERROR_REPLY(MessageTypeName.DUPLICATE_MESSAGE_ERROR_REPLY)
}

object MessageTypeName {
    const val GET_DELIVERY_BY_ID_REQUEST = "GET_DELIVERY_BY_ID_REQUEST"
    const val GET_DELIVERY_BY_ORDER_ID_REQUEST = "GET_DELIVERY_BY_ORDER_ID_REQUEST"
    const val CREATE_DELIVERY_REQUEST = "CREATE_DELIVERY_REQUEST"
    const val DELIVERY_DATA_REPLY = "DELIVERY_DATA_REPLY"
    const val DELIVERY_NOT_FOUND_ERROR_REPLY = "DELIVERY_NOT_FOUND_ERROR_REPLY"
    const val DUPLICATE_MESSAGE_ERROR_REPLY = "DUPLICATE_MESSAGE_ERROR_REPLY"
}

enum class StatusCode {
    SUCCESS, ERROR
}

data class GetDeliveryByIdRequest(
    override val data: GetDeliveryById,
    override val meta: RequestMeta = RequestMeta(
        service = "order-service",
        type = MessageType.GET_DELIVERY_BY_ID_REQUEST,
        version = 1
    )
) : RequestDeliveryMessage(data = data, meta = meta) {
    constructor(deliveryId: UUID) : this(GetDeliveryById(deliveryId))
}

data class GetDeliveryByOrderIdRequest(
    override val data: GetDeliveryByOrderId,
    override val meta: RequestMeta = RequestMeta(
        service = "order-service",
        type = MessageType.GET_DELIVERY_BY_ORDER_ID_REQUEST,
        version = 1
    )
) : RequestDeliveryMessage(data = data, meta = meta) {
    constructor(orderId: UUID) : this(GetDeliveryByOrderId(orderId))
}

data class CreateDeliveryRequest(
    override val data: CreateDelivery,
    override val meta: RequestMeta = RequestMeta(
        service = "order-service",
        type = MessageType.CREATE_DELIVERY_REQUEST,
        version = 1
    )
) : RequestDeliveryMessage(data = data, meta = meta) {
    constructor(type: Type, address: Address, orderId: UUID) : this(CreateDelivery(type, address, orderId))
}

data class DeliveryDataReply(
    override val data: Delivery,
    override val meta: ReplyMeta = ReplyMeta(
        service = "delivery-service",
        type = MessageType.DELIVERY_DATA_REPLY,
        statusCode = StatusCode.SUCCESS,
        version = 1
    )
) : ReplyDeliveryMessage(data = data, meta = meta)

data class DeliveryNotFoundErrorReply(
    override val data: DeliveryNotFoundError,
    override val meta: ReplyMeta = ReplyMeta(
        service = "delivery-service",
        type = MessageType.DELIVERY_NOT_FOUND_ERROR_REPLY,
        statusCode = StatusCode.ERROR,
        version = 1
    )
) : ReplyDeliveryMessage(data = data, meta = meta) {
    constructor(message: String) : this(DeliveryNotFoundError(message))
}

data class DuplicateMessageErrorReply(
    override val data: DuplicateMessageError,
    override val meta: ReplyMeta = ReplyMeta(
        service = "delivery-service",
        type = MessageType.DUPLICATE_MESSAGE_ERROR_REPLY,
        statusCode = StatusCode.ERROR,
        version = 1
    )
) : ReplyDeliveryMessage(data = data, meta = meta) {
    constructor(messageKey: UUID, resourceId: UUID, resource: String) : this(
        DuplicateMessageError(messageKey, resourceId, resource)
    )
}