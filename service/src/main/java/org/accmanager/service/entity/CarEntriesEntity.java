package org.accmanager.service.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "CAR_ENTRIES")
public class CarEntriesEntity {

    private String carEntriesId;
    private String carEntryId;
    private int forceEntryList;
    private int configVersion;

    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "CAR_ENTRIES_ID")
    public String getCarEntriesId() {
        return carEntriesId;
    }

    public void setCarEntriesId(String carEntriesId) {
        this.carEntriesId = carEntriesId;
    }

    @Basic
    @Column(name = "CAR_ENTRY_ID")
    public String getCarEntryId() {
        return carEntryId;
    }

    public void setCarEntryId(String carEntryId) {
        this.carEntryId = carEntryId;
    }

    @Basic
    @Column(name = "FORCE_ENTRY_LIST")
    public int getForceEntryList() {
        return forceEntryList;
    }

    public void setForceEntryList(int forceEntryList) {
        this.forceEntryList = forceEntryList;
    }

    @Basic
    @Column(name = "CONFIG_VERSION")
    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }
}
