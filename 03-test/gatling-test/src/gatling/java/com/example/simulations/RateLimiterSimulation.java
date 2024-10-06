package com.example.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.internal.HttpCheckBuilders.status;

public class RateLimiterSimulation extends Simulation {

    private static final String API_GATEWAY_URL = "http://localhost:8080";
    private static final String BOOK_ID = "01907547-c86d-71e5-896f-ba05fddafd2b";

    HttpProtocolBuilder httpProtocol = http.baseUrl(API_GATEWAY_URL)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .check(status().within(200, 429));

    ScenarioBuilder getBookByIdScenario = scenario("Test rate limiter in API Gateway")
            .exec(http("getBookById").get("/api-gateway/api/v1/books/" + BOOK_ID));

    {
        setUp(getBookByIdScenario.injectOpen(
                constantUsersPerSec(10).during(60)
        )).protocols(httpProtocol)
                .assertions(global().successfulRequests().percent().gt(70.0));
    }
}