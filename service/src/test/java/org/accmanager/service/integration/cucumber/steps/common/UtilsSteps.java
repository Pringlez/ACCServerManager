package org.accmanager.service.integration.cucumber.steps.common;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.accmanager.service.integration.cucumber.steps.common.CommonSteps.aHeaderParameterWithAValueIsIncludedInTheRequest;
import static org.accmanager.service.integration.cucumber.steps.common.CommonSteps.aHeaderParameterWithAValueIsIncludedInTheRequestAndBase64Encoded;
import static org.accmanager.service.integration.cucumber.steps.common.CommonSteps.aPOSTRequestBodyIsBuiltFromScaffoldingJsonFileInDirectory;
import static org.accmanager.service.integration.cucumber.steps.common.CommonSteps.theEndpointIs;

public class UtilsSteps extends TestBase {

    @Before
    public void createTestServerInstance() {
        if (!isTestInstancePopulated) {
            theEndpointIs("/api/v1/instances/ae85423a-b502-4833-bcc2-a424d3f8281e");
            aHeaderParameterWithAValueIsIncludedInTheRequestAndBase64Encoded("Authorization", "user-1:vxUdzhqrwt8eqQS7yszq");
            response = given(request).when().get(getAppUrl() + ":" + getAppPort() + endpoint);
            if (HttpStatus.valueOf(response.statusCode()).is4xxClientError()) {
                theEndpointIs("/api/v1/instances");
                aPOSTRequestBodyIsBuiltFromScaffoldingJsonFileInDirectory("createInstance.json", "instances");
                aHeaderParameterWithAValueIsIncludedInTheRequestAndBase64Encoded("Authorization", "user-1:vxUdzhqrwt8eqQS7yszq");
                aHeaderParameterWithAValueIsIncludedInTheRequest("Content-Type", "application/json");
                aHeaderParameterWithAValueIsIncludedInTheRequest("User-Agent", "Cucumber Test");
                response = given(request).body(loadScaffoldingFile("createInstance.json", "instances")).when().post(getAppUrl() + ":" + getAppPort() + endpoint);
                isTestInstancePopulated = true;
            }
        }
    }

    @After
    public void deleteTestServerInstance() {
        theEndpointIs("/api/v1/instances/ae85423a-b502-4833-bcc2-a424d3f8281e");
        aHeaderParameterWithAValueIsIncludedInTheRequestAndBase64Encoded("Authorization", "user-1:vxUdzhqrwt8eqQS7yszq");
        response = given(request).when().delete(getAppUrl() + ":" + getAppPort() + endpoint);
    }
}
