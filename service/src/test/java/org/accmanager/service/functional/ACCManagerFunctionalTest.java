package org.accmanager.service.functional;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty",
    "html:target/cucumber", "json:target/cucumber/cucumber.json", "junit:target/cucumber/cucumber.xml" },
    features = "src/main/resources/features", glue = "classpath:org/accmanager/service/functional", strict = true, dryRun = false)
public class ACCManagerFunctionalTest {
}
