package org.accmanager.service.services.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.accmanager.model.Instance;
import org.accmanager.service.services.dao.InstanceDaoService;

public abstract class ServerControl {

    private final InstanceDaoService instanceDaoService;
    private final ObjectMapper mapper = new ObjectMapper();

    protected ServerControl(InstanceDaoService instanceDaoService) {
        this.instanceDaoService = instanceDaoService;
    }

    public abstract String createInstance(Instance instance);

    public abstract void startInstance(String instanceId);

    public abstract void stopInstance(String instanceId);

    public abstract void restartInstance(String instanceId);

    public abstract void deleteConfigFiles(String instanceId);

    public abstract String inspectInstance(String instanceId);

    public InstanceDaoService getDaoService() {
        return instanceDaoService;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
