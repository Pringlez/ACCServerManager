package org.accmanager.service.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "BOP_ENTRY")
public class BopEntryEntity {

    private String bopEntryId;
    private String bopId;
    private String track;
    private int carModel;
    private int ballest;
    private int restrictor;

    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "BOP_ENTRY_ID")
    public String getBopEntryId() {
        return bopEntryId;
    }

    public void setBopEntryId(String bopEntryId) {
        this.bopEntryId = bopEntryId;
    }

    @Basic
    @Column(name = "BOP_ID")
    public String getBopId() {
        return bopId;
    }

    public void setBopId(String bopId) {
        this.bopId = bopId;
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
    @Column(name = "CAR_MODEL")
    public int getCarModel() {
        return carModel;
    }

    public void setCarModel(int carModel) {
        this.carModel = carModel;
    }

    @Basic
    @Column(name = "BALLEST")
    public int getBallest() {
        return ballest;
    }

    public void setBallest(int ballest) {
        this.ballest = ballest;
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
