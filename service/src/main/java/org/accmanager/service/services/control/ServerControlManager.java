package org.accmanager.service.services.control;

import org.accmanager.model.Instance;
import org.accmanager.service.services.control.container.ContainerControlService;
import org.accmanager.service.services.control.executable.ExecutableControlService;
import org.accmanager.service.services.dao.InstanceDaoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ServerControlManager extends ServerControl {

    private final ContainerControlService containerControl;
    private final ExecutableControlService nativeControl;

    public ServerControlManager(InstanceDaoService instanceDaoService,
                                @Qualifier("containerControlService") ContainerControlService containerControl,
                                @Qualifier("executableControlService") ExecutableControlService nativeControl) {
        super(instanceDaoService);
        this.containerControl = containerControl;
        this.nativeControl = nativeControl;
    }

    private ServerControl getEngine(String instanceId) {
        Instance.ControlTypeEnum controlType = getDaoService().retrieveById(instanceId)
                .map(Instance::getControlType)
                .orElse(Instance.ControlTypeEnum.DOCKER);
        if (controlType == Instance.ControlTypeEnum.DOCKER) {
            return containerControl;
        }
        return nativeControl;
    }

    @Override
    public String createInstance(Instance instance) {
        if (instance.getControlType() == Instance.ControlTypeEnum.DOCKER) {
            return containerControl.createInstance(instance);
        }
        return nativeControl.createInstance(instance);
    }

    @Override
    public void startInstance(String instanceId) {
        getEngine(instanceId).startInstance(instanceId);
    }

    @Override
    public void stopInstance(String instanceId) {
        getEngine(instanceId).stopInstance(instanceId);
    }

    @Override
    public void restartInstance(String instanceId) {
        getEngine(instanceId).restartInstance(instanceId);
    }

    @Override
    public void deleteConfigFiles(String instanceId) {
        getEngine(instanceId).deleteConfigFiles(instanceId);
    }

    @Override
    public String inspectInstance(String instanceId) {
        return getEngine(instanceId).inspectInstance(instanceId);
    }

    @Override
    public String getContainerStats(String instanceId) {
        return containerControl.getContainerStats(instanceId);
    }

    public boolean isPortFree(int port) {
        if (port < 1 || port > 65535) {
            return false;
        }
        try (java.net.ServerSocket ss = new java.net.ServerSocket(port);
             java.net.DatagramSocket ds = new java.net.DatagramSocket(port)) {
            return true;
        } catch (java.io.IOException | IllegalArgumentException e) {
            return false;
        }
    }
}