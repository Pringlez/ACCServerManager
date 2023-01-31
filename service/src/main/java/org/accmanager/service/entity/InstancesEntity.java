package org.accmanager.service.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INSTANCES")
public class InstancesEntity {

    private String instanceId;
    private String instanceName;
    private String dockerImage;
    private String containerState;
    private String eventId;
    private String eventRulesId;
    private String entriesId;
    private String assistsId;
    private String bopId;
    private String configId;
    private String settingsId;

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
    @Column(name = "DOCKER_IMAGE")
    public String getDockerImage() {
        return dockerImage;
    }

    public void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage;
    }

    @Basic
    @Column(name = "CONTAINER_STATE")
    public String getContainerState() {
        return containerState;
    }

    public void setContainerState(String containerState) {
        this.containerState = containerState;
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
    @Column(name = "ASSISTS_ID")
    public String getAssistsId() {
        return assistsId;
    }

    public void setAssistsId(String assistsId) {
        this.assistsId = assistsId;
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
}
