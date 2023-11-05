package org.accmanager.service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ASSIST_RULES")
public class AssistRulesEntity {

    private String assistsId;
    private int stabilityControlLevelMax;
    private int disableAutoSteer;
    private int disableAutoLights;
    private int disableAutoWiper;
    private int disableAutoEngineStart;
    private int disableAutoPitLimiter;
    private int disableAutoGear;
    private int disableAutoClutch;
    private int disableIdealLine;

    @Id
    @Column(name = "ASSIST_RULES_ID")
    public String getAssistsId() {
        return assistsId;
    }

    public void setAssistsId(String assistsId) {
        this.assistsId = assistsId;
    }

    @Basic
    @Column(name = "STABILITY_CONTROL_LEVEL_MAX")
    public int getStabilityControlLevelMax() {
        return stabilityControlLevelMax;
    }

    public void setStabilityControlLevelMax(int stabilityControlLevelMax) {
        this.stabilityControlLevelMax = stabilityControlLevelMax;
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

    @Basic
    @Column(name = "DISABLE_AUTO_ENGINE_START")
    public int getDisableAutoEngineStart() {
        return disableAutoEngineStart;
    }

    public void setDisableAutoEngineStart(int disableAutoEngineStart) {
        this.disableAutoEngineStart = disableAutoEngineStart;
    }

    @Basic
    @Column(name = "DISABLE_AUTO_PIT_LIMITER")
    public int getDisableAutoPitLimiter() {
        return disableAutoPitLimiter;
    }

    public void setDisableAutoPitLimiter(int disableAutoPitLimiter) {
        this.disableAutoPitLimiter = disableAutoPitLimiter;
    }

    @Basic
    @Column(name = "DISABLE_AUTO_GEAR")
    public int getDisableAutoGear() {
        return disableAutoGear;
    }

    public void setDisableAutoGear(int disableAutoGear) {
        this.disableAutoGear = disableAutoGear;
    }

    @Basic
    @Column(name = "DISABLE_AUTO_CLUTCH")
    public int getDisableAutoClutch() {
        return disableAutoClutch;
    }

    public void setDisableAutoClutch(int disableAutoClutch) {
        this.disableAutoClutch = disableAutoClutch;
    }

    @Basic
    @Column(name = "DISABLE_IDEAL_LINE")
    public int getDisableIdealLine() {
        return disableIdealLine;
    }

    public void setDisableIdealLine(int disableIdealLine) {
        this.disableIdealLine = disableIdealLine;
    }
}
