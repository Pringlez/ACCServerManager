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
import org.accmanager.service.services.files.DirectoryReadWriteService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.FilesEnum.ASSIST_RULES_JSON;
import static org.accmanager.service.enums.FilesEnum.BOP_JSON;
import static org.accmanager.service.enums.FilesEnum.CONFIGURATION_JSON;
import static org.accmanager.service.enums.FilesEnum.ENTRY_LIST_JSON;
import static org.accmanager.service.enums.FilesEnum.EVENT_JSON;
import static org.accmanager.service.enums.FilesEnum.EVENT_RULES_JSON;
import static org.accmanager.service.enums.FilesEnum.SETTINGS_JSON;
import static org.accmanager.service.enums.PathsEnum.PATH_CONTAINER;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE;

@Service
public class ContainerService {

    private final DockerClient dockerClient;
    private final DirectoryReadWriteService directoryReadWriteService;
    private final FileReadWriteService fileReadWriteService;

    @Autowired
    public ContainerService(DockerClient dockerClient, DirectoryReadWriteService directoryReadWriteService, FileReadWriteService fileReadWriteService) {
        this.dockerClient = dockerClient;
        this.directoryReadWriteService = directoryReadWriteService;
        this.fileReadWriteService = fileReadWriteService;
    }

    public CreateContainerResponse createDockerContainer(Instance instance) {
        writeInstanceConfiguration(instance);
        return dockerClient.createContainerCmd(instance.getDockerImage())
                .withCmd("--net=host")
                .withName(instance.getId())
                .withUser(fileReadWriteService.getDockerUsername())
                .withVolumes(new Volume(format(PATH_HOST_SERVER_INSTANCE.toString(),
                        fileReadWriteService.getDockerUsername(), instance.getId()) + PATH_CONTAINER.toString()))
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
        Optional<List<Path>> accServerDirs = directoryReadWriteService.getAllServerDirectories(
                fileReadWriteService.getDockerUsername());
        List<Instance> instancesList = new ArrayList<>();
        accServerDirs.ifPresent(dirs -> dirs.forEach(
                dir -> instancesList.add(readInstanceConfiguration(dir.toString()))));
        return instancesList;
    }

    private void writeInstanceConfiguration(Instance instance) {
        fileReadWriteService.writeJsonFile(instance.getId(), EVENT_JSON, Event.class);
        fileReadWriteService.writeJsonFile(instance.getId(), EVENT_RULES_JSON, EventRules.class);
        fileReadWriteService.writeJsonFile(instance.getId(), ENTRY_LIST_JSON, EntriesList.class);
        fileReadWriteService.writeJsonFile(instance.getId(), ASSIST_RULES_JSON, AssistRules.class);
        fileReadWriteService.writeJsonFile(instance.getId(), BOP_JSON, BoP.class);
        fileReadWriteService.writeJsonFile(instance.getId(), CONFIGURATION_JSON, Config.class);
        fileReadWriteService.writeJsonFile(instance.getId(), SETTINGS_JSON, Settings.class);
    }

    private Instance readInstanceConfiguration(String instanceId) {
        Instance instance = new Instance();
        instance.setId(instanceId);
        instance.setEvent((Event) fileReadWriteService.readJsonFile(instanceId, EVENT_JSON, Event.class).orElse(new Event()));
        instance.setEventRules((EventRules) fileReadWriteService.readJsonFile(instanceId, EVENT_RULES_JSON, EventRules.class).orElse(new EventRules()));
        instance.setEntriesList((EntriesList) fileReadWriteService.readJsonFile(instanceId, ENTRY_LIST_JSON, EntriesList.class).orElse(new EntriesList()));
        instance.setAssists((AssistRules) fileReadWriteService.readJsonFile(instanceId, ASSIST_RULES_JSON, AssistRules.class).orElse(new AssistRules()));
        instance.setBop((BoP) fileReadWriteService.readJsonFile(instanceId, BOP_JSON, BoP.class).orElse(new BoP()));
        instance.setConfig((Config) fileReadWriteService.readJsonFile(instanceId, CONFIGURATION_JSON, Config.class).orElse(new Config()));
        instance.setSettings((Settings) fileReadWriteService.readJsonFile(instanceId, SETTINGS_JSON, Settings.class).orElse(new Settings()));
        return instance;
    }
}
