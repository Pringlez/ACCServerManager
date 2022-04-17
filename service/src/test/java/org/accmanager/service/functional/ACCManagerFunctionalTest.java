package org.accmanager.service.functional;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
//import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty",
    "html:target/report/cucumber.html", "json:target/cucumber/cucumber.json", "junit:target/cucumber/cucumber.xml" },
    features = "src/main/resources/features", glue = "classpath:org/accmanager/service/functional", dryRun = false)
//@CucumberContextConfiguration
public class ACCManagerFunctionalTest {
}
