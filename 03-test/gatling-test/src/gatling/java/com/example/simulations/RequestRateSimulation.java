package com.example.simulations;

import io.gatling.javaapi.core.Body;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.http.HeaderValues.ApplicationJson;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.internal.HttpCheckBuilders.status;

public class RequestRateSimulation extends Simulation {

    private static final String BOOK_SERVICE_URL = "http://localhost:8090";
    private static final String SEARCH_BOOKS_URL = "/api/v1/books/search?page=0";

    HttpProtocolBuilder httpProtocol = http.baseUrl(BOOK_SERVICE_URL)
        .acceptHeader(ApplicationJson())
        .contentTypeHeader(ApplicationJson());

    Body payload = StringBody("""
        {
          "genre": "HEALTH"
        }
        """);

    ScenarioBuilder scenario = scenario("Test request rate").exec(
        http("searchBooks")
            .post(SEARCH_BOOKS_URL).body(payload)
            .check(status().is(200))
    );

    {
        setUp(scenario.injectOpen(
            constantUsersPerSec(50).during(70),
            constantUsersPerSec(10).during(70),
            constantUsersPerSec(50).during(70),
            constantUsersPerSec(10).during(70),
            constantUsersPerSec(50).during(70),
            constantUsersPerSec(10).during(70)
        )).protocols(httpProtocol)
            .assertions(global().successfulRequests().percent().is(100.00));
    }
}