package org.accmanager.service.services.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.accmanager.service.enums.FilesEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE;

@Service
public class FileReadWriteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReadWriteService.class);
    private static final String FAILED_TO_PARSE_FILE = "Failed to parse '%s' file: %s";
    private static final String FAILED_TO_WRITE_FILE = "Failed to write '%s' file: %s";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    public Optional<Object> readJsonFile(String instanceId, FilesEnum filesEnum, Class<?> cls) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, filesEnum.toString()), cls));
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_PARSE_FILE, filesEnum.toString(), e.getMessage()));
        }
        return Optional.empty();
    }

    public void writeJsonFile(String instanceId, FilesEnum filesEnum, Object object) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, filesEnum.toString()), object);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, filesEnum.toString(), e.getMessage()));
        }
    }

    private File createNewFile(String instanceId, String jsonFile) {
        return new File(format(PATH_HOST_SERVER_INSTANCE.toString() + "/%s", dockerUsername, instanceId, jsonFile));
    }

    public boolean createDirectory(String directory) {
        try {
            return new File(directory).mkdir();
        } catch (Exception e) {
            LOGGER.error(format("Error creating directory: %s", e));
            return false;
        }
    }

    public String getDockerUsername() {
        return dockerUsername;
    }
}
