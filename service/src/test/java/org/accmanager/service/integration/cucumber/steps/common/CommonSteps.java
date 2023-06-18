package org.accmanager.service.integration.cucumber.steps.common;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.config.HttpClientConfig;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ConnectionConfig.connectionConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;

public class CommonSteps extends TestBase {

    @Given("the endpoint is {string}")
    public void theEndpointIs(String endpointToTest) {
        config = newConfig().connectionConfig(connectionConfig().closeIdleConnectionsAfterEachResponse());
        config.getHttpClientConfig().reuseHttpClientInstance();
        config.httpClient(HttpClientConfig.httpClientConfig()
                .setParam(CONNECTION_TIMEOUT, 3000)
                .setParam(SO_TIMEOUT, 3000));
        endpoint = endpointToTest;
        request = given().config(config);
        formParam = new HashMap<>();
    }

    @And("a query parameter {string} with a value {string} is included in the request")
    public void aQueryParameterWithAValueIsIncludedInTheRequest(String key, String value) {
        if (!isEmpty(value)) {
            request = given(request).queryParam(key, value);
        }
    }

    @And("a form parameter with an element {string} with a value {string}")
    public void aFormParameterWithAnElementWithAValue(String key, String value) {
        if (!isEmpty(value)) {
            formParam.put(key, value);
        }
    }

    @And("the form parameter with key {string} is included in the request")
    public void theFormParameterWithKeyIsIncludedInTheRequest(String key) {
        request = given(request).formParam(key, formParam.get(key));
        formParam.clear();
    }

    @When("a GET request is executed we should receive {int} status code")
    public void aGETRequestIsExecutedWeShouldReceiveStatusStatusCode(int statusCode) {
        response = given(request).when().get(getAppUrl() + ":" + getAppPort() + endpoint);
        response.then().assertThat().statusCode(statusCode);
    }

    @When("a POST request is executed we should receive {int} status code")
    public void aPOSTRequestIsExecutedWeShouldReceiveStatusStatusCode(int statusCode) {
        response = given(request).when().post(getAppUrl() + ":" + getAppPort() + endpoint);
        response.then().assertThat().statusCode(statusCode);
    }

    @Then("the response structure should match {string} file in directory {string}")
    public void theResponseStructureShouldMatchFileInDirectory(String schemaFile, String schemaDir) {
        if (!isEmpty(schemaFile)) {
            try {
                response.then().assertThat().body(matchesJsonSchema(new File(new File(schemasDir + schemaDir), schemaFile)));
            } catch (Exception e) {
                throw new RuntimeException("Could not parse schema file and assert API response structure!");
            }
        }
    }

    @And("the response time should be less then or equal to {int} seconds")
    public void theResponseTimeShouldBeLessThenOrEqualToSeconds(int responseTime) {
        assertThat(response.getTimeIn(SECONDS), lessThanOrEqualTo(responseTime * 1000L));
    }

    @And("the response results object should contain {int} elements")
    public void theResponseResultsObjectShouldContainElements(int elementsSize) {
        LinkedHashMap<String, Object> results = response.jsonPath().get("results[0]");
        if (!isEmpty(results)) {
            assertThat(results.keySet(), hasSize(elementsSize));
        }
    }

    @And("the response contains {string} element with value of {string}")
    public void theResponseContainsElementWithValueOf(String elementKey, String elementValue) {
        if (!isEmpty(elementValue)) {
            assertThat(response.jsonPath().get(elementKey).toString(), is(elementValue));
        }
    }
}
