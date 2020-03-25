package org.accmanager.service.functional;

import static io.restassured.RestAssured.given;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class HealthSteps extends ACCManagerFunctionalBaseTest {

	@Given("^the health service url is \"([^\"]*)\"$")
	public void the_health_service_url_is(String url) throws Throwable {
		System.out.println("URL is: " + url);
	}

	@When("^the client calls /health and response status code of (\\d+)$")
	public void the_client_calls_health(int statusCode) throws Throwable {
		System.out.println("The health URL is: " + getAppUrl() + ":" + getAppServerPort() + getAppContextPath());
		given().when().get("http://localhost:" + getAppServerPort() + "/api/v1/actuator/health").then().statusCode(statusCode);
	}
}
