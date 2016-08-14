package com.jcoapps.snowmobile_trail_maps.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcoapps.snowmobile_trail_maps.models.MaintenanceEntriesDB;
import com.jcoapps.snowmobile_trail_maps.models.MaintenanceLogsDB;
import com.jcoapps.snowmobile_trail_maps.models.MaintenanceTypesDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

        // If the log contains entries, call the maintenancelogentriesdao.saveorupdate
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

    public MaintenanceLogsDB getMaintenanceLogById(Long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        MaintenanceLogsDB maintenanceLog = new MaintenanceLogsDB();
        String selectMaintenanceLogByIdQuery = "SELECT id, created_at, updated_at, name, notes FROM " + dbHelper.MAINTENANCE_LOGS_TABLE + " WHERE id = " + id + ";";
        Cursor maintenanceCursor = db.rawQuery(selectMaintenanceLogByIdQuery, null);

        if (maintenanceCursor.moveToFirst()) {
            maintenanceLog.setId(maintenanceCursor.getLong(0));
            maintenanceLog.setCreatedAt(new Timestamp(maintenanceCursor.getLong(1)));
            maintenanceLog.setUpdatedAt(new Timestamp(maintenanceCursor.getLong(2)));
            maintenanceLog.setName(maintenanceCursor.getString(3));
            maintenanceLog.setNotes(maintenanceCursor.getString(4));

            MaintenanceEntriesDao entriesDao = new MaintenanceEntriesDao(dbHelper);
            List<MaintenanceEntriesDB> entryList = entriesDao.getMaintenanceEntriesByMaintenanceLog(maintenanceLog);

            // Set entry data in log
            maintenanceLog.setMaintenanceEntries(entryList);
        }

        db.close();
        return maintenanceLog;
    }
}
