package org.accmanager.service.api;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.accmanager.api.InstancesApi;
import org.accmanager.model.Instance;
import org.accmanager.model.InstanceState;
import org.accmanager.service.services.docker.ContainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

import static org.accmanager.model.InstanceState.CRASHED;
import static org.accmanager.model.InstanceState.CREATED;

@Controller
public class InstancesApiImpl implements InstancesApi {

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
        return ResponseEntity.ok(buildInstance(containerResponse, instance));
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
        return containerResponse.getWarnings().length == 0 ? CREATED : CRASHED;
    }

    private InstanceState getStateOfContainer(InspectContainerResponse containerResponse) {
        return InstanceState.fromValue(containerResponse.getState().getStatus());
    }

    @Override
    public ResponseEntity<Instance> updateInstanceById(String instanceId, Instance instance) {
        containerService.writeInstanceConfiguration(instance);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteInstanceById(String instanceId) {
        containerService.killContainer(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> startInstanceById(String instanceId) {
        containerService.startContainer(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> restartInstanceById(String instanceId) {
        containerService.restartContainer(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> stopInstanceById(String instanceId) {
        containerService.stopContainer(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
