package org.accmanager.service.integration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import org.accmanager.service.repository.EventRepository;
import org.accmanager.service.repository.InstancesRepository;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.dao.InstanceDaoService;
import org.accmanager.service.services.files.DirectoryReadWriteService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ServerCreationIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DockerClient dockerClient;

    @MockBean
    private FileReadWriteService fileReadWriteService;

    @MockBean
    private DirectoryReadWriteService directoryReadWriteService;

    @Autowired
    private ServerControl serverControl;

    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InstancesRepository instancesRepository;

    @BeforeEach
    void setUp() {
        // Setup DockerClient mock to return valid responses during create and start
        CreateContainerCmd createContainerCmd = Mockito.mock(CreateContainerCmd.class);
        CreateContainerResponse createContainerResponse = Mockito.mock(CreateContainerResponse.class);
        com.github.dockerjava.api.command.StartContainerCmd startContainerCmd = Mockito.mock(com.github.dockerjava.api.command.StartContainerCmd.class);
        
        Mockito.when(dockerClient.createContainerCmd(anyString())).thenReturn(createContainerCmd);
        Mockito.when(createContainerCmd.withName(anyString())).thenReturn(createContainerCmd);
        Mockito.when(createContainerCmd.withHostConfig(any())).thenReturn(createContainerCmd);
        Mockito.when(createContainerCmd.withExposedPorts(any(com.github.dockerjava.api.model.ExposedPort[].class))).thenReturn(createContainerCmd);
        Mockito.when(createContainerCmd.exec()).thenReturn(createContainerResponse);
        Mockito.when(createContainerResponse.getId()).thenReturn("mocked-container-id");
        Mockito.when(createContainerResponse.getWarnings()).thenReturn(new String[0]);
        
        Mockito.when(dockerClient.startContainerCmd(anyString())).thenReturn(startContainerCmd);
    }

    @Test
    void addServer_Docker_succeedsWithoutNullPointer() throws Exception {
        mockMvc.perform(post("/web/servers/add")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Docker Test Server")
                        .param("tcpPort", "10001")
                        .param("udpPort", "10002")
                        .param("controlType", "DOCKER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/servers"));
    }

    @Test
    void addServer_Native_succeedsWithoutNullPointer() throws Exception {
        mockMvc.perform(post("/web/servers/add")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Native Test Server")
                        .param("tcpPort", "10003")
                        .param("udpPort", "10004")
                        .param("controlType", "NATIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/servers"));
    }

    @Test
    void addServer_withPreset_Laguna_succeeds() throws Exception {
        mockMvc.perform(post("/web/servers/add")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Laguna Preset Server")
                        .param("tcpPort", "10005")
                        .param("udpPort", "10006")
                        .param("controlType", "DOCKER")
                        .param("presetId", "458780f0-8a4c-4bd9-b35b-b26aab2351c4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/servers"));
    }

    @Test
    void addServer_withPreset_Monza_succeeds() throws Exception {
        mockMvc.perform(post("/web/servers/add")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Monza Preset Server")
                        .param("tcpPort", "10007")
                        .param("udpPort", "10008")
                        .param("controlType", "NATIVE")
                        .param("presetId", "preset-monza-id-12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/servers"));
    }

    @Test
    void addServer_withPreset_Spa_succeeds() throws Exception {
        mockMvc.perform(post("/web/servers/add")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Spa Preset Server")
                        .param("tcpPort", "10009")
                        .param("udpPort", "10010")
                        .param("controlType", "DOCKER")
                        .param("presetId", "preset-spa-id-12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/servers"));
    }

    @Test
    void homePage_withEmptyAndNullJsonFiles_rendersSuccessfully() throws Exception {
        // Stub directory list to return a test instance folder
        Mockito.when(directoryReadWriteService.getAllServerDirectories())
                .thenReturn(Optional.of(List.of(Paths.get("test-null-instance"))));

        // Stub file reading to return empty Optionals (simulating corrupted/null JSON files)
        Mockito.when(fileReadWriteService.readJsonFile(anyString(), any(), any()))
                .thenReturn(Optional.empty());

        // Perform GET request to Home page
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
