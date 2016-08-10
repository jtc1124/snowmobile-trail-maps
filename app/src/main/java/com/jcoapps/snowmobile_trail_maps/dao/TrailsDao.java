package com.jcoapps.snowmobile_trail_maps.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcoapps.snowmobile_trail_maps.models.TrailPathsDB;
import com.jcoapps.snowmobile_trail_maps.models.TrailsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 8/3/2016.
 */
public class TrailsDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public TrailsDao(SnowmobileTrailDatabaseHelper dbHelper) { this.dbHelper = dbHelper; }

    public List<TrailsDB> getAllTrails() {
        List<TrailsDB> trails = new ArrayList<TrailsDB>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "SELECT id, created_at, updated_at, name FROM " + dbHelper.TRAILS_TABLE + ";";

        Cursor trailsCursor = db.rawQuery(selectQuery, null);

        if (trailsCursor.moveToFirst()) {
            do {
                // For each trail, get all TrailPathsDB entries associated
                TrailsDB trail = new TrailsDB();

                trail.setId(trailsCursor.getLong(0));
                trail.setCreatedAt(new Timestamp(Long.parseLong(trailsCursor.getString(1))));
                trail.setUpdatedAt(new Timestamp(Long.parseLong(trailsCursor.getString(2))));
                trail.setName(trailsCursor.getString(3));

                // TODO maybe we don't want to pull the trail paths at first since they can be very large,
                // maybe only get these when we want to inspect the trail details or draw it on the map
                List<TrailPathsDB> paths = new ArrayList<TrailPathsDB>();
                String selectPathsQuery = "SELECT id, created_at, updated_at, latitude, longitude, trail_id FROM " + dbHelper.TRAIL_PATHS_TABLE + " WHERE trail_id = " + trail.getId() + ";";

                Cursor pathsCursor = db.rawQuery(selectPathsQuery, null);

                if (pathsCursor.moveToFirst()) {
                    do {
                        TrailPathsDB path = new TrailPathsDB();

                        path.setId(pathsCursor.getLong(0));
                        path.setCreatedAt(new Timestamp(Long.parseLong(pathsCursor.getString(1))));
                        path.setUpdatedAt(new Timestamp(Long.parseLong(pathsCursor.getString(2))));
                        path.setLatitude(pathsCursor.getDouble(3));
                        path.setLongitude(pathsCursor.getDouble(4));
                        path.setTrail(trail);
                        paths.add(path);
                    } while (pathsCursor.moveToNext());
                }

                trail.setPaths(paths);
                trails.add(trail);
            } while (trailsCursor.moveToNext());
        }

        db.close();
        return trails;
    }

    public TrailsDB getTrailByName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        TrailsDB trail = new TrailsDB();

        String selectQuery = "SELECT id, created_at, updated_at, name  FROM " + dbHelper.TRAILS_TABLE + " WHERE name = '" + name + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            trail.setId(cursor.getLong(0));
            trail.setCreatedAt(new Timestamp(Long.parseLong(cursor.getString(1))));
            trail.setUpdatedAt(new Timestamp(Long.parseLong(cursor.getString(2))));
            trail.setName(cursor.getString(3));
        }

        String selectPathsQuery = "SELECT id, created_at, updated_at, latitude, longitude, trail_id FROM " + dbHelper.TRAIL_PATHS_TABLE + " WHERE trail_id = " + trail.getId() + ";";

        Cursor pathsCursor = db.rawQuery(selectPathsQuery, null);

        List<TrailPathsDB> trailPaths = new ArrayList<TrailPathsDB>();
        if (pathsCursor.moveToFirst()) {
            do {
                TrailPathsDB path = new TrailPathsDB();
                path.setId(pathsCursor.getLong(0));
                path.setCreatedAt(new Timestamp(Long.parseLong(pathsCursor.getString(1))));
                path.setUpdatedAt(new Timestamp(Long.parseLong(pathsCursor.getString(2))));
                path.setLatitude(pathsCursor.getDouble(3));
                path.setLongitude(pathsCursor.getDouble(4));
                path.setTrail(trail);
                trailPaths.add(path);
            } while(pathsCursor.moveToNext());
        }

        trail.setPaths(trailPaths);

        db.close();
        return trail;
    }

    public boolean saveOrUpdateTrail(TrailsDB trail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.UPDATED_AT, System.currentTimeMillis());
        cv.put(dbHelper.TRAIL_NAME, trail.getName());

        // If the TrailsDB object has an ID, it exists in the DB. Therefore, update.
        if (trail.getId() != null) {
            int nRowsAffected = db.update(dbHelper.TRAILS_TABLE, cv, dbHelper.ID + "=?", new String[]{trail.getId().toString()});

            db.close();

            if (nRowsAffected > 0) {
                saveTrailPaths(trail);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            // Otherwise if the TrailsDB object has no ID, create the record.
            cv.put(dbHelper.CREATED_AT, System.currentTimeMillis());

            long id = db.insert(dbHelper.TRAILS_TABLE, null, cv);
            trail.setId(id);

            db.close();

            if (id > 0) {
                saveTrailPaths(trail);
                return true;
            }
            else {
                return false;
            }
        }
    }

    private void saveTrailPaths(TrailsDB trail) {
        if (trail.getPaths() != null) {
            TrailPathsDao pathDao = new TrailPathsDao(dbHelper);
            for (TrailPathsDB path : trail.getPaths()) {
                pathDao.saveOrUpdateTrailPath(path);
            }
        }
    }
}
