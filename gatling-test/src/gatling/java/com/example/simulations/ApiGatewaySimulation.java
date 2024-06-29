package com.example.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.internal.HttpCheckBuilders.status;

public class ApiGatewaySimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .check(status().within(200, 429));

    ScenarioBuilder getBooksScenario = scenario("Test API Gateway")
            .exec(http("get books").get("/book-service/books"));

    {
        setUp(getBooksScenario.injectOpen(constantUsersPerSec(50).during(60)))
                .protocols(httpProtocol)
                .assertions(global().successfulRequests().percent().gt(60.0));
    }
}