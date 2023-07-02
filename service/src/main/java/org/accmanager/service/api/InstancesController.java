package org.accmanager.service.api;

import org.accmanager.api.InstancesApi;
import org.accmanager.model.Instance;
import org.accmanager.model.InstanceSuccess;
import org.accmanager.model.StorageType;
import org.accmanager.service.exception.InstanceNotFoundException;
import org.accmanager.service.services.control.ServerControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

import static java.lang.String.format;
import static org.accmanager.model.InstanceState.CREATED;
import static org.accmanager.model.ServerControl.EXECUTABLE;
import static org.accmanager.service.enums.ExceptionEnum.ACC_SERVER_INSTANCE_NOT_FOUND;

@Controller
public class InstancesController implements InstancesApi {

    private final ServerControl serverControl;

    public InstancesController(ServerControl serverControl) {
        this.serverControl = serverControl;
    }

    @PreAuthorize("hasAuthority('read.instance')")
    @Override
    public ResponseEntity<List<Instance>> getAllInstances(StorageType storageType) {
        return ResponseEntity.ok(serverControl.getDaoService().listOfInstances(storageType));
    }

    @PreAuthorize("hasAuthority('read.instance')")
    @Override
    public ResponseEntity<Instance> getInstanceById(String instanceId, StorageType storageType) {
        serverControl.inspectInstance(instanceId, storageType);
        return ResponseEntity.ok(getInstance(instanceId, storageType));
    }

    private Instance getInstance(String instanceId, StorageType storageType) {
        return serverControl.getDaoService().listOfInstances(storageType).stream()
                .filter(instance -> instance.getId().equals(instanceId)).findFirst()
                .orElseThrow(() -> new InstanceNotFoundException(format(ACC_SERVER_INSTANCE_NOT_FOUND.toString(), instanceId)));
    }

    @PreAuthorize("hasAuthority('write.instance')")
    @Override
    public ResponseEntity<InstanceSuccess> createInstance(Instance instance) {
        serverControl.createInstance(instance);
        return ResponseEntity.ok(buildSuccessInstance("ACC server instance files created successfully"));
    }

    private InstanceSuccess buildSuccessInstance(String message) {
        InstanceSuccess instanceSuccess = new InstanceSuccess();
        instanceSuccess.setState(CREATED);
        instanceSuccess.setMessage(message);
        instanceSuccess.setServerControl(EXECUTABLE);
        return instanceSuccess;
    }

    @PreAuthorize("hasAuthority('write.instance')")
    @Override
    public ResponseEntity<InstanceSuccess> updateInstanceById(String instanceId, Instance instance) {
        serverControl.getDaoService().writeInstanceConfiguration(instance);
        return ResponseEntity.ok(buildSuccessInstance("ACC server instance was updated successfully, you'll need to restart the ACC server"));
    }

    @PreAuthorize("hasAuthority('write.instance')")
    @Override
    public ResponseEntity<Void> deleteInstanceById(String instanceId) {
        serverControl.deleteConfigFiles(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('read.instance')")
    @Override
    public ResponseEntity<Void> startInstanceById(String instanceId) {
        serverControl.startInstance(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('read.instance')")
    @Override
    public ResponseEntity<Void> restartInstanceById(String instanceId) {
        serverControl.restartInstance(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('read.instance')")
    @Override
    public ResponseEntity<Void> stopInstanceById(String instanceId) {
        serverControl.stopInstance(instanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
