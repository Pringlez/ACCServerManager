package org.accmanager.service.integration.steps.health;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.accmanager.service.integration.steps.common.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class HealthSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthSteps.class);

    @Given("^the health service url is \"([^\"]*)\"$")
    public void theHealthServiceUrlIs(String url) {
        LOGGER.info("URL is: " + url);
    }

    @When("^the client calls /health and response status code of (\\d+)$")
    public void theClientCallsHealth(int statusCode) {
        given().when().get(getAppUrlAndPort() + "/actuator/health").then().statusCode(statusCode);
    }
}
