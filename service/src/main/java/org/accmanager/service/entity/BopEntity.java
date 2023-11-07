package org.accmanager.service.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BOP")
public class BopEntity {

    private String bopId;
    private String bopEntryId;
    private int disableAutoSteer;
    private int disableAutoLights;
    private int disableAutoWiper;

    @Id
    @Column(name = "BOP_ID")
    public String getBopId() {
        return bopId;
    }

    public void setBopId(String bopId) {
        this.bopId = bopId;
    }

    @Basic
    @Column(name = "BOP_ENTRY_ID")
    public String getBopEntryId() {
        return bopEntryId;
    }

    public void setBopEntryId(String bopEntryId) {
        this.bopEntryId = bopEntryId;
    }

    @Basic
    @Column(name = "DISABLE_AUTO_STEER")
    public int getDisableAutoSteer() {
        return disableAutoSteer;
    }

    public void setDisableAutoSteer(int disableAutoSteer) {
        this.disableAutoSteer = disableAutoSteer;
    }

    @Basic
    @Column(name = "DISABLE_AUTO_LIGHTS")
    public int getDisableAutoLights() {
        return disableAutoLights;
    }

    public void setDisableAutoLights(int disableAutoLights) {
        this.disableAutoLights = disableAutoLights;
    }

    @Basic
    @Column(name = "DISABLE_AUTO_WIPER")
    public int getDisableAutoWiper() {
        return disableAutoWiper;
    }

    public void setDisableAutoWiper(int disableAutoWiper) {
        this.disableAutoWiper = disableAutoWiper;
    }
}
