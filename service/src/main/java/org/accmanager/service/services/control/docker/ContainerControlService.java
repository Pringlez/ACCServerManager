package org.accmanager.service.services.control.docker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import org.accmanager.model.Instance;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.files.DirectoryReadWriteService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_KILLING_CONTAINER_INSTANCE_ID;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_REMOVING_CONTAINER_INSTANCE_ID;
import static org.accmanager.service.enums.PathsEnum.PATH_CONTAINER;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE;

@Service
public class ContainerControlService extends ServerControl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerControlService.class);

    private final DockerClient dockerClient;

    @Autowired
    public ContainerControlService(DockerClient dockerClient, DirectoryReadWriteService directoryReadWriteService, FileReadWriteService fileReadWriteService) {
        super(directoryReadWriteService, fileReadWriteService);
        this.dockerClient = dockerClient;
    }

    @Override
    public String createInstance(Instance instance) {
        createDirectories(instance);
        writeInstanceConfiguration(instance);
        copyExecutable(instance.getId());
        CreateContainerResponse dockerContainerResponse = dockerClient.createContainerCmd(instance.getDockerImage())
                .withCmd(Arrays.asList("--net=host", "--restart unless-stopped"))
                .withName(instance.getId())
                .withBinds(Bind.parse(format(PATH_HOST_SERVER_INSTANCE.toString(),
                        instance.getId()) + PATH_CONTAINER))
                .withExposedPorts(ExposedPort.udp(instance.getConfig().getUdpPort()),
                        ExposedPort.tcp(instance.getConfig().getTcpPort())).exec();
        ObjectNode containerResponse = buildContainerResponse(instance.getId(), dockerContainerResponse);
        Optional<String> jsonResponse = getJsonResponse(containerResponse);
        return jsonResponse.orElse(format("Could not create container response of instance id: %s", instance.getId()));
    }

    @Override
    public void startInstance(String instanceId) {
        dockerClient.startContainerCmd(instanceId).exec();
    }

    @Override
    public void stopInstance(String instanceId) {
        dockerClient.stopContainerCmd(instanceId).exec();
    }

    @Override
    public void restartInstance(String instanceId) {
        dockerClient.restartContainerCmd(instanceId).exec();
    }

    @Override
    public void deleteConfigFiles(String instanceId) {
        getFileReadWriteService().deleteInstanceDirectoryConfigsAndFiles(instanceId);
        attemptKillingContainer(instanceId);
        attemptRemovingContainer(instanceId);
    }

    @Override
    public String inspectInstance(String instanceId) {
        InspectContainerResponse dockerContainerResponse = dockerClient.inspectContainerCmd(instanceId).exec();
        ObjectNode containerResponse = buildContainerResponse(instanceId, dockerContainerResponse);
        Optional<String> jsonResponse = getJsonResponse(containerResponse);
        return jsonResponse.orElse(format("Could not create container response of instance id: %s", instanceId));
    }

    private ObjectNode buildContainerResponse(String instanceId, CreateContainerResponse dockerContainerResponse) {
        ObjectNode containerResponse = getMapper().createObjectNode();
        containerResponse.put("instanceId", instanceId);
        containerResponse.put("containerId", dockerContainerResponse.getId());
        containerResponse.put("containerWarnings", Arrays.toString(dockerContainerResponse.getWarnings()));
        return containerResponse;
    }

    private ObjectNode buildContainerResponse(String instanceId, InspectContainerResponse dockerContainerResponse) {
        ObjectNode containerResponse = getMapper().createObjectNode();
        containerResponse.put("instanceId", instanceId);
        containerResponse.put("containerId", dockerContainerResponse.getId());
        containerResponse.put("containerStatus", dockerContainerResponse.getState().getStatus());
        return containerResponse;
    }

    private Optional<String> getJsonResponse(ObjectNode containerResponse) {
        try {
            return Optional.ofNullable(getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(containerResponse));
        } catch (JsonProcessingException jpe) {
            LOGGER.error(format("Could not create container response of instance id: %s", containerResponse.get("instanceId")));
        }
        return Optional.empty();
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
}
