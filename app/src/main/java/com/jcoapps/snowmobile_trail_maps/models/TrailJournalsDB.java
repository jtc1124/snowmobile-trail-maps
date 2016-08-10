package com.jcoapps.snowmobile_trail_maps.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Jeremy on 7/30/2016.
 */
public class TrailJournalsDB implements Serializable {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String entryName;
    private Double miles;
    private Integer maxSpeed;
    private Integer minSpeed;
    private Integer avgSpeed;
    private ConditionTypesDB conditionType;
    private TrailsDB trail;
    private SledsDB sled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public Double getMiles() {
        return miles;
    }

    public void setMiles(Double miles) {
        this.miles = miles;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Integer minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Integer getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Integer avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public ConditionTypesDB getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionTypesDB conditionType) {
        this.conditionType = conditionType;
    }

    public TrailsDB getTrail() {
        return trail;
    }

    public void setTrail(TrailsDB trail) {
        this.trail = trail;
    }

    public SledsDB getSled() {
        return sled;
    }

    public void setSled(SledsDB sled) {
        this.sled = sled;
    }
}
