package org.accmanager.service.services.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Volume;
import org.accmanager.model.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContainerService {

    private static final String VOLUME_PATH_HOST = "/home/%s/acc-manager/servers/%s/config";
    private static final String VOLUME_PATH_CONTAINER = ":/home/server/config";

    private final DockerClient dockerClient;

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    @Autowired
    public ContainerService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public CreateContainerResponse createDockerContainer(Instance instance) {
        return dockerClient.createContainerCmd(instance.getDockerImage())
                .withCmd("--net=host")
                .withName(instance.getId())
                .withVolumes(new Volume(String.format(
                        VOLUME_PATH_HOST, dockerUsername, instance.getId()) + VOLUME_PATH_CONTAINER))
                .withExposedPorts(new ExposedPort(instance.getConfig().getTcpPort()),
                        new ExposedPort(instance.getConfig().getUdpPort())).exec();
    }

    public void startContainer(String instanceId) {
        dockerClient.startContainerCmd(instanceId).exec();
    }

    public void stopContainer(String instanceId) {
        dockerClient.stopContainerCmd(instanceId).exec();
    }

    public void restartContainer(String instanceId) {
        dockerClient.restartContainerCmd(instanceId).exec();
    }

    public void killContainer(String instanceId) {
        dockerClient.killContainerCmd(instanceId).exec();
    }

    public InspectContainerResponse inspectContainer(String instanceId) {
        return dockerClient.inspectContainerCmd(instanceId).exec();
    }

    public ListContainersCmd listOfContainers() {
        return dockerClient.listContainersCmd().withShowAll(true);
    }
}
