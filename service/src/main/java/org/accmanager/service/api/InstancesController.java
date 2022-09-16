package org.accmanager.service.api;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.accmanager.api.InstancesApi;
import org.accmanager.model.Instance;
import org.accmanager.model.InstanceState;
import org.accmanager.service.exception.InstanceNotFoundException;
import org.accmanager.service.services.control.ServerControl;
import org.accmanager.service.services.control.docker.ContainerControlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

import static java.lang.String.format;
import static org.accmanager.model.InstanceState.CRASHED;
import static org.accmanager.model.InstanceState.CREATED;
import static org.accmanager.service.enums.ExceptionEnum.ACC_SERVER_INSTANCE_NOT_FOUND;

@Controller
public class InstancesController implements InstancesApi {

    private final ServerControl serverControl;

    public InstancesController(ServerControl serverControl) {
        this.serverControl = serverControl;
    }

    @Override
    public ResponseEntity<List<Instance>> getAllInstances() {
        return ResponseEntity.ok(serverControl.listOfInstances());
    }

    @Override
    public ResponseEntity<Instance> getInstanceById(String instanceId) {
        serverControl.inspectInstance(instanceId);
        return ResponseEntity.ok(buildInstance(getInstance(instanceId)));
    }

    private Instance getInstance(String instanceId) {
        for (Instance instance : serverControl.listOfInstances()) {
            if (instance.getId().equals(instanceId)) {
                return instance;
            }
        }
        throw new InstanceNotFoundException(format(ACC_SERVER_INSTANCE_NOT_FOUND.toString(), instanceId));
    }

    @Override
    public ResponseEntity<Instance> createInstance(Instance instance) {
        serverControl.createInstance(instance);
        return ResponseEntity.ok(buildInstance(instance));
    }

    private Instance buildInstance(Instance instance) {
        instance.setId(instance.getId());
        return instance;
    }

    @Override
    public ResponseEntity<Instance> updateInstanceById(String instanceId, Instance instance) {
        serverControl.writeInstanceConfiguration(instance);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteInstanceById(String instanceId) {
        serverControl.deleteConfigFiles(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> startInstanceById(String instanceId) {
        serverControl.startInstance(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> restartInstanceById(String instanceId) {
        serverControl.restartInstance(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> stopInstanceById(String instanceId) {
        serverControl.stopInstance(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
