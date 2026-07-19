package org.accmanager.service.services.control.container;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import org.accmanager.model.Instance;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.dao.InstanceDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_KILLING_CONTAINER_INSTANCE_ID;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_REMOVING_CONTAINER_INSTANCE_ID;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_RESTARTING_CONTAINER;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_STARTING_CONTAINER;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_STOPPING_CONTAINER;
import static org.accmanager.service.enums.PathsEnum.PATH_CONTAINER;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE;

@Service("containerControlService")
public class ContainerControlService extends ServerControl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerControlService.class);

    private final DockerClient dockerClient;

    @Value("${accserver.files.directory.override:}")
    private String accFileDirectoryOverride;

    @Autowired
    public ContainerControlService(DockerClient dockerClient, InstanceDaoService instanceDaoService) {
        super(instanceDaoService);
        this.dockerClient = dockerClient;
    }

    @Override
    public String createInstance(Instance instance) {
        getDaoService().createDirectories(instance);
        getDaoService().writeInstanceConfiguration(instance);
        getDaoService().copyExecutable(instance.getId());

        HostConfig hostConfig = HostConfig.newHostConfig()
                .withNetworkMode("host")
                .withRestartPolicy(RestartPolicy.parse("unless-stopped"))
                .withBinds(Bind.parse(format(accFileDirectoryOverride + PATH_HOST_SERVER_INSTANCE,
                        instance.getId()) + PATH_CONTAINER));

        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(instance.getContainerImage())
                .withName(instance.getId())
                .withHostConfig(hostConfig)
                .withExposedPorts(ExposedPort.udp(instance.getConfig().getUdpPort()),
                        ExposedPort.tcp(instance.getConfig().getTcpPort())).exec();
        ObjectNode containerResponse = buildContainerResponse(instance.getId(), createContainerResponse);
        Optional<String> jsonResponse = getJsonResponse(containerResponse);
        return jsonResponse.orElse(format("Could not create container response of instance id: %s", instance.getId()));
    }

    @Override
    public void startInstance(String instanceId) {
        try {
            dockerClient.startContainerCmd(instanceId).exec();
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_STARTING_CONTAINER.toString(), instanceId, ex));
            throw ex;
        }
    }

    @Override
    public void stopInstance(String instanceId) {
        try {
            dockerClient.stopContainerCmd(instanceId).exec();
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_STOPPING_CONTAINER.toString(), instanceId, ex));
            throw ex;
        }
    }

    @Override
    public void restartInstance(String instanceId) {
        try {
            dockerClient.restartContainerCmd(instanceId).exec();
        } catch (Exception ex) {
            LOGGER.warn(format(ERROR_RESTARTING_CONTAINER.toString(), instanceId, ex));
            throw ex;
        }
    }

    @Override
    public void deleteConfigFiles(String instanceId) {
        getDaoService().getFileReadWriteService().deleteInstanceDirectoryConfigsAndFiles(instanceId);
        attemptKillingContainer(instanceId);
        attemptRemovingContainer(instanceId);
    }

    @Override
    public String inspectInstance(String instanceId) {
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(instanceId).exec();
        ObjectNode containerResponse = buildContainerResponse(instanceId, inspectContainerResponse);
        Optional<String> jsonResponse = getJsonResponse(containerResponse);
        return jsonResponse.orElse(format("Could not create container response of instance id: %s", instanceId));
    }

    @Override
    public String getContainerStats(String instanceId) {
        AtomicReference<Statistics> statsRef = new AtomicReference<>();
        try {
            dockerClient.statsCmd(instanceId).exec(new ResultCallbackTemplate<com.github.dockerjava.api.async.ResultCallback<Statistics>, Statistics>() {
                @Override
                public void onNext(Statistics stats) {
                    statsRef.set(stats);
                    try {
                        close();
                    } catch (IOException e) {
                        LOGGER.error("Error closing stats callback", e);
                    }
                }
            }).awaitCompletion(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Statistics stats = statsRef.get();
        if (stats == null) return "N/A";

        long memUsage = stats.getMemoryStats().getUsage() != null ? stats.getMemoryStats().getUsage() / (1024 * 1024) : 0;
        
        // Simplified CPU % calculation
        double cpuPercent = 0.0;
        if (stats.getCpuStats().getCpuUsage().getTotalUsage() != null && stats.getPreCpuStats().getCpuUsage().getTotalUsage() != null) {
            long totalUsage = stats.getCpuStats().getCpuUsage().getTotalUsage();
            long preTotalUsage = stats.getPreCpuStats().getCpuUsage().getTotalUsage();
            long systemUsage = stats.getCpuStats().getSystemCpuUsage();
            long preSystemUsage = stats.getPreCpuStats().getSystemCpuUsage();
            
            if (systemUsage - preSystemUsage > 0) {
                cpuPercent = ((double)(totalUsage - preTotalUsage) / (systemUsage - preSystemUsage)) * 100.0 * stats.getCpuStats().getOnlineCpus();
            }
        }

        return format("Memory: %d MB | CPU: %.1f%%", memUsage, cpuPercent);
    }


    private ObjectNode buildContainerResponse(String instanceId, CreateContainerResponse createContainerResponse) {
        ObjectNode containerResponse = getMapper().createObjectNode();
        containerResponse.put("instanceId", instanceId);
        containerResponse.put("containerId", createContainerResponse.getId());
        containerResponse.put("containerWarnings", Arrays.toString(createContainerResponse.getWarnings()));
        return containerResponse;
    }

    private ObjectNode buildContainerResponse(String instanceId, InspectContainerResponse inspectContainerResponse) {
        ObjectNode containerResponse = getMapper().createObjectNode();
        containerResponse.put("instanceId", instanceId);
        containerResponse.put("containerId", inspectContainerResponse.getId());
        containerResponse.put("containerStatus", inspectContainerResponse.getState().getStatus());
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
