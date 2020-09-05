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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${docker.username:accmanager}")
    private String dockerUsername;

    public void writeEventFile(String instanceId, Event event) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, EVENT_JSON.toString()), event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEventRulesFile(String instanceId, EventRules eventRules) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, EVENT_RULES_JSON.toString()), eventRules);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEntriesListFile(String instanceId, EntriesList entriesList) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, ENTRY_LIST_JSON.toString()), entriesList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAssistRulesFile(String instanceId, AssistRules assistRules) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, ASSIST_RULES_JSON.toString()), assistRules);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBopFile(String instanceId) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, BOP_JSON.toString()), BoP.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeConfigurationFile(String instanceId, Config config) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, CONFIGURATION_JSON.toString()), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSettingsFile(String instanceId, Settings settings) {
        try {
            objectMapper.writeValue(createNewFile(instanceId, SETTINGS_JSON.toString()), settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createNewFile(String instanceId, String assistRulesJson) {
        return new File(format(VOLUME_PATH_HOST_CONFIGS.toString() + "/%s", dockerUsername, instanceId, assistRulesJson));
    }
}
