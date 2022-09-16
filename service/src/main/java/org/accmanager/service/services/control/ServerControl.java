package org.accmanager.service.services.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.exception.DockerException;
import org.accmanager.model.AssistRules;
import org.accmanager.model.BoP;
import org.accmanager.model.Config;
import org.accmanager.model.EntriesList;
import org.accmanager.model.Event;
import org.accmanager.model.EventRules;
import org.accmanager.model.Instance;
import org.accmanager.model.Settings;
import org.accmanager.service.services.files.DirectoryReadWriteService;
import org.accmanager.service.services.files.FileReadWriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_COPYING_EXECUTABLE;
import static org.accmanager.service.enums.ExceptionEnum.ERROR_RETRIEVING_LIST_OF_CONTAINERS;
import static org.accmanager.service.enums.FilesEnum.ASSIST_RULES_JSON;
import static org.accmanager.service.enums.FilesEnum.BOP_JSON;
import static org.accmanager.service.enums.FilesEnum.CONFIGURATION_JSON;
import static org.accmanager.service.enums.FilesEnum.ENTRY_LIST_JSON;
import static org.accmanager.service.enums.FilesEnum.EVENT_JSON;
import static org.accmanager.service.enums.FilesEnum.EVENT_RULES_JSON;
import static org.accmanager.service.enums.FilesEnum.SETTINGS_JSON;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE_CFG;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE_EXECUTABLE;
import static org.accmanager.service.enums.PathsEnum.PATH_HOST_SERVER_INSTANCE_LOGS;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
public abstract class ServerControl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerControl.class);

    private final DirectoryReadWriteService directoryReadWriteService;
    private final FileReadWriteService fileReadWriteService;

    private final ObjectMapper mapper = new ObjectMapper();

    protected ServerControl(DirectoryReadWriteService directoryReadWriteService, FileReadWriteService fileReadWriteService) {
        this.directoryReadWriteService = directoryReadWriteService;
        this.fileReadWriteService = fileReadWriteService;
    }

    public abstract String createInstance(Instance instance);

    public abstract void startInstance(String instanceId);

    public abstract void stopInstance(String instanceId);

    public abstract void restartInstance(String instanceId);

    public abstract void deleteConfigFiles(String instanceId);

    public abstract String inspectInstance(String instanceId);

    public List<Instance> listOfInstances() {
        List<Instance> instancesList = new ArrayList<>();
        try {
            Optional<List<Path>> accServerDirs = directoryReadWriteService.getAllServerDirectories();
            accServerDirs.ifPresent(dirs -> dirs.forEach(
                    dir -> instancesList.add(readInstanceConfiguration(dir.toString()))));
        } catch (Exception ex) {
            LOGGER.warn(ERROR_RETRIEVING_LIST_OF_CONTAINERS.toString());
            throw new DockerException(ERROR_RETRIEVING_LIST_OF_CONTAINERS.toString(), INTERNAL_SERVER_ERROR.value(), ex);
        }
        return instancesList;
    }

    public void writeInstanceConfiguration(Instance instance) {
        fileReadWriteService.writeJsonFile(instance.getId(), EVENT_JSON, instance.getEvent());
        fileReadWriteService.writeJsonFile(instance.getId(), EVENT_RULES_JSON, instance.getEventRules());
        fileReadWriteService.writeJsonFile(instance.getId(), ENTRY_LIST_JSON, instance.getEntriesList());
        fileReadWriteService.writeJsonFile(instance.getId(), ASSIST_RULES_JSON, instance.getAssists());
        fileReadWriteService.writeJsonFile(instance.getId(), BOP_JSON, instance.getBop());
        fileReadWriteService.writeJsonFile(instance.getId(), CONFIGURATION_JSON, instance.getConfig());
        fileReadWriteService.writeJsonFile(instance.getId(), SETTINGS_JSON, instance.getSettings());
    }

    public Instance readInstanceConfiguration(String instanceId) {
        Instance instance = new Instance();
        instance.setId(instanceId);
        instance.setEvent((Event) fileReadWriteService.readJsonFile(instanceId, EVENT_JSON, Event.class).orElse(new Event()));
        instance.setEventRules((EventRules) fileReadWriteService.readJsonFile(instanceId, EVENT_RULES_JSON, EventRules.class).orElse(new EventRules()));
        instance.setEntriesList((EntriesList) fileReadWriteService.readJsonFile(instanceId, ENTRY_LIST_JSON, EntriesList.class).orElse(new EntriesList()));
        instance.setAssists((AssistRules) fileReadWriteService.readJsonFile(instanceId, ASSIST_RULES_JSON, AssistRules.class).orElse(new AssistRules()));
        instance.setBop((BoP) fileReadWriteService.readJsonFile(instanceId, BOP_JSON, BoP.class).orElse(new BoP()));
        instance.setConfig((Config) fileReadWriteService.readJsonFile(instanceId, CONFIGURATION_JSON, Config.class).orElse(new Config()));
        instance.setSettings((Settings) fileReadWriteService.readJsonFile(instanceId, SETTINGS_JSON, Settings.class).orElse(new Settings()));
        return instance;
    }

    public void copyExecutable(String instanceId) {
        try {
            fileReadWriteService.copyExecutable(instanceId);
        } catch (Exception ex) {
            LOGGER.warn(ERROR_COPYING_EXECUTABLE.toString());
        }
    }


    public void createDirectories(Instance instance) {
        fileReadWriteService.createNewDirectory(format(PATH_HOST_SERVER_INSTANCE_CFG.toString(), instance.getId()));
        fileReadWriteService.createNewDirectory(format(PATH_HOST_SERVER_INSTANCE_EXECUTABLE.toString(), instance.getId()));
        fileReadWriteService.createNewDirectory(format(PATH_HOST_SERVER_INSTANCE_LOGS.toString(), instance.getId()));
    }

    public DirectoryReadWriteService getDirectoryReadWriteService() {
        return directoryReadWriteService;
    }

    public FileReadWriteService getFileReadWriteService() {
        return fileReadWriteService;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
