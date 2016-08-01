package com.jcoapps.snowmobile_trail_maps.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcoapps.snowmobile_trail_maps.models.MaintenanceEntriesDB;
import com.jcoapps.snowmobile_trail_maps.models.MaintenanceLogsDB;
import com.jcoapps.snowmobile_trail_maps.models.MaintenanceTypesDB;
import com.jcoapps.snowmobile_trail_maps.models.SledsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jeremy on 7/31/2016.
 */
public class SledsDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public SledsDao(SnowmobileTrailDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // TODO: refactor this mess
    public List<SledsDB> getAllSleds() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<SledsDB> sledsList = new ArrayList<SledsDB>();
        String selectQuery = "SELECT * FROM " + dbHelper.SLEDS_TABLE + ";";

        Cursor sledsCursor = db.rawQuery(selectQuery, null);

        if (sledsCursor.moveToFirst()) {
            do {
                SledsDB sled = new SledsDB();
                MaintenanceLogsDB maintenanceLog = new MaintenanceLogsDB();

                // get sled data
                sled.setId(sledsCursor.getLong(0));
                sled.setCreatedAt(new Timestamp(sledsCursor.getLong(1)));
                sled.setUpdatedAt(new Timestamp(sledsCursor.getLong(2)));
                sled.setName(sledsCursor.getString(3));
                sled.setMileage(sledsCursor.getDouble(4));
                sled.setNotes(sledsCursor.getString(5));

                // get associated maintenance log data
                maintenanceLog.setId(sledsCursor.getLong(6));
                String selectMaintenanceLogByIdQuery = "SELECT * FROM " + dbHelper.MAINTENANCE_LOGS_TABLE + " WHERE id = " + maintenanceLog.getId() + ";";
                Cursor maintenanceCursor = db.rawQuery(selectMaintenanceLogByIdQuery, null);

                if (maintenanceCursor.moveToFirst()) {
                    List<MaintenanceEntriesDB> entryList = new ArrayList<MaintenanceEntriesDB>();
                    maintenanceLog.setCreatedAt(new Timestamp(maintenanceCursor.getLong(1)));
                    maintenanceLog.setUpdatedAt(new Timestamp(maintenanceCursor.getLong(2)));
                    maintenanceLog.setName(maintenanceCursor.getString(3));
                    maintenanceLog.setNotes(maintenanceCursor.getString(4));

                    String selectEntriesByLogIdQuery = "SELECT * FROM " + dbHelper.MAINTENANCE_ENTRIES_TABLE + " WHERE maintenance_log_id = " + maintenanceLog.getId() + ";";
                    Cursor entriesCursor = db.rawQuery(selectEntriesByLogIdQuery, null);

                    // get associated log entries
                    if (entriesCursor.moveToFirst()) {
                        do {
                            MaintenanceEntriesDB entry = new MaintenanceEntriesDB();
                            MaintenanceTypesDB maintenanceType = new MaintenanceTypesDB();

                            entry.setId(entriesCursor.getLong(0));
                            entry.setCreatedAt(new Timestamp(entriesCursor.getLong(1)));
                            entry.setUpdatedAt(new Timestamp(entriesCursor.getLong(2)));
                            entry.setNotes(entriesCursor.getString(3));

                            maintenanceType.setId(entriesCursor.getLong(4));

                            // set the maintenance log this entry is associated with
                            entry.setLog(maintenanceLog);

                            // get associated maintenance type
                            String selectMaintenanceTypeByIdQuery = "SELECT * FROM " + dbHelper.MAINTENANCE_TYPES_TABLE + " WHERE id = " + maintenanceType.getId() + ";";
                            Cursor typeCursor = db.rawQuery(selectMaintenanceTypeByIdQuery, null);

                            if (typeCursor.moveToFirst()) {
                                maintenanceType.setCreatedAt(new Timestamp(typeCursor.getLong(1)));
                                maintenanceType.setUpdatedAt(new Timestamp(typeCursor.getLong(2)));
                                maintenanceType.setName(typeCursor.getString(3));
                            }

                            // set type in entry
                            entry.setType(maintenanceType);
                            // add entry to entry list
                            entryList.add(entry);
                        } while (entriesCursor.moveToNext());
                    }

                    // Set entry data in log
                    maintenanceLog.setMaintenanceEntries(entryList);
                }

                // set maintenance log in sled
                sled.setMaintenanceLog(maintenanceLog);
                sledsList.add(sled);
            } while (sledsCursor.moveToNext());
        }

        db.close();

        return sledsList;
    }

    // TODO update this method to work with log entries changes
    public boolean saveOrUpdateSled(SledsDB sled) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.UPDATED_AT, System.currentTimeMillis());
        cv.put(dbHelper.SLED_NAME, sled.getName());
        cv.put(dbHelper.SLED_MILEAGE, sled.getMileage());
        cv.put(dbHelper.SLED_NOTES, sled.getNotes());

        MaintenanceLogsDB maintenanceLog = sled.getMaintenanceLog();
        if (maintenanceLog != null) {
            cv.put(dbHelper.SLED_MAINTENANCE_LOG_ID, maintenanceLog.getId());
        }

        // If the SledDB object has an ID, it exists in the DB. Therefore, update.
        if (sled.getId() != null) {
            int nRowsAffected = db.update(dbHelper.SLEDS_TABLE, cv, dbHelper.ID + "=?", new String[]{sled.getId().toString()});

            db.close();

            return nRowsAffected > 0;
        }
        else {
            // Otherwise if the SledDB object has no ID, create the record.
            cv.put(dbHelper.CREATED_AT, System.currentTimeMillis());

            long id = db.insert(dbHelper.SLEDS_TABLE, null, cv);
            sled.setId(id);

            db.close();

            return id > 0;
        }
    }
}
