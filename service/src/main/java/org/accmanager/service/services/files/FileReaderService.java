package org.accmanager.service.services.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.accmanager.model.AssistRules;
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

@Service
public class FileReaderService {

    private static final String BASE_ACC_PATH = "/home/%s/acc-manager/servers/%s/config/%s";
    private static final String ASSIST_RULES_JSON = "assistRules.json";
    private static final String CONFIGURATION_JSON = "configuration.json";
    private static final String ENTRY_LIST_JSON = "entryList.json";
    private static final String EVENT_JSON = "event.json";
    private static final String EVENT_RULES_JSON = "eventRules.json";
    private static final String SETTINGS_JSON = "settings.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    public Optional<AssistRules> readAssistRulesFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, ASSIST_RULES_JSON), AssistRules.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Config> readConfigurationFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, CONFIGURATION_JSON), Config.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<EntriesList> readEntriesListFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, ENTRY_LIST_JSON), EntriesList.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Event> readEventFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, EVENT_JSON), Event.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<EventRules> readEventRulesFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, EVENT_RULES_JSON), EventRules.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Settings> readSettingsFile(String instanceId) {
        try {
            return Optional.of(objectMapper.readValue(createNewFile(instanceId, SETTINGS_JSON), Settings.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private File createNewFile(String instanceId, String jsonFile) {
        return new File(String.format(BASE_ACC_PATH, dockerUsername, instanceId, jsonFile));
    }
}
