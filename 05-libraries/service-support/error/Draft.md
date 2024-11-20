### library
Errors.kt
```kotlin
data class ErrorResponse(
    val errorId: UUID = UUID.randomUUID(),
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    val path: String,
    val code: String,
    val message: String
)

enum class ErrorCode {
    RESOURCE_NOT_FOUND,
    REQUEST_VALIDATION_ERROR,
    REQUEST_ALREADY_PROCESSED,
    DOWNSTREAM_SERVICE_UNAVAILABLE_ERROR,
    INTERNAL_SERVER_ERROR
}
```
Exceptions.kt
```kotlin
open class ServiceException(
    message: String,
    val errorCode: String,
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
    
enum class ErrorCode {
    INVALID_ORDER_STATUS_UPDATE
}
```