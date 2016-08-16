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
public class MaintenanceEntriesDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public MaintenanceEntriesDao(SnowmobileTrailDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean saveOrUpdateMaintenanceEntry(MaintenanceEntriesDB entry) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.UPDATED_AT, System.currentTimeMillis());
        cv.put(dbHelper.MAINTENANCE_ENTRY_NOTES, entry.getNotes());

        MaintenanceTypesDB type = entry.getType();
        if (type != null) {
            cv.put(dbHelper.MAINTENANCE_ENTRY_TYPE_ID, type.getId());
        }
        MaintenanceLogsDB log = entry.getLog();
        if (log != null) {
            cv.put(dbHelper.MAINTENANCE_ENTRY_LOG_ID, log.getId());
        }

        if (entry.getId() != null) {
            // If the ID exists, then do an update
            int nRowsAffected = db.update(dbHelper.MAINTENANCE_ENTRIES_TABLE, cv, dbHelper.ID + "=?", new String[]{entry.getId().toString()});

            db.close();
            return nRowsAffected > 0;
        }
        else {
            // If the ID does not exist, create the record
            cv.put(dbHelper.CREATED_AT, System.currentTimeMillis());

            long id = db.insert(dbHelper.MAINTENANCE_ENTRIES_TABLE, null, cv);
            entry.setId(id);

            db.close();

            return id > 0;
        }
    }

    public List<MaintenanceEntriesDB> getMaintenanceEntriesByMaintenanceLog(MaintenanceLogsDB maintenanceLog) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<MaintenanceEntriesDB> entryList = new ArrayList<MaintenanceEntriesDB>();
        String selectEntriesByLogIdQuery = "SELECT id, created_at, updated_at, notes, maintenance_type_id, maintenance_log_id FROM " + dbHelper.MAINTENANCE_ENTRIES_TABLE + " WHERE maintenance_log_id = " + maintenanceLog.getId() + ";";
        Cursor entriesCursor = db.rawQuery(selectEntriesByLogIdQuery, null);

        // get associated log entries
        if (entriesCursor.moveToFirst()) {
            do {
                MaintenanceEntriesDB entry = new MaintenanceEntriesDB();

                entry.setId(entriesCursor.getLong(0));
                entry.setCreatedAt(new Timestamp(entriesCursor.getLong(1)));
                entry.setUpdatedAt(new Timestamp(entriesCursor.getLong(2)));
                entry.setNotes(entriesCursor.getString(3));

                // set the maintenance log this entry is associated with
                entry.setLog(maintenanceLog);

                // get associated maintenance type
                MaintenanceTypesDao typesDao = new MaintenanceTypesDao(dbHelper);
                MaintenanceTypesDB maintenanceType = typesDao.getMaintenanceTypeById(entriesCursor.getLong(4));

                // set type in entry
                entry.setType(maintenanceType);
                // add entry to entry list
                entryList.add(entry);
            } while (entriesCursor.moveToNext());
        }

        db.close();
        return entryList;
    }
}
