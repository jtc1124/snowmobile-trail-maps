package com.jcoapps.snowmobile_trail_maps.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.jcoapps.snowmobile_trail_maps.models.MaintenanceEntriesDB;
import com.jcoapps.snowmobile_trail_maps.models.MaintenanceLogsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

/**
 * Created by Jeremy on 8/1/2016.
 */
public class MaintenanceLogsDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public MaintenanceLogsDao(SnowmobileTrailDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean saveOrUpdateMaintenanceLog(MaintenanceLogsDB log) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.UPDATED_AT, System.currentTimeMillis());
        cv.put(dbHelper.MAINTENANCE_LOG_NAME, log.getName());
        cv.put(dbHelper.MAINTENANCE_LOG_NOTES, log.getNotes());

        // TODO: if the log contains entries, call the maintenancelogentriesdao.saveorupdate
        if (log.getMaintenanceEntries() != null) {
            MaintenanceEntriesDao entryDao = new MaintenanceEntriesDao(dbHelper);
            for (MaintenanceEntriesDB entry : log.getMaintenanceEntries()) {
                entryDao.saveOrUpdateMaintenanceEntry(entry);
            }
        }

        if (log.getId() != null) {
            // If the ID exists, then do an update
            int nRowsAffected = db.update(dbHelper.MAINTENANCE_LOGS_TABLE, cv, dbHelper.ID + "=?", new String[]{log.getId().toString()});

            db.close();
            return nRowsAffected > 0;
        }
        else {
            // If the ID does not exist, create the record
            cv.put(dbHelper.CREATED_AT, System.currentTimeMillis());

            long id = db.insert(dbHelper.MAINTENANCE_LOGS_TABLE, null, cv);
            log.setId(id);

            db.close();

            return id > 0;
        }
    }
}
