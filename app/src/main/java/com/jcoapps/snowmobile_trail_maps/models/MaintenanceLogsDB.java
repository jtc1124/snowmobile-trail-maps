package com.jcoapps.snowmobile_trail_maps.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Jeremy on 7/30/2016.
 */
public class MaintenanceLogsDB implements Serializable {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String name;
    private String notes;
    private Collection<MaintenanceEntriesDB> maintenanceEntries;

    public Long getId() { return id; }

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

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Collection<MaintenanceEntriesDB> getMaintenanceEntries() { return maintenanceEntries; }

    public void setMaintenanceEntries(Collection<MaintenanceEntriesDB> maintenanceEntries) { this.maintenanceEntries = maintenanceEntries; }
}
