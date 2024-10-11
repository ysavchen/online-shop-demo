package com.example.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.UUID;

import static io.gatling.http.HeaderValues.ApplicationJson;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.internal.HttpCheckBuilders.status;

public class RequestErrorRatioSimulation extends Simulation {

    private static final String BOOK_SERVICE_URL = "http://localhost:8090";
    private static final String BOOKS_URL = "/api/v1/books";
    private static final String BOOK_ONE_ID = "01907547-c86d-71e5-896f-ba05fddafd2b";
    private static final String BOOK_TWO_ID = "01907547-ef4d-7277-a902-c2a9ffc74e4f";
    private static final String NON_EXISTING_BOOK_ID = UUID.randomUUID().toString();

    HttpProtocolBuilder httpProtocol = http.baseUrl(BOOK_SERVICE_URL)
        .acceptHeader(ApplicationJson())
        .contentTypeHeader(ApplicationJson());

    ScenarioBuilder scenario = scenario("Test request error ratio").exec(
        http("getBookOneById").get(BOOKS_URL + "/" + BOOK_ONE_ID).check(status().is(200)),
        http("getBookTwoById").get(BOOKS_URL + "/" + BOOK_TWO_ID).check(status().is(200)),
        http("getNonExistingBookById").get(BOOKS_URL + "/" + NON_EXISTING_BOOK_ID).check(status().is(404))
    );

    {
        setUp(scenario.injectOpen(
            constantUsersPerSec(10).during(Duration.ofMinutes(5)))
        ).protocols(httpProtocol)
            .assertions(global().successfulRequests().percent().is(100.00));
    }
}