package org.accmanager.service.integration.cucumber.steps.common;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;

public class TestBase {

    @Value("${system.app.url:http://localhost:}")
    private String appUrl;

    @LocalServerPort
    private int appPort;

    public String getAppUrl() {
        return appUrl;
    }

    public int getAppPort() {
        return appPort;
    }

    public String getAppUrlAndPort() {
        return getAppUrl() + getAppPort();
    }

    public static String schemasDir = "src/test/resources/schemas/";

    protected static String endpoint;
    protected static String gspAccessToken;
    protected static RequestSpecification request;
    protected static Response response;
    protected static HashMap<String, String> formParam;
}
