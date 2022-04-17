package org.accmanager.service.integration.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class CommonSteps {

    @Value("${system.app.url:http://localhost:}")
    private String appUrl;

    @LocalServerPort
    private int appServerPort;

    public String getAppUrl() {
        return appUrl;
    }

    public int getAppServerPort() {
        return appServerPort;
    }
}
