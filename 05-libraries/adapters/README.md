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
implementation("com.example:order-service-domain-client-starter:1.0.0")
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
implementation("com.example:order-service-domain-client-starter:1.0.0")
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