### library

Exceptions.kt
```kotlin
open class ServiceException(
    message: String,
    val errorCode: ErrorCode,
    val httpStatusCode: HttpStatusCode
) : RuntimeException(message)

class ResourceNotFoundException(message: String) :
    ServiceException(message, ErrorCode.RESOURCE_NOT_FOUND.name, HttpStatus.NOT_FOUND)

class RequestValidationException(message: String) :
    ServiceException(message, ErrorCode.REQUEST_VALIDATION_ERROR.name, HttpStatus.BAD_REQUEST)

class DuplicateRequestException(idempotencyKey: UUID, resourceId: UUID) :
    ServiceException(
        "Duplicate request with idempotencyKey=$idempotencyKey, resource already created with id=$resourceId",
        ErrorCode.REQUEST_ALREADY_PROCESSED.name,
        HttpStatus.CONFLICT
    )

class DownstreamServiceUnavailableException(message: String) :
    BookServiceClientException(message, ErrorCode.DOWNSTREAM_SERVICE_UNAVAILABLE_ERROR.name, HttpStatus.GATEWAY_TIMEOUT)
```

### service
Exceptions.kt
```kotlin
class InvalidOrderStatusUpdate(orderId: UUID, currentStatus: Status, newStatus: Status) :
    ServiceException(
        "Update order with id=$orderId from status $currentStatus to $newStatus is not valid",
        ErrorCode.INVALID_ORDER_STATUS_UPDATE.name,
        HttpStatus.FORBIDDEN
    )
    
enum class ServiceErrorCode : ErrorCode {
    INVALID_ORDER_STATUS_UPDATE
}
```