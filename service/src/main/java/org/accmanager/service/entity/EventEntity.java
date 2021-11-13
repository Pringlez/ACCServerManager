package org.accmanager.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "EVENT")
public class EventEntity {

    private String eventId;
    private String eventName;
    private String track;
    private int preRaceWaitingTimeSec;
    private int sessionOverTimeSec;
    private int ambientTemp;
    private int trackTemp;
    private double cloudLevel;
    private double rain;
    private int weatherRandomness;
    private int postQualyTimeSec;
    private int postRaceTimeSec;
    private String metaData;
    private String sessionId;

    @Id
    @Column(name = "EVENT_ID")
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Basic
    @Column(name = "EVENT_NAME")
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Basic
    @Column(name = "TRACK")
    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    @Basic
    @Column(name = "PRE_RACE_WAITING_TIME_SEC")
    public int getPreRaceWaitingTimeSec() {
        return preRaceWaitingTimeSec;
    }

    public void setPreRaceWaitingTimeSec(int preRaceWaitingTimeSec) {
        this.preRaceWaitingTimeSec = preRaceWaitingTimeSec;
    }

    @Basic
    @Column(name = "SESSION_OVER_TIME_SEC")
    public int getSessionOverTimeSec() {
        return sessionOverTimeSec;
    }

    public void setSessionOverTimeSec(int sessionOverTimeSec) {
        this.sessionOverTimeSec = sessionOverTimeSec;
    }

    @Basic
    @Column(name = "AMBIENT_TEMP")
    public int getAmbientTemp() {
        return ambientTemp;
    }

    public void setAmbientTemp(int ambientTemp) {
        this.ambientTemp = ambientTemp;
    }

    @Basic
    @Column(name = "TRACK_TEMP")
    public int getTrackTemp() {
        return trackTemp;
    }

    public void setTrackTemp(int trackTemp) {
        this.trackTemp = trackTemp;
    }

    @Basic
    @Column(name = "CLOUD_LEVEL")
    public double getCloudLevel() {
        return cloudLevel;
    }

    public void setCloudLevel(double cloudLevel) {
        this.cloudLevel = cloudLevel;
    }

    @Basic
    @Column(name = "RAIN")
    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    @Basic
    @Column(name = "WEATHER_RANDOMNESS")
    public int getWeatherRandomness() {
        return weatherRandomness;
    }

    public void setWeatherRandomness(int weatherRandomness) {
        this.weatherRandomness = weatherRandomness;
    }

    @Basic
    @Column(name = "POST_QUALY_TIME_SEC")
    public int getPostQualyTimeSec() {
        return postQualyTimeSec;
    }

    public void setPostQualyTimeSec(int postQualyTimeSec) {
        this.postQualyTimeSec = postQualyTimeSec;
    }

    @Basic
    @Column(name = "POST_RACE_TIME_SEC")
    public int getPostRaceTimeSec() {
        return postRaceTimeSec;
    }

    public void setPostRaceTimeSec(int postRaceTimeSec) {
        this.postRaceTimeSec = postRaceTimeSec;
    }

    @Basic
    @Column(name = "META_DATA")
    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    @Basic
    @Column(name = "SESSION_ID")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
