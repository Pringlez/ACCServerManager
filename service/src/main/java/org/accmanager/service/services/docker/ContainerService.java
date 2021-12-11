package org.accmanager.service.services.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import org.accmanager.model.*;
import org.accmanager.service.services.files.DirectoryReadWriteService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.ExceptionEnum.*;
import static org.accmanager.service.enums.FilesEnum.*;
import static org.accmanager.service.enums.PathsEnum.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class ContainerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerService.class);

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
        createDirectories(instance);
        writeInstanceConfiguration(instance);
        copyExecutable(instance.getId());
        return dockerClient.createContainerCmd(instance.getDockerImage())
                .withCmd(Arrays.asList("--net=host", "--restart unless-stopped"))
                .withName(instance.getId())
                .withBinds(Bind.parse(format(PATH_HOST_SERVER_INSTANCE.toString(),
                        fileReadWriteService.getDockerUsername(), instance.getId()) + PATH_CONTAINER))
                .withExposedPorts(ExposedPort.udp(instance.getConfig().getUdpPort()),
                        ExposedPort.tcp(instance.getConfig().getTcpPort())).exec();
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

    public void killAndRemoveContainer(String instanceId) {
        fileReadWriteService.deleteInstanceDirectoryConfigsAndFiles(instanceId);
        attemptKillingContainer(instanceId);
        attemptRemovingContainer(instanceId);
    }

    public InspectContainerResponse inspectContainer(String instanceId) {
        return dockerClient.inspectContainerCmd(instanceId).exec();
    }

    public List<Instance> listOfContainers() {
        List<Instance> instancesList = new ArrayList<>();
        try {
            Optional<List<Path>> accServerDirs = directoryReadWriteService.getAllServerDirectories(
                    fileReadWriteService.getDockerUsername());
            accServerDirs.ifPresent(dirs -> dirs.forEach(
                    dir -> instancesList.add(readInstanceConfiguration(dir.toString()))));
        } catch (Exception ex) {
            LOGGER.warn(ERROR_RETRIEVING_LIST_OF_CONTAINERS.toString());
            throw new DockerException(ERROR_RETRIEVING_LIST_OF_CONTAINERS.toString(), INTERNAL_SERVER_ERROR.value(), ex);
        }
        return instancesList;
    }

    public void writeInstanceConfiguration(Instance instance) {
        fileReadWriteService.writeJsonFile(instance.getId(), EVENT_JSON, instance.getEvent());
        fileReadWriteService.writeJsonFile(instance.getId(), EVENT_RULES_JSON, instance.getEventRules());
        fileReadWriteService.writeJsonFile(instance.getId(), ENTRY_LIST_JSON, instance.getEntriesList());
        fileReadWriteService.writeJsonFile(instance.getId(), ASSIST_RULES_JSON, instance.getAssists());
        fileReadWriteService.writeJsonFile(instance.getId(), BOP_JSON, instance.getBop());
        fileReadWriteService.writeJsonFile(instance.getId(), CONFIGURATION_JSON, instance.getConfig());
        fileReadWriteService.writeJsonFile(instance.getId(), SETTINGS_JSON, instance.getSettings());
    }

    private void attemptKillingContainer(String instanceId) {
        try {
            dockerClient.killContainerCmd(instanceId).exec();
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_KILLING_CONTAINER_INSTANCE_ID.toString(), instanceId));
        }
    }

    private void attemptRemovingContainer(String instanceId) {
        try {
            dockerClient.removeContainerCmd(instanceId).exec();
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_REMOVING_CONTAINER_INSTANCE_ID.toString(), instanceId));
        }
    }

    private void copyExecutable(String instanceId) {
        try {
            fileReadWriteService.copyExecutable(instanceId);
        } catch (Exception ex) {
            LOGGER.warn(ERROR_COPYING_EXECUTABLE.toString());
        }
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

    private void createDirectories(Instance instance) {
        fileReadWriteService.createNewDirectory(format(PATH_HOST_SERVER_INSTANCE_CFG.toString(),
                fileReadWriteService.getDockerUsername(), instance.getId()));
        fileReadWriteService.createNewDirectory(format(PATH_HOST_SERVER_INSTANCE_EXECUTABLE.toString(),
                fileReadWriteService.getDockerUsername(), instance.getId()));
    }
}
