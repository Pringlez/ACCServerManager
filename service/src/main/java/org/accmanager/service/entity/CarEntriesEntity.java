package org.accmanager.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "CAR_ENTRIES")
public class CarEntriesEntity {

    private String carEntriesId;
    private String carEntryId;
    private Object forceEntryList;
    private int configVersion;

    @Id
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
    public Object getForceEntryList() {
        return forceEntryList;
    }

    public void setForceEntryList(Object forceEntryList) {
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
