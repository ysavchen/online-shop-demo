package com.example.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.http.HeaderValues.ApplicationJson;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.internal.HttpCheckBuilders.status;

public class RateLimiterSimulation extends Simulation {

    private static final String API_GATEWAY_URL = "http://localhost:8080";
    private static final String BOOK_ID = "01907547-c86d-71e5-896f-ba05fddafd2b";
    private static final String GET_BOOK_BY_ID_URL = "/api-gateway/api/v1/books/" + BOOK_ID;

    HttpProtocolBuilder httpProtocol = http.baseUrl(API_GATEWAY_URL)
            .acceptHeader(ApplicationJson())
            .contentTypeHeader(ApplicationJson());

    //сценарий действий виртуального пользователя
    ScenarioBuilder scenario = scenario("Test rate limiter")
            .exec(http("getBookById").get(GET_BOOK_BY_ID_URL)
                    .check(status().in(200, 429)));

    //профиль нагрузки
    {
        setUp(scenario.injectOpen(
                constantUsersPerSec(10).during(60)
        )).protocols(httpProtocol)
                .assertions(global().successfulRequests().percent().gt(70.0));
    }
}