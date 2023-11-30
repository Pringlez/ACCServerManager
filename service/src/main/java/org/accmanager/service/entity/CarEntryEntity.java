package org.accmanager.service.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "CAR_ENTRY")
public class CarEntryEntity {

    private String carEntryId;
    private String carEntriesId;
    private String driverId;
    private int raceNumber;
    private int forceCarModel;
    private int overrideDriverInfo;
    private String customCar;
    private int overrideCarModelForCustomCar;
    private int isServerAdmin;
    private int defaultGridPosition;
    private int ballastKg;
    private int restrictor;

    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "CAR_ENTRY_ID")
    public String getCarEntryId() {
        return carEntryId;
    }

    public void setCarEntryId(String carEntryId) {
        this.carEntryId = carEntryId;
    }

    @Basic
    @Column(name = "CAR_ENTRIES_ID")
    public String getCarEntriesId() {
        return carEntriesId;
    }

    public void setCarEntriesId(String carEntriesId) {
        this.carEntriesId = carEntriesId;
    }

    @Basic
    @Column(name = "DRIVER_ID")
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Basic
    @Column(name = "RACE_NUMBER")
    public int getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(int raceNumber) {
        this.raceNumber = raceNumber;
    }

    @Basic
    @Column(name = "FORCE_CAR_MODEL")
    public int getForceCarModel() {
        return forceCarModel;
    }

    public void setForceCarModel(int forceCarModel) {
        this.forceCarModel = forceCarModel;
    }

    @Basic
    @Column(name = "OVERRIDE_DRIVER_INFO")
    public int getOverrideDriverInfo() {
        return overrideDriverInfo;
    }

    public void setOverrideDriverInfo(int overrideDriverInfo) {
        this.overrideDriverInfo = overrideDriverInfo;
    }

    @Basic
    @Column(name = "CUSTOM_CAR")
    public String getCustomCar() {
        return customCar;
    }

    public void setCustomCar(String customCar) {
        this.customCar = customCar;
    }

    @Basic
    @Column(name = "OVERRIDE_CAR_MODEL_FOR_CUSTOM_CAR")
    public int getOverrideCarModelForCustomCar() {
        return overrideCarModelForCustomCar;
    }

    public void setOverrideCarModelForCustomCar(int overrideCarModelForCustomCar) {
        this.overrideCarModelForCustomCar = overrideCarModelForCustomCar;
    }

    @Basic
    @Column(name = "IS_SERVER_ADMIN")
    public int getIsServerAdmin() {
        return isServerAdmin;
    }

    public void setIsServerAdmin(int isServerAdmin) {
        this.isServerAdmin = isServerAdmin;
    }

    @Basic
    @Column(name = "DEFAULT_GRID_POSITION")
    public int getDefaultGridPosition() {
        return defaultGridPosition;
    }

    public void setDefaultGridPosition(int defaultGridPosition) {
        this.defaultGridPosition = defaultGridPosition;
    }

    @Basic
    @Column(name = "BALLAST_KG")
    public int getBallastKg() {
        return ballastKg;
    }

    public void setBallastKg(int ballastKg) {
        this.ballastKg = ballastKg;
    }

    @Basic
    @Column(name = "RESTRICTOR")
    public int getRestrictor() {
        return restrictor;
    }

    public void setRestrictor(int restrictor) {
        this.restrictor = restrictor;
    }
}
