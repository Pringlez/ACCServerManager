package org.accmanager.service.services.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.accmanager.model.AssistRules;
import org.accmanager.model.BoP;
import org.accmanager.model.Config;
import org.accmanager.model.EntriesList;
import org.accmanager.model.Event;
import org.accmanager.model.EventRules;
import org.accmanager.model.Settings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    public Optional<Event> readEventFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, EVENT_JSON.toString()), Event.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<EventRules> readEventRulesFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, EVENT_RULES_JSON.toString()), EventRules.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<EntriesList> readEntriesListFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, ENTRY_LIST_JSON.toString()), EntriesList.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<AssistRules> readAssistRulesFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, ASSIST_RULES_JSON.toString()), AssistRules.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<BoP> readBopFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, BOP_JSON.toString()), BoP.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Config> readConfigurationFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, CONFIGURATION_JSON.toString()), Config.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public Optional<Settings> readSettingsFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, SETTINGS_JSON.toString()), Settings.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private File createNewFile(String instanceId, String jsonFile) {
        return new File(String.format(VOLUME_PATH_HOST_CONFIGS.toString() + "/%s", dockerUsername, instanceId, jsonFile));
    }
}
