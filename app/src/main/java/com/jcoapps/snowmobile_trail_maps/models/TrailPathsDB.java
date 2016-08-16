package com.jcoapps.snowmobile_trail_maps.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Jeremy on 8/2/2016.
 */
public class TrailPathsDB implements Serializable {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Double latitude;
    private Double longitude;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public TrailsDB getTrail() {
        return trail;
    }

    public void setTrail(TrailsDB trail) {
        this.trail = trail;
    }
}
