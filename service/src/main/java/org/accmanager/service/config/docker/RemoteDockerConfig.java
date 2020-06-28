package org.accmanager.service.config.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class RemoteDockerConfig {

    @Value("${docker.host}")
    private String dockerHost;

    @Value("${docker.verify}")
    private boolean dockerVerify;

    @Bean
    @Primary
    public DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .withDockerTlsVerify(dockerVerify)
                .build();
    }

    @Bean
    public DockerClient dockerClient(DockerClientConfig dockerClientConfig) {
        return DockerClientBuilder.getInstance(dockerClientConfig).build();
    }
}
