package demo.api.client;

import demo.api.config.Config;
import demo.api.services.Users;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.config.HttpClientConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClient {
    private static RequestSpecification requestSpec;

    public static RequestSpecification getRequestSpec() {

        if (requestSpec == null) {
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
                    .addFilter(new ErrorLoggingFilter())
                    .addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter())
                    .build();
        }

        return requestSpec;
    }
}
