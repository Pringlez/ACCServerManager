package org.accmanager.service.enums;

public enum ConfigFiles {

    ASSIST_RULES_JSON("assistRules.json"),
    CONFIGURATION_JSON("configuration.json"),
    BOP_JSON("bop.json"),
    ENTRY_LIST_JSON("entrylist.json"),
    EVENT_JSON("event.json"),
    EVENT_RULES_JSON("eventRules.json"),
    SETTINGS_JSON("settings.json");

    private final String configFile;

    ConfigFiles(String configFile) {
        this.configFile = configFile;
    }

    public String getConfigFile() {
        return configFile;
    }
}
