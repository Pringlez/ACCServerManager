package org.accmanager.service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SESSIONS")
public class SessionsEntity {

    private String sessionId;
    private String eventId;
    private int hourOfDay;
    private int dayOfWeekend;
    private int timeMultiplier;
    private String sessionType;
    private int sessionDurationMin;

    @Id
    @Column(name = "SESSION_ID")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
    @Column(name = "HOUR_OF_DAY")
    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    @Basic
    @Column(name = "DAY_OF_WEEKEND")
    public int getDayOfWeekend() {
        return dayOfWeekend;
    }

    public void setDayOfWeekend(int dayOfWeekend) {
        this.dayOfWeekend = dayOfWeekend;
    }

    @Basic
    @Column(name = "TIME_MULTIPLIER")
    public int getTimeMultiplier() {
        return timeMultiplier;
    }

    public void setTimeMultiplier(int timeMultiplier) {
        this.timeMultiplier = timeMultiplier;
    }

    @Basic
    @Column(name = "SESSION_TYPE")
    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    @Basic
    @Column(name = "SESSION_DURATION_MIN")
    public int getSessionDurationMin() {
        return sessionDurationMin;
    }

    public void setSessionDurationMin(int sessionDurationMin) {
        this.sessionDurationMin = sessionDurationMin;
    }
}
