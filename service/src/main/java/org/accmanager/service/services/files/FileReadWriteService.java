package org.accmanager.service.services.files;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.accmanager.service.enums.FilesEnum;
import org.accmanager.service.exception.FileReadException;
import org.accmanager.service.exception.FileWriteException;
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
import static org.accmanager.service.enums.ExceptionEnum.*;
import static org.accmanager.service.enums.FilesEnum.ACC_SERVER_EXE;
import static org.accmanager.service.enums.PathsEnum.*;

@Service
public class FileReadWriteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReadWriteService.class);

    private final ObjectMapper objectMapper;
    private final DefaultPrettyPrinter defaultPrettyPrinter;

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    public FileReadWriteService(ObjectMapper objectMapper, DefaultPrettyPrinter defaultPrettyPrinter) {
        this.objectMapper = objectMapper;
        this.defaultPrettyPrinter = defaultPrettyPrinter;
    }

    public Optional<Object> readJsonFile(String instanceId, FilesEnum filesEnum, Class<?> cls) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, filesEnum.toString()), cls));
        } catch (IOException ex) {
            LOGGER.error(format(ERROR_READING_FILE.toString(), filesEnum, ex.getMessage()));
            throw new FileReadException(format(ERROR_READING_FILE.toString(), filesEnum, ex.getMessage()), ex);
        }
    }

    public void writeJsonFile(String instanceId, FilesEnum filesEnum, Object object) {
        try {
            objectMapper.writer(defaultPrettyPrinter).writeValue(objectMapper.createGenerator(createNewFile(instanceId,
                    filesEnum.toString()), JsonEncoding.UTF16_LE), object);
        } catch (IOException ex) {
            LOGGER.error(format(ERROR_WRITING_FILE.toString(), filesEnum, ex.getMessage()));
            throw new FileWriteException(format(ERROR_WRITING_FILE.toString(), filesEnum, ex.getMessage()), ex);
        }
    }

    private File createNewFile(String instanceId, String jsonFile) {
        try {
            return new File(format(PATH_HOST_SERVER_INSTANCE_CFG_FILE.toString(), dockerUsername, instanceId, jsonFile));
        } catch (Exception ex) {
            LOGGER.error(format(ERROR_WRITING_FILE.toString(), jsonFile, ex.getMessage()));
            throw new FileWriteException(format(ERROR_WRITING_FILE.toString(), jsonFile, ex.getMessage()), ex);
        }
    }

    public void createNewDirectory(String directory) {
        try {
            new File(directory).mkdirs();
        } catch (Exception ex) {
            LOGGER.error(format(ERROR_CREATING_DIRECTORY.toString(), ex));
            throw new FileWriteException(format(ERROR_CREATING_DIRECTORY.toString(), ex.getMessage()), ex);
        }
    }

    public void copyExecutable(String instanceId) {
        try {
            Files.copy(Paths.get(format(PATH_HOST_EXECUTABLE.toString(), dockerUsername) + ACC_SERVER_EXE),
                    Paths.get(format(PATH_HOST_SERVER_INSTANCE_EXECUTABLE.toString(), dockerUsername, instanceId) + ACC_SERVER_EXE));
        } catch (Exception ex) {
            LOGGER.error(format(ERROR_COPYING_EXECUTABLE.toString(), ex));
            throw new FileWriteException(format(ERROR_COPYING_EXECUTABLE.toString(), ex), ex);
        }
    }

    public void deleteInstanceDirectoryConfigsAndFiles(String instanceId) {
        try {
            Files.walk(Paths.get(format(PATH_HOST_SERVER_INSTANCE.toString(), dockerUsername, instanceId)))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            Files.deleteIfExists(Paths.get(format(PATH_HOST_SERVER_INSTANCE.toString(), dockerUsername, instanceId)));
        } catch (Exception ex) {
            LOGGER.error(format(ERROR_DELETING_INSTANCE_DIRECTORY.toString(), ex));
        }
    }

    public String getDockerUsername() {
        return dockerUsername;
    }
}
