package org.accmanager.service.services.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.accmanager.model.AssistRules;
import org.accmanager.model.BoP;
import org.accmanager.model.Config;
import org.accmanager.model.EntriesList;
import org.accmanager.model.Event;
import org.accmanager.model.EventRules;
import org.accmanager.model.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;
import static org.accmanager.service.enums.ConfigFiles.ASSIST_RULES_JSON;
import static org.accmanager.service.enums.ConfigFiles.BOP_JSON;
import static org.accmanager.service.enums.ConfigFiles.CONFIGURATION_JSON;
import static org.accmanager.service.enums.ConfigFiles.ENTRY_LIST_JSON;
import static org.accmanager.service.enums.ConfigFiles.EVENT_JSON;
import static org.accmanager.service.enums.ConfigFiles.EVENT_RULES_JSON;
import static org.accmanager.service.enums.ConfigFiles.SETTINGS_JSON;
import static org.accmanager.service.enums.VolumePaths.VOLUME_PATH_HOST_CONFIGS;

@Service
public class FileReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReaderService.class);
    private static final String FAILED_TO_PARSE_FILE = "Failed to parse '%s' file: %s";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    public Optional<Event> readEventFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, EVENT_JSON.getConfigFile()), Event.class));
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_PARSE_FILE, EVENT_JSON.getConfigFile(), e.getMessage()));
        }
        return Optional.empty();
    }

    public Optional<EventRules> readEventRulesFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, EVENT_RULES_JSON.getConfigFile()), EventRules.class));
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_PARSE_FILE, EVENT_RULES_JSON.getConfigFile(), e.getMessage()));
        }
        return Optional.empty();
    }

    public Optional<EntriesList> readEntriesListFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, ENTRY_LIST_JSON.getConfigFile()), EntriesList.class));
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_PARSE_FILE, ENTRY_LIST_JSON.getConfigFile(), e.getMessage()));
        }
        return Optional.empty();
    }

    public Optional<AssistRules> readAssistRulesFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, ASSIST_RULES_JSON.getConfigFile()), AssistRules.class));
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_PARSE_FILE, ASSIST_RULES_JSON.getConfigFile(), e.getMessage()));
        }
        return Optional.empty();
    }

    public Optional<BoP> readBopFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, BOP_JSON.getConfigFile()), BoP.class));
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_PARSE_FILE, BOP_JSON.getConfigFile(), e.getMessage()));
        }
        return Optional.empty();
    }

    public Optional<Config> readConfigurationFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, CONFIGURATION_JSON.getConfigFile()), Config.class));
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_PARSE_FILE, CONFIGURATION_JSON.getConfigFile(), e.getMessage()));
        }
        return Optional.empty();
    }


    public Optional<Settings> readSettingsFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, SETTINGS_JSON.getConfigFile()), Settings.class));
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_PARSE_FILE, SETTINGS_JSON.getConfigFile(), e.getMessage()));
        }
        return Optional.empty();
    }

    private File createNewFile(String instanceId, String jsonFile) {
        return new File(format(VOLUME_PATH_HOST_CONFIGS.getVolumePath() + "/%s", dockerUsername, instanceId, jsonFile));
    }
}
