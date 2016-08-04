package com.jcoapps.snowmobile_trail_maps.models;

import java.sql.Timestamp;

/**
 * Created by Jeremy on 8/2/2016.
 */
public class TrailPathsDB {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Float latitude;
    private Float longitude;
    private TrailsDB trail;

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

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public TrailsDB getTrail() {
        return trail;
    }

    public void setTrail(TrailsDB trail) {
        this.trail = trail;
    }
}
