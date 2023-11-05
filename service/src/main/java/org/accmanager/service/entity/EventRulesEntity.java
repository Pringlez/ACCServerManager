package org.accmanager.service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "EVENT_RULES")
public class EventRulesEntity {

    private String eventRulesId;
    private int qualifyStandingType;
    private int superpoleMaxCar;
    private int pitWindowLengthSec;
    private int driverStintTimeSec;
    private int mandatoryPitstopCount;
    private int maxTotalDrivingTime;
    private int maxDriversCount;
    private boolean isRefuellingAllowedInRace;
    private boolean isRefuellingTimeFixed;
    private boolean isMandatoryPitstopRefuellingRequired;
    private boolean isMandatoryPitstopTyreChangeRequired;
    private boolean isMandatoryPitstopSwapDriverRequired;
    private int tyreSetCount;

    @Id
    @Column(name = "EVENT_RULES_ID")
    public String getEventRulesId() {
        return eventRulesId;
    }

    public void setEventRulesId(String eventRulesId) {
        this.eventRulesId = eventRulesId;
    }

    @Basic
    @Column(name = "QUALIFY_STANDING_TYPE")
    public int getQualifyStandingType() {
        return qualifyStandingType;
    }

    public void setQualifyStandingType(int qualifyStandingType) {
        this.qualifyStandingType = qualifyStandingType;
    }

    @Basic
    @Column(name = "SUPERPOLE_MAX_CAR")
    public int getSuperpoleMaxCar() {
        return superpoleMaxCar;
    }

    public void setSuperpoleMaxCar(int superpoleMaxCar) {
        this.superpoleMaxCar = superpoleMaxCar;
    }

    @Basic
    @Column(name = "PIT_WINDOW_LENGTH_SEC")
    public int getPitWindowLengthSec() {
        return pitWindowLengthSec;
    }

    public void setPitWindowLengthSec(int pitWindowLengthSec) {
        this.pitWindowLengthSec = pitWindowLengthSec;
    }

    @Basic
    @Column(name = "DRIVER_STINT_TIME_SEC")
    public int getDriverStintTimeSec() {
        return driverStintTimeSec;
    }

    public void setDriverStintTimeSec(int driverStintTimeSec) {
        this.driverStintTimeSec = driverStintTimeSec;
    }

    @Basic
    @Column(name = "MANDATORY_PITSTOP_COUNT")
    public int getMandatoryPitstopCount() {
        return mandatoryPitstopCount;
    }

    public void setMandatoryPitstopCount(int mandatoryPitstopCount) {
        this.mandatoryPitstopCount = mandatoryPitstopCount;
    }

    @Basic
    @Column(name = "MAX_TOTAL_DRIVING_TIME")
    public int getMaxTotalDrivingTime() {
        return maxTotalDrivingTime;
    }

    public void setMaxTotalDrivingTime(int maxTotalDrivingTime) {
        this.maxTotalDrivingTime = maxTotalDrivingTime;
    }

    @Basic
    @Column(name = "MAX_DRIVERS_COUNT")
    public int getMaxDriversCount() {
        return maxDriversCount;
    }

    public void setMaxDriversCount(int maxDriversCount) {
        this.maxDriversCount = maxDriversCount;
    }

    @Basic
    @Column(name = "IS_REFUELLING_ALLOWED_IN_RACE")
    public boolean getIsRefuellingAllowedInRace() {
        return isRefuellingAllowedInRace;
    }

    public void setIsRefuellingAllowedInRace(boolean isRefuellingAllowedInRace) {
        this.isRefuellingAllowedInRace = isRefuellingAllowedInRace;
    }

    @Basic
    @Column(name = "IS_REFUELLING_TIME_FIXED")
    public boolean getIsRefuellingTimeFixed() {
        return isRefuellingTimeFixed;
    }

    public void setIsRefuellingTimeFixed(boolean isRefuellingTimeFixed) {
        this.isRefuellingTimeFixed = isRefuellingTimeFixed;
    }

    @Basic
    @Column(name = "IS_MANDATORY_PITSTOP_REFUELLING_REQUIRED")
    public boolean getIsMandatoryPitstopRefuellingRequired() {
        return isMandatoryPitstopRefuellingRequired;
    }

    public void setIsMandatoryPitstopRefuellingRequired(boolean isMandatoryPitstopRefuellingRequired) {
        this.isMandatoryPitstopRefuellingRequired = isMandatoryPitstopRefuellingRequired;
    }

    @Basic
    @Column(name = "IS_MANDATORY_PITSTOP_TYRE_CHANGE_REQUIRED")
    public boolean getIsMandatoryPitstopTyreChangeRequired() {
        return isMandatoryPitstopTyreChangeRequired;
    }

    public void setIsMandatoryPitstopTyreChangeRequired(boolean isMandatoryPitstopTyreChangeRequired) {
        this.isMandatoryPitstopTyreChangeRequired = isMandatoryPitstopTyreChangeRequired;
    }

    @Basic
    @Column(name = "IS_MANDATORY_PITSTOP_SWAP_DRIVER_REQUIRED")
    public boolean getIsMandatoryPitstopSwapDriverRequired() {
        return isMandatoryPitstopSwapDriverRequired;
    }

    public void setIsMandatoryPitstopSwapDriverRequired(boolean isMandatoryPitstopSwapDriverRequired) {
        this.isMandatoryPitstopSwapDriverRequired = isMandatoryPitstopSwapDriverRequired;
    }

    @Basic
    @Column(name = "TYRE_SET_COUNT")
    public int getTyreSetCount() {
        return tyreSetCount;
    }

    public void setTyreSetCount(int tyreSetCount) {
        this.tyreSetCount = tyreSetCount;
    }
}
