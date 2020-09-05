package org.accmanager.service.services.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Volume;
import org.accmanager.model.Instance;
import org.accmanager.service.services.files.DirectoryReader;
import org.accmanager.service.services.files.FileReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_CONTAINER_CONFIGS;
import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_HOST_CONFIGS;

@Service
public class ContainerService {

    private static final String GUID_REGEX = "^[^/]+/[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    private final DockerClient dockerClient;
    private final DirectoryReader directoryReader;
    private final FileReaderService fileReaderService;

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    @Autowired
    public ContainerService(DockerClient dockerClient, DirectoryReader directoryReader, FileReaderService fileReaderService) {
        this.dockerClient = dockerClient;
        this.directoryReader = directoryReader;
        this.fileReaderService = fileReaderService;
    }

    public CreateContainerResponse createDockerContainer(Instance instance) {
        return dockerClient.createContainerCmd(instance.getDockerImage())
                .withCmd("--net=host")
                .withName(instance.getId())
                .withVolumes(new Volume(String.format(VOLUME_PATH_HOST_CONFIGS.toString(), dockerUsername,
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
        Optional<String[]> serverDirectories = directoryReader.getAllServerDirectories();
        List<String> accServerDirectories = new ArrayList<>();
        if (serverDirectories.isPresent()) {
            for (String directory : serverDirectories.get()) {
                if (directory.matches(GUID_REGEX)) {
                    accServerDirectories.add(directory);
                }
            }
        }

        List<Instance> instancesList = new ArrayList<>();

        for (String instanceId : accServerDirectories) {
            instancesList.add(buildInstance(instanceId));
        }

        return instancesList;
    }

    private Instance buildInstance(String instanceId) {
        Instance instance = new Instance();
        instance.setEvent(fileReaderService.readEventFile(instanceId).get());
        instance.setEventRules(fileReaderService.readEventRulesFile(instanceId).get());
        instance.setEntriesList(fileReaderService.readEntriesListFile(instanceId).get());
        instance.setAssists(fileReaderService.readAssistRulesFile(instanceId).get());
        instance.setBop(fileReaderService.readBopFile(instanceId).get());
        instance.setConfig(fileReaderService.readConfigurationFile(instanceId).get());
        instance.setSettings(fileReaderService.readSettingsFile(instanceId).get());
        return instance;
    }
}
