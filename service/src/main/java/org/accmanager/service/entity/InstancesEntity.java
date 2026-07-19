package org.accmanager.service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "INSTANCES")
public class InstancesEntity {

    private String instanceId;
    private String instanceName;
    private String containerImage;
    private String controlType;
    private String eventId;
    private String eventRulesId;
    private String entriesId;
    private String assistRulesId;
    private String bopId;
    private String configId;
    private String settingsId;
    private String entryListData;
    private String bopData;

    @Id
    @Column(name = "INSTANCE_ID")
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Basic
    @Column(name = "INSTANCE_NAME")
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Basic
    @Column(name = "CONTAINER_IMAGE")
    public String getContainerImage() {
        return containerImage;
    }

    public void setContainerImage(String containerImage) {
        this.containerImage = containerImage;
    }

    @Basic
    @Column(name = "EVENT_ID")
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Basic
    @Column(name = "EVENT_RULES_ID")
    public String getEventRulesId() {
        return eventRulesId;
    }

    public void setEventRulesId(String eventRulesId) {
        this.eventRulesId = eventRulesId;
    }

    @Basic
    @Column(name = "ENTRIES_ID")
    public String getEntriesId() {
        return entriesId;
    }

    public void setEntriesId(String entriesId) {
        this.entriesId = entriesId;
    }

    @Basic
    @Column(name = "ASSIST_RULES_ID")
    public String getAssistRulesId() {
        return assistRulesId;
    }

    public void setAssistRulesId(String assistRulesId) {
        this.assistRulesId = assistRulesId;
    }

    @Basic
    @Column(name = "BOP_ID")
    public String getBopId() {
        return bopId;
    }

    public void setBopId(String bopId) {
        this.bopId = bopId;
    }

    @Basic
    @Column(name = "CONFIG_ID")
    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    @Basic
    @Column(name = "SETTINGS_ID")
    public String getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(String settingsId) {
        this.settingsId = settingsId;
    }

    @Basic
    @Column(name = "CONTROL_TYPE")
    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    @Basic
    @Column(name = "ENTRY_LIST_DATA", columnDefinition = "TEXT")
    public String getEntryListData() {
        return entryListData;
    }

    public void setEntryListData(String entryListData) {
        this.entryListData = entryListData;
    }

    @Basic
    @Column(name = "BOP_DATA", columnDefinition = "TEXT")
    public String getBopData() {
        return bopData;
    }

    public void setBopData(String bopData) {
        this.bopData = bopData;
    }
}
