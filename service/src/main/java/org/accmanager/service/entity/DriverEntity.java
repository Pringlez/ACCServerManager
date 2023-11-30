package org.accmanager.service.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "DRIVER")
public class DriverEntity {

    private String driverId;
    private String carEntryId;
    private String firstName;
    private String lastName;
    private String shortName;
    private int driverCategory;
    private String playerId;

    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "DRIVER_ID")
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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
