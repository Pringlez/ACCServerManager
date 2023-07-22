package org.accmanager.service.integration.cucumber.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"accserver.files.directory.override=../", "accserver.executable.name=dummyAccServer.exe"})
public class CucumberSpringConfig {

}
