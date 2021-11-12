package org.accmanager.service.services.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.accmanager.service.enums.FilesEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.PathsEnum.*;

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
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(createNewFile(instanceId, filesEnum.toString()), object);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, filesEnum.toString(), e.getMessage()));
        }
    }

    private File createNewFile(String instanceId, String jsonFile) {
        return new File(format(PATH_HOST_SERVER_INSTANCE_CFG_FILE.toString(), dockerUsername, instanceId, jsonFile));
    }

    public boolean createDirectory(String directory) {
        try {
            return new File(directory).mkdirs();
        } catch (Exception e) {
            LOGGER.error(format("Error creating directory: %s", e));
            return false;
        }
    }

    public void copyExecutable(String instanceId) {
        try {
            Files.copy(Paths.get(format(PATH_HOST_EXECUTABLE.toString(), dockerUsername) + "/accServer.exe"),
                    Paths.get(format(PATH_HOST_SERVER_INSTANCE_EXECUTABLE.toString(), dockerUsername, instanceId) + "/accServer.exe"));
        } catch (Exception e) {
            LOGGER.error(format("Error copying executable: %s", e));
        }
    }

    public void deleteInstanceDirectoryConfigsAndFiles(String instanceId) {
        try {
            Files.walk(Paths.get(format(PATH_HOST_SERVER_INSTANCE.toString(), dockerUsername, instanceId)))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            Files.deleteIfExists(Paths.get(format(PATH_HOST_SERVER_INSTANCE.toString(), dockerUsername, instanceId)));
        } catch (Exception e) {
            LOGGER.error(format("Error deleting instance directory: %s", e));
        }
    }

    public String getDockerUsername() {
        return dockerUsername;
    }
}
