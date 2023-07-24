package org.accmanager.service.integration.cucumber.steps.common;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.util.HashMap;

public class TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);

    @Value("${system.app.url:http://localhost}")
    private String appUrl;

    @LocalServerPort
    private int appPort;

    protected static final String SCHEMAS_DIR = "src/test/resources/schemas/";
    protected static final String BASE_RESOURCE_DIR = "src/test/resources/";
    protected static final String BASE_REQUESTS_DIR = BASE_RESOURCE_DIR + "requests/";

    protected static String endpoint;
    protected static RequestSpecification request;
    protected static Response response;
    protected static HashMap<String, String> formParam;

    protected static boolean isTestInstancePopulated;

    public String getAppUrl() {
        return appUrl;
    }

    public int getAppPort() {
        return appPort;
    }

    public String getAppUrlAndPort() {
        return getAppUrl() + getAppPort();
    }

    public static File loadScaffoldingFile(String fileName, String fileDir) {
        try {
            return new File(new File(BASE_REQUESTS_DIR + fileDir), fileName);
        } catch (Exception e) {
            LOGGER.error("Could not load scaffolding file: {}", fileName);
            return null;
        }
    }
}
