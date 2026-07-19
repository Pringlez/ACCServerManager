package org.accmanager.service.integration;

import org.accmanager.service.services.dao.InstanceDaoService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class BaseIT {

    @Autowired
    public WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @MockBean
    public FileReadWriteService fileReadWriteService;

    @MockBean
    public org.accmanager.service.services.control.ServerControl serverControl;

    @MockBean
    public InstanceDaoService instanceDaoService;

    @BeforeEach
    public void setup() {
        org.accmanager.model.Instance dummy = new org.accmanager.model.Instance();
        dummy.setId("test-instance");

        org.accmanager.model.Settings settings = new org.accmanager.model.Settings();
        settings.setServerName("Test Instance");
        dummy.setSettings(settings);

        org.accmanager.model.Config config = new org.accmanager.model.Config();
        config.setTcpPort(9232);
        config.setUdpPort(9231);
        dummy.setConfig(config);

        Mockito.when(serverControl.getDaoService()).thenReturn(instanceDaoService);
        Mockito.when(instanceDaoService.listOfInstances()).thenReturn(Collections.singletonList(dummy));
        Mockito.when(instanceDaoService.readInstanceConfiguration(Mockito.anyString())).thenReturn(dummy);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
}
