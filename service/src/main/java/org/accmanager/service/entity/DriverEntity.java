package org.accmanager.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "DRIVER")
public class DriverEntity {

    private String driverId;
    private String firstName;
    private String lastName;
    private String shortName;
    private int driverCategory;
    private String playerId;

    @Id
    @Column(name = "DRIVER_ID")
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "SHORT_NAME")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Basic
    @Column(name = "DRIVER_CATEGORY")
    public int getDriverCategory() {
        return driverCategory;
    }

    public void setDriverCategory(int driverCategory) {
        this.driverCategory = driverCategory;
    }

    @Basic
    @Column(name = "PLAYER_ID")
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
