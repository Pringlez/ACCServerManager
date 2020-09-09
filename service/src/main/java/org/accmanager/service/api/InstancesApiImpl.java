package org.accmanager.service.api;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.accmanager.api.InstancesApi;
import org.accmanager.model.Instance;
import org.accmanager.model.InstanceState;
import org.accmanager.service.services.docker.ContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

import static org.accmanager.model.InstanceState.CRASHED;
import static org.accmanager.model.InstanceState.RUNNING;

@Controller
public class InstancesApiImpl implements InstancesApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstancesApiImpl.class);

    private final ContainerService containerService;

    public InstancesApiImpl(ContainerService containerService) {
        this.containerService = containerService;
    }

    @Override
    public ResponseEntity<List<Instance>> getAllInstances() {
        return ResponseEntity.ok(containerService.listOfContainers());
    }

    @Override
    public ResponseEntity<Instance> getInstanceById(String instanceId) {
        InspectContainerResponse containerResponse = containerService.inspectContainer(instanceId);
        return ResponseEntity.ok(buildInstance(containerResponse, getInstance(instanceId)));
    }

    private Instance getInstance(String instanceId) {
        for (Instance instance : containerService.listOfContainers()) {
            if (instance.getId().equals(instanceId)) {
                return instance;
            }
        }
        return new Instance();
    }

    @Override
    public ResponseEntity<Instance> createInstance(Instance instance) {
        CreateContainerResponse containerResponse = containerService.createDockerContainer(instance);
        return ResponseEntity.ok(buildInstance(containerResponse.getId()));
    }

    private Instance buildInstance(String instanceId) {
        Instance instance = new Instance();
        instance.setId(instanceId);
        return instance;
    }

    private Instance buildInstance(CreateContainerResponse containerResponse, Instance instance) {
        instance.setId(containerResponse.getId());
        instance.setState(getStateOfContainer(containerResponse));
        return instance;
    }

    private Instance buildInstance(InspectContainerResponse containerResponse, Instance instance) {
        instance.setId(containerResponse.getId());
        instance.setState(getStateOfContainer(containerResponse));
        return instance;
    }

    private InstanceState getStateOfContainer(CreateContainerResponse containerResponse) {
        return (containerResponse.getWarnings().length == 0) ? RUNNING : CRASHED;
    }

    private InstanceState getStateOfContainer(InspectContainerResponse containerResponse) {
        String status = containerResponse.getState().getStatus();
        assert status != null;
        boolean isRunning = status.equals("Running!");
        return isRunning ? RUNNING : CRASHED;
    }

    @Override
    public ResponseEntity<Instance> updateInstanceById(String instanceId, Instance instance) {
        LOGGER.info("TODO - Not yet implemented");
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteInstanceById(String instanceId) {
        containerService.killContainer(instanceId);
        return null;
    }

    @Override
    public ResponseEntity<Void> startInstanceById(String instanceId) {
        containerService.startContainer(instanceId);
        return null;
    }

    @Override
    public ResponseEntity<Void> restartInstanceById(String instanceId) {
        containerService.restartContainer(instanceId);
        return null;
    }

    @Override
    public ResponseEntity<Void> stopInstanceById(String instanceId) {
        containerService.stopContainer(instanceId);
        return null;
    }
}
