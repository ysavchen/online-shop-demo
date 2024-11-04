## adapters

#### book-service-rest-client

1. Add dependency

```
implementation("com.example:book-service-rest-client-starter:1.0.0")
```

2. Add configuration

```
application:
  client:
    book-service:
      http:
        base-url: http://localhost:8090
```

3. Usage

```
@Service
class ExampleService(private val bookServiceClient: BookServiceRestClient) {

    fun getBook(id: UUID) {
        runBlocking {
            val book = bookServiceClient.getBookById(id)
            ...
        }
    }
}
```