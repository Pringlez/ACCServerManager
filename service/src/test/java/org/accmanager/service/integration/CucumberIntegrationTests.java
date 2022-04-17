package org.accmanager.service.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty",
    "html:target/report/cucumber.html", "json:target/cucumber/cucumber.json", "junit:target/cucumber/cucumber.xml" },
    features = "src/main/resources/features", glue = "classpath:org/accmanager/service/functional", dryRun = false)
public class CucumberIntegrationTests {
}
