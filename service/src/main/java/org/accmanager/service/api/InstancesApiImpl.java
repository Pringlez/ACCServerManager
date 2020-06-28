package org.accmanager.service.api;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.accmanager.api.InstancesApi;
import org.accmanager.model.Instance;
import org.accmanager.service.services.docker.DockerControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class InstancesApiImpl implements InstancesApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstancesApiImpl.class);

    private final DockerControlService dockerControlService;

    public InstancesApiImpl(DockerControlService dockerControlService) {
        this.dockerControlService = dockerControlService;
    }

    @Override
    public ResponseEntity<List<Instance>> getInstances(String name, String state) {
        return null;
    }

    @Override
    public ResponseEntity<Instance> getInstanceById(String instanceId) {
        InspectContainerResponse containerResponse = dockerControlService.inspectContainer(instanceId);
        return ResponseEntity.ok(buildInstance(containerResponse));
    }

    @Override
    public ResponseEntity<Instance> createInstance(Instance instance) {
        CreateContainerResponse createContainerResponse = dockerControlService.createDockerContainer(instance);
        return ResponseEntity.ok(buildInstance(createContainerResponse.getId()));
    }

    private Instance buildInstance(String instanceId) {
        Instance instance = new Instance();
        instance.setId(instanceId);
        return instance;
    }

    private Instance buildInstance(InspectContainerResponse containerResponse) {
        Instance instance = new Instance();
        instance.setId(containerResponse.getId());
        instance.setName(containerResponse.getName());
        return instance;
    }

    @Override
    public ResponseEntity<Instance> updateInstanceById(String instanceId, Instance instance) {
        LOGGER.info("TODO - Not yet implemented");
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteInstanceById(String instanceId) {
        dockerControlService.killContainer(instanceId);
        return null;
    }

    @Override
    public ResponseEntity<Void> startInstanceById(String instanceId) {
        dockerControlService.startContainer(instanceId);
        return null;
    }

    @Override
    public ResponseEntity<Void> restartInstanceById(String instanceId) {
        dockerControlService.restartContainer(instanceId);
        return null;
    }

    @Override
    public ResponseEntity<Void> stopInstanceById(String instanceId) {
        dockerControlService.stopContainer(instanceId);
        return null;
    }
}
