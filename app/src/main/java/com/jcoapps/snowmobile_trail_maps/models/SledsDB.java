package com.jcoapps.snowmobile_trail_maps.models;

import java.sql.Timestamp;

/**
 * Created by Jeremy on 7/30/2016.
 */
public class SledsDB {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String name;
    private Double mileage;
    private String notes;
    private MaintenanceLogsDB maintenanceLog;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MaintenanceLogsDB getMaintenanceLog() {
        return maintenanceLog;
    }

    public void setMaintenanceLog(MaintenanceLogsDB maintenanceLog) {
        this.maintenanceLog = maintenanceLog;
    }
}
