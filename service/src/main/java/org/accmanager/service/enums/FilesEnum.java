package org.accmanager.service.enums;

public enum FilesEnum {

    ASSIST_RULES_JSON("assistRules.json"),
    CONFIGURATION_JSON("configuration.json"),
    BOP_JSON("bop.json"),
    ENTRY_LIST_JSON("entrylist.json"),
    EVENT_JSON("event.json"),
    EVENT_RULES_JSON("eventRules.json"),
    SETTINGS_JSON("settings.json"),
    ACC_SERVER_EXE("/accServer.exe");

    private final String configFile;

    FilesEnum(String configFile) {
        this.configFile = configFile;
    }

    @Override
    public String toString() {
        return configFile;
    }
}
