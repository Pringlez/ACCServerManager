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
public class FileWriterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWriterService.class);
    private static final String FAILED_TO_WRITE_FILE = "Failed to write '%s' file: %s";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    public void writeEventFile(String instanceId, Event event) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, EVENT_JSON.getConfigFile()), event);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, EVENT_JSON.getConfigFile(), e.getMessage()));
        }
    }

    public void writeEventRulesFile(String instanceId, EventRules eventRules) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, EVENT_RULES_JSON.getConfigFile()), eventRules);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, EVENT_RULES_JSON.getConfigFile(), e.getMessage()));
        }
    }

    public void writeEntriesListFile(String instanceId, EntriesList entriesList) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, ENTRY_LIST_JSON.getConfigFile()), entriesList);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, ENTRY_LIST_JSON.getConfigFile(), e.getMessage()));
        }
    }

    public void writeAssistRulesFile(String instanceId, AssistRules assistRules) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, ASSIST_RULES_JSON.getConfigFile()), assistRules);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, ASSIST_RULES_JSON.getConfigFile(), e.getMessage()));
        }
    }

    public void writeBopFile(String instanceId) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, BOP_JSON.getConfigFile()), BoP.class);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, BOP_JSON.getConfigFile(), e.getMessage()));
        }
    }

    public void writeConfigurationFile(String instanceId, Config config) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, CONFIGURATION_JSON.getConfigFile()), config);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, CONFIGURATION_JSON.getConfigFile(), e.getMessage()));
        }
    }

    public void writeSettingsFile(String instanceId, Settings settings) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, SETTINGS_JSON.getConfigFile()), settings);
        } catch (IOException e) {
            LOGGER.error(format(FAILED_TO_WRITE_FILE, SETTINGS_JSON.getConfigFile(), e.getMessage()));
        }
    }

    private File createNewFile(String instanceId, String assistRulesJson) {
        return new File(format(VOLUME_PATH_HOST_CONFIGS.getVolumePath() + "/%s", dockerUsername, instanceId, assistRulesJson));
    }
}
