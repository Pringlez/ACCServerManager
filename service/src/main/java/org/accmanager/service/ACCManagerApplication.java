package org.accmanager.service;

import org.accmanager.service.services.files.FileReadWriteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static java.lang.String.format;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_EXECUTABLE;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVERS;

@SpringBootApplication
public class ACCManagerApplication {

    private final FileReadWriteService fileReadWriteService;

    @Value("${accserver.files.directory.override:}")
    private String accFileDirectoryOverride;

    public ACCManagerApplication(FileReadWriteService fileReadWriteService) {
        this.fileReadWriteService = fileReadWriteService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ACCManagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            fileReadWriteService.createNewDirectory(format(accFileDirectoryOverride + PATH_HOST_EXECUTABLE));
            fileReadWriteService.createNewDirectory(format(accFileDirectoryOverride + PATH_HOST_SERVERS));
        };
    }
}
