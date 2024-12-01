## adapters

### book-service-rest-client
1. Add dependency
```kotlin
implementation("com.example:book-service-rest-client-starter:1.0.0")
```
2. Add configuration
```yaml
application:
  clients:
    book-service:
      http:
        base-url: http://localhost:8090
```
3. Use BookServiceRestClient in a service
```kotlin
@Service
class BookClientService(private val bookServiceClient: BookServiceRestClient) {

    fun getBook(id: UUID) {
        runBlocking {
            val book = bookServiceClient.getBookById(id)
            //business logic
        }
    }
}
```

### order-service-domain-kafka-client

#### Kafka producer
1. Add dependency
```kotlin
implementation("com.example:order-service-domain-kafka-client-starter:1.0.0")
```
2. Add configuration
```yaml
application:
  clients:
    order-service:
      kafka:
        connection:
          bootstrap-servers: http://localhost:9092
        domain.producer:
          topic: order-service.domain
```
3. Use DomainOrderKafkaProducer in a service

```kotlin
@Service
class DomainEventService(private val kafkaProducer: DomainOrderKafkaProducer) {

    fun send(event: DomainEvent) = kafkaProducer.send(event)
}
```

#### Kafka consumer
1. Add dependency
```kotlin
implementation("com.example:order-service-domain-kafka-client-starter:1.0.0")
```
2. Add configuration
```yaml
application:
  clients:
    order-service:
      kafka:
        connection:
          bootstrap-servers: http://localhost:9092
        domain.consumer:
          group-id: ${spring.application.name}
          topics: order-service.domain
```
3. Use DomainOrderKafkaConsumer in a service
```kotlin
@Component
class DomainOrderKafkaConsumerImpl(private val bookService: BookService) : DomainOrderKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, DomainEvent>) {
        bookService.processMessage(data)
    }
}
```

### delivery-service-request-kafka-client
1. Add dependency
```kotlin
implementation("com.example:delivery-service-request-kafka-client-starter:1.0.0")
```
2. Add configuration
```yaml
application:
  clients:
    delivery-service:
      kafka:
        connection:
          bootstrap-servers: http://localhost:9092
        replying.producer:
          request:
            topic: delivery-service.request
          reply:
            group-id-prefix: ${spring.application.name}
            topics: delivery-service.reply
```
3. Use ReplyingDeliveryKafkaProducer in a service
```kotlin
@Service
class DeliveryClientService(private val kafkaProducer: ReplyingDeliveryKafkaProducer) {

    fun createDelivery(request: CreateDeliveryRequest): Delivery {
        val reply = kafkaProducer.sendAndReceive(request).get().value()
        val delivery = when (reply) {
            is DeliveryCreatedReply -> reply.data.toModel()
            //handle replies
        }
        return delivery
    }
}
```

### delivery-service-reply-kafka-client
1. Add dependency
```kotlin
implementation("com.example:delivery-service-reply-kafka-client-starter:1.0.0")
```
2. Add configuration
```yaml
application:
  clients:
    delivery-service:
      kafka:
        connection:
          bootstrap-servers: http://localhost:9092
        replying.consumer:
          request:
            group-id: ${spring.application.name}
            topics: delivery-service.request
          reply:
            topic: delivery-service.reply
```
3. Use ReplyingDeliveryKafkaConsumer in a service
```kotlin
@Component
class ReplyingDeliveryKafkaConsumerImpl(private val deliveryService: DeliveryService) : ReplyingDeliveryKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>): ReplyDeliveryMessage = 
        deliveryService.processMessage(data)
    
}
```