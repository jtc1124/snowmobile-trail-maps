package com.jcoapps.snowmobile_trail_maps.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Jeremy on 7/31/2016.
 */
public class MaintenanceEntriesDB implements Serializable {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String notes;
    private MaintenanceTypesDB type;
    private MaintenanceLogsDB log;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MaintenanceTypesDB getType() {
        return type;
    }

    public void setType(MaintenanceTypesDB type) {
        this.type = type;
    }

    public MaintenanceLogsDB getLog() {
        return log;
    }

    public void setLog(MaintenanceLogsDB log) {
        this.log = log;
    }
}
