package org.accmanager.service.functional;

import org.accmanager.service.ACCManagerApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = ACCManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class ACCManagerFunctionalBaseTest {

	@Value("${system.app.url:http://localhost:}")
	private String appUrl;
	
	@Value("${server.servlet.context-path:}")
	private String appContextPath;

	@Value("${management.endpoints.web.base-path}")
	private String actuatorPath;

	@LocalServerPort
	private int appServerPort;

	public String getAppUrl() {
		return appUrl;
	}

	public String getAppContextPath() {
		return appContextPath;
	}

	public String getActuatorPath() {
		return actuatorPath;
	}

	public int getAppServerPort() {
		return appServerPort;
	}
}
