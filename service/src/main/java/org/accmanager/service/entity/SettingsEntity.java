package org.accmanager.service.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SETTINGS")
public class SettingsEntity {

    private String settingsId;
    private String serverInstanceName;
    private String adminPassword;
    private String carGroup;
    private int trackMedalsRequirement;
    private int safetyRatingRequirement;
    private int raceCraftRatingRequirement;
    private String serverPassword;
    private String spectatorPassword;
    private int maxCarSlots;
    private int dumpLeaderBoards;
    private int isRaceLocked;
    private int isPrepPhaseLocked;
    private int randomizeTrackWhenEmpty;
    private String centralEntryListPath;
    private int allowAutoDq;
    private int shortFormationLap;
    private int dumpEntryList;
    private int formationLapType;
    private int doDriverSwapBroadcast;
    private int configVersion;

    @Id
    @Column(name = "SETTINGS_ID")
    public String getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(String settingsId) {
        this.settingsId = settingsId;
    }

    @Basic
    @Column(name = "SERVER_INSTANCE_NAME")
    public String getServerInstanceName() {
        return serverInstanceName;
    }

    public void setServerInstanceName(String serverInstanceName) {
        this.serverInstanceName = serverInstanceName;
    }

    @Basic
    @Column(name = "ADMIN_PASSWORD")
    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Basic
    @Column(name = "CAR_GROUP")
    public String getCarGroup() {
        return carGroup;
    }

    public void setCarGroup(String carGroup) {
        this.carGroup = carGroup;
    }

    @Basic
    @Column(name = "TRACK_MEDALS_REQUIREMENT")
    public int getTrackMedalsRequirement() {
        return trackMedalsRequirement;
    }

    public void setTrackMedalsRequirement(int trackMedalsRequirement) {
        this.trackMedalsRequirement = trackMedalsRequirement;
    }

    @Basic
    @Column(name = "SAFETY_RATING_REQUIREMENT")
    public int getSafetyRatingRequirement() {
        return safetyRatingRequirement;
    }

    public void setSafetyRatingRequirement(int safetyRatingRequirement) {
        this.safetyRatingRequirement = safetyRatingRequirement;
    }

    @Basic
    @Column(name = "RACE_CRAFT_RATING_REQUIREMENT")
    public int getRaceCraftRatingRequirement() {
        return raceCraftRatingRequirement;
    }

    public void setRaceCraftRatingRequirement(int raceCraftRatingRequirement) {
        this.raceCraftRatingRequirement = raceCraftRatingRequirement;
    }

    @Basic
    @Column(name = "SERVER_PASSWORD")
    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    @Basic
    @Column(name = "SPECTATOR_PASSWORD")
    public String getSpectatorPassword() {
        return spectatorPassword;
    }

    public void setSpectatorPassword(String spectatorPassword) {
        this.spectatorPassword = spectatorPassword;
    }

    @Basic
    @Column(name = "MAX_CAR_SLOTS")
    public int getMaxCarSlots() {
        return maxCarSlots;
    }

    public void setMaxCarSlots(int maxCarSlots) {
        this.maxCarSlots = maxCarSlots;
    }

    @Basic
    @Column(name = "DUMP_LEADER_BOARDS")
    public int getDumpLeaderBoards() {
        return dumpLeaderBoards;
    }

    public void setDumpLeaderBoards(int dumpLeaderBoards) {
        this.dumpLeaderBoards = dumpLeaderBoards;
    }

    @Basic
    @Column(name = "IS_RACE_LOCKED")
    public int getIsRaceLocked() {
        return isRaceLocked;
    }

    public void setIsRaceLocked(int isRaceLocked) {
        this.isRaceLocked = isRaceLocked;
    }

    @Basic
    @Column(name = "IS_PREP_PHASE_LOCKED")
    public int getIsPrepPhaseLocked() {
        return isPrepPhaseLocked;
    }

    public void setIsPrepPhaseLocked(int isPrepPhaseLocked) {
        this.isPrepPhaseLocked = isPrepPhaseLocked;
    }

    @Basic
    @Column(name = "RANDOMIZE_TRACK_WHEN_EMPTY")
    public int getRandomizeTrackWhenEmpty() {
        return randomizeTrackWhenEmpty;
    }

    public void setRandomizeTrackWhenEmpty(int randomizeTrackWhenEmpty) {
        this.randomizeTrackWhenEmpty = randomizeTrackWhenEmpty;
    }

    @Basic
    @Column(name = "CENTRAL_ENTRY_LIST_PATH")
    public String getCentralEntryListPath() {
        return centralEntryListPath;
    }

    public void setCentralEntryListPath(String centralEntryListPath) {
        this.centralEntryListPath = centralEntryListPath;
    }

    @Basic
    @Column(name = "ALLOW_AUTO_DQ")
    public int getAllowAutoDq() {
        return allowAutoDq;
    }

    public void setAllowAutoDq(int allowAutoDq) {
        this.allowAutoDq = allowAutoDq;
    }

    @Basic
    @Column(name = "SHORT_FORMATION_LAP")
    public int getShortFormationLap() {
        return shortFormationLap;
    }

    public void setShortFormationLap(int shortFormationLap) {
        this.shortFormationLap = shortFormationLap;
    }

    @Basic
    @Column(name = "DUMP_ENTRY_LIST")
    public int getDumpEntryList() {
        return dumpEntryList;
    }

    public void setDumpEntryList(int dumpEntryList) {
        this.dumpEntryList = dumpEntryList;
    }

    @Basic
    @Column(name = "FORMATION_LAP_TYPE")
    public int getFormationLapType() {
        return formationLapType;
    }

    public void setFormationLapType(int formationLapType) {
        this.formationLapType = formationLapType;
    }

    @Basic
    @Column(name = "DO_DRIVER_SWAP_BROADCAST")
    public int getDoDriverSwapBroadcast() {
        return doDriverSwapBroadcast;
    }

    public void setDoDriverSwapBroadcast(int doDriverSwapBroadcast) {
        this.doDriverSwapBroadcast = doDriverSwapBroadcast;
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
