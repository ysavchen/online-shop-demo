## adapters

### book-service-rest-client
1. Add dependency
```
implementation("com.example:book-service-rest-client-starter:1.0.0")
```
2. Add configuration
```
application:
  clients:
    book-service:
      http:
        base-url: http://localhost:8090
```
3. Use BookServiceRestClient in a service
```
@Service
class BookClientService(private val bookServiceClient: BookServiceRestClient) {

    fun getBook(id: UUID) {
        runBlocking {
            val book = bookServiceClient.getBookById(id)
            ...
        }
    }
}
```

### order-service-domain-kafka-client

#### Kafka producer
1. Add dependency
```
implementation("com.example:order-service-domain-client-starter:1.0.0")
```
2. Add configuration
```
application:
  clients:
    order-service:
      kafka.domain:
        connection:
          bootstrap-servers: http://localhost:9092
        producer:
          topic: order-service.domain
```
3. Use DomainOrderKafkaProducer in a service

```
@Service
class DomainEventService(private val kafkaProducer: DomainOrderKafkaProducer) {

    fun send(event: DomainEvent) = kafkaProducer.send(event)
}
```

#### Kafka consumer
1. Add dependency
```
implementation("com.example:order-service-domain-client-starter:1.0.0")
```
2. Add configuration
```
application:
  clients:
    order-service:
      kafka.domain:
        connection:
          bootstrap-servers: http://localhost:9092
        consumer:
          group-id: ${spring.application.name}
          topics: order-service.domain
```
3. Use DomainOrderKafkaConsumer in a service
```
@Component
class DomainOrderKafkaConsumerImpl(private val bookService: BookService) : DomainOrderKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, DomainEvent>) {
        bookService.processEvent(data.value())
    }
}
```