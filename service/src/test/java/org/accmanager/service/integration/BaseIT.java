package org.accmanager.service.integration;

import org.accmanager.service.services.control.executable.ExecutableControlService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class BaseIT {

    @Autowired
    public WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @MockBean
    public FileReadWriteService fileReadWriteService;

    @MockBean
    public ExecutableControlService executableControlService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
}
