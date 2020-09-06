package org.accmanager.service.services.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Volume;
import org.accmanager.model.AssistRules;
import org.accmanager.model.BoP;
import org.accmanager.model.Config;
import org.accmanager.model.EntriesList;
import org.accmanager.model.Event;
import org.accmanager.model.EventRules;
import org.accmanager.model.Instance;
import org.accmanager.model.Settings;
import org.accmanager.service.services.files.DirectoryReaderService;
import org.accmanager.service.services.files.FileReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_CONTAINER_CONFIGS;
import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_HOST_CONFIGS;

@Service
public class ContainerService {

    private final DockerClient dockerClient;
    private final DirectoryReaderService directoryReaderService;
    private final FileReaderService fileReaderService;

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    @Autowired
    public ContainerService(DockerClient dockerClient, DirectoryReaderService directoryReaderService, FileReaderService fileReaderService) {
        this.dockerClient = dockerClient;
        this.directoryReaderService = directoryReaderService;
        this.fileReaderService = fileReaderService;
    }

    public CreateContainerResponse createDockerContainer(Instance instance) {
        return dockerClient.createContainerCmd(instance.getDockerImage())
                .withCmd("--net=host")
                .withName(instance.getId())
                .withVolumes(new Volume(format(VOLUME_PATH_HOST_CONFIGS.toString(), dockerUsername,
                        instance.getId()) + VOLUME_PATH_CONTAINER_CONFIGS.toString()))
                .withExposedPorts(new ExposedPort(instance.getConfig().getTcpPort()),
                        new ExposedPort(instance.getConfig().getUdpPort())).exec();
    }

    public void startContainer(String instanceId) {
        dockerClient.startContainerCmd(instanceId).exec();
    }

    public void stopContainer(String instanceId) {
        dockerClient.stopContainerCmd(instanceId).exec();
    }

    public void restartContainer(String instanceId) {
        dockerClient.restartContainerCmd(instanceId).exec();
    }

    public void killContainer(String instanceId) {
        dockerClient.killContainerCmd(instanceId).exec();
    }

    public InspectContainerResponse inspectContainer(String instanceId) {
        return dockerClient.inspectContainerCmd(instanceId).exec();
    }

    public List<Instance> listOfContainers() {
        Optional<List<Path>> accServerDirs = directoryReaderService.getAllServerDirectories(dockerUsername);
        List<Instance> instancesList = new ArrayList<>();
        accServerDirs.ifPresent(dirs -> dirs.forEach(
                dir -> instancesList.add(buildInstance(dir.toString()))));
        return instancesList;
    }

    private Instance buildInstance(String instanceId) {
        Instance instance = new Instance();
        instance.setId(instanceId);
        instance.setEvent(fileReaderService.readEventFile(instanceId).orElse(new Event()));
        instance.setEventRules(fileReaderService.readEventRulesFile(instanceId).orElse(new EventRules()));
        instance.setEntriesList(fileReaderService.readEntriesListFile(instanceId).orElse(new EntriesList()));
        instance.setAssists(fileReaderService.readAssistRulesFile(instanceId).orElse(new AssistRules()));
        instance.setBop(fileReaderService.readBopFile(instanceId).orElse(new BoP()));
        instance.setConfig(fileReaderService.readConfigurationFile(instanceId).orElse(new Config()));
        instance.setSettings(fileReaderService.readSettingsFile(instanceId).orElse(new Settings()));
        return instance;
    }
}
