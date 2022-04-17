package org.accmanager.service.integration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;

public class HealthSteps {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthSteps.class);

	@Autowired
	private CommonSteps commonSteps;

	@Given("^the health service url is \"([^\"]*)\"$")
	public void the_health_service_url_is(String url) throws Throwable {
		LOGGER.info("URL is: " + url);
	}

	@When("^the client calls /health and response status code of (\\d+)$")
	public void the_client_calls_health(int statusCode) throws Throwable {
		String appUrl = commonSteps.getAppUrl();
		int appServerPort = commonSteps.getAppServerPort();
		given().when().get(appUrl + appServerPort + "/actuator/health").then().statusCode(statusCode);
	}
}
