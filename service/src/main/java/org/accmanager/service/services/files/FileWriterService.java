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

@Service
public class FileWriterService {

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

    public void writeAssistRulesFile(String instanceId, AssistRules assistRules) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, ASSIST_RULES_JSON), assistRules);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeConfigurationFile(String instanceId, Config config) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, CONFIGURATION_JSON), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEntriesListFile(String instanceId, EntriesList entriesList) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, ENTRY_LIST_JSON), entriesList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEventFile(String instanceId, Event event) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, EVENT_JSON), event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEventRulesFile(String instanceId, EventRules eventRules) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, EVENT_RULES_JSON), eventRules);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSettingsFile(String instanceId, Settings settings) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, SETTINGS_JSON), settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createNewFile(String instanceId, String assistRulesJson) {
        return new File(String.format(BASE_ACC_PATH, dockerUsername, instanceId, assistRulesJson));
    }
}
