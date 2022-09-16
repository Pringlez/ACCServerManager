package org.accmanager.service.config.control;

import com.github.dockerjava.api.DockerClient;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.control.docker.ContainerControlService;
import org.accmanager.service.services.control.executable.ExecutableControlService;
import org.accmanager.service.services.files.DirectoryReadWriteService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerControlConfig {

    @Bean(name = "dockerServerControl")
    @ConditionalOnProperty(prefix = "server", name = "control", havingValue = "docker")
    public ServerControl dockerServerControl(DockerClient dockerClient, DirectoryReadWriteService directoryReadWriteService, FileReadWriteService fileReadWriteService) {
        return new ContainerControlService(dockerClient, directoryReadWriteService, fileReadWriteService);
    }

    @Bean(name = "executableServerControl")
    @ConditionalOnProperty(prefix = "server", name = "control", havingValue = "executable")
    public ServerControl executableServerControl(DirectoryReadWriteService directoryReadWriteService, FileReadWriteService fileReadWriteService) {
        return new ExecutableControlService(directoryReadWriteService, fileReadWriteService);
    }
}
