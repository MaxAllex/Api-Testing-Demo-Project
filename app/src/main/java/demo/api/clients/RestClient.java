package demo.api.clients;

import demo.api.config.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.config.HttpClientConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.restassured.AllureRestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClient {
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    private static RequestSpecification requestSpec;

    public static RequestSpecification getRequestSpec() {
        if (requestSpec == null) {
            logger.debug("Initializing RestClient with baseUrl: {}", Config.getBaseUrl());
            RestAssured.defaultParser = Parser.JSON;
            requestSpec = new RequestSpecBuilder()
                    .setBaseUri(Config.getBaseUrl())
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .setConfig(RestAssured.config()
                            .httpClient(HttpClientConfig.httpClientConfig()
                                    .setParam("http.connection.timeout", Config.getTimeout())
                                    .setParam("http.socket.timeout", Config.getTimeout())))
                    .setAuth(RestAssured.oauth2(Config.getAuthToken()))
                    .addFilter(new AllureRestAssured())
                    .addFilter(new ErrorLoggingFilter())
                    .addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter())
                    .build();
            logger.debug("RestClient initialized successfully");
        }

        return requestSpec;
    }
}
