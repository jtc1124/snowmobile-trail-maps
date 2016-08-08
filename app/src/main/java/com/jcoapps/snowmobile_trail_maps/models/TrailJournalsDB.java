package com.jcoapps.snowmobile_trail_maps.models;

import java.sql.Timestamp;

/**
 * Created by Jeremy on 7/30/2016.
 */
public class TrailJournalsDB {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String entryName;
    private Double miles;
    private Double maxSpeed;
    private Double minSpeed;
    private Double avgSpeed;
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

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
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
