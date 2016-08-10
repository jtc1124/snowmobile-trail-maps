package com.jcoapps.snowmobile_trail_maps.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jeremy on 8/2/2016.
 */
public class TrailsDB implements Serializable {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String name;
    private Collection<TrailPathsDB> paths;
    private List<TrailJournalsDB> journals;

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

    public Collection<TrailPathsDB> getPaths() {
        return paths;
    }

    public void setPaths(Collection<TrailPathsDB> paths) {
        this.paths = paths;
    }

    public List<TrailJournalsDB> getJournals() {
        return journals;
    }

    public void setJournals(List<TrailJournalsDB> journals) {
        this.journals = journals;
    }

    public void addJournal(TrailJournalsDB journal) {
        if (journals == null) {
            journals = new ArrayList<TrailJournalsDB>();
        }
        journals.add(journal);
    }
}
