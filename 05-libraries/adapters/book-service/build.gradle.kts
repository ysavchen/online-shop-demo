project("book-service-rest-client") {
    dependencies {
    }
}

project("book-service-rest-client-starter") {
    dependencies {
        api(project(":book-service:book-service-rest-client"))
    }
}