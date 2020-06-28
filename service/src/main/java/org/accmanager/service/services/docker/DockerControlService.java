package org.accmanager.service.services.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Volume;
import org.accmanager.model.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DockerControlService {

    private final DockerClient dockerClient;

    @Autowired
    public DockerControlService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public CreateContainerResponse createDockerContainer(Instance instance) {
        return dockerClient.createContainerCmd("acc-server-1.4.4-wine")
                .withCmd("--net=host")
                .withName("acc-server-1")
                .withVolumes(new Volume("/home/john/Projects/Docker/Projects/ACC/Configs/server-1:/home/server/config"))
                .withExposedPorts(new ExposedPort(9231), new ExposedPort(9232)).exec();
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
