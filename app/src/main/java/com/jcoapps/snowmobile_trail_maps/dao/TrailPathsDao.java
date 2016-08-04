package com.jcoapps.snowmobile_trail_maps.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.jcoapps.snowmobile_trail_maps.models.TrailPathsDB;
import com.jcoapps.snowmobile_trail_maps.models.TrailsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

/**
 * Created by Jeremy on 8/3/2016.
 */
public class TrailPathsDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public TrailPathsDao(SnowmobileTrailDatabaseHelper dbHelper) { this.dbHelper = dbHelper; }

    public boolean saveOrUpdateTrailPath(TrailPathsDB path) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.UPDATED_AT, System.currentTimeMillis());
        cv.put(dbHelper.TRAIL_PATH_LATITUDE, path.getLatitude());
        cv.put(dbHelper.TRAIL_PATH_LONGITUDE, path.getLongitude());

        TrailsDB trail = path.getTrail();
        if (trail != null) {
            cv.put(dbHelper.TRAIL_PATH_TRAIL_ID, trail.getId());
        }

        if (path.getId() != null) {
            // If the ID exists, then do an update
            int nRowsAffected = db.update(dbHelper.TRAIL_PATHS_TABLE, cv, dbHelper.ID + "=?", new String[]{path.getId().toString()});

            db.close();
            return nRowsAffected > 0;
        }
        else {
            // If the ID does not exist, create the record
            cv.put(dbHelper.CREATED_AT, System.currentTimeMillis());

            long id = db.insert(dbHelper.TRAIL_PATHS_TABLE, null, cv);
            path.setId(id);

            db.close();

            return id > 0;
        }
    }
}
