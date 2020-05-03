package org.accmanager.service.api;

import org.accmanager.api.InstancesApi;
import org.accmanager.model.Instance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class InstancesApiImpl implements InstancesApi {

    @Override
    public ResponseEntity<List<Instance>> getInstances(String name, String state) {
        return null;
    }

    @Override
    public ResponseEntity<Instance> getInstanceById(String instanceId) {
        return null;
    }

    @Override
    public ResponseEntity<Instance> createInstance(Instance instance) {
        return null;
    }

    @Override
    public ResponseEntity<Instance> updateInstanceById(String instanceId, Instance instance) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteInstanceById(String instanceId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> startInstanceById(String instanceId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> restartInstanceById(String instanceId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> stopInstanceById(String instanceId) {
        return null;
    }
}
