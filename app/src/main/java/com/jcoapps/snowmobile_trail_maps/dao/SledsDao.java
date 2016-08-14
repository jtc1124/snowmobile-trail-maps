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
import java.util.List;

/**
 * Created by Jeremy on 7/31/2016.
 */
public class SledsDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public SledsDao(SnowmobileTrailDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<SledsDB> getAllSleds() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<SledsDB> sledsList = new ArrayList<SledsDB>();
        String selectQuery = "SELECT id, created_at, updated_at, year, make, model, mileage, notes, maintenance_log_id  FROM " + dbHelper.SLEDS_TABLE + ";";

        Cursor sledsCursor = db.rawQuery(selectQuery, null);

        if (sledsCursor.moveToFirst()) {
            do {
                SledsDB sled = new SledsDB();

                // get sled data
                sled.setId(sledsCursor.getLong(0));
                sled.setCreatedAt(new Timestamp(sledsCursor.getLong(1)));
                sled.setUpdatedAt(new Timestamp(sledsCursor.getLong(2)));
                sled.setYear(sledsCursor.getInt(3));
                sled.setMake(sledsCursor.getString(4));
                sled.setModel(sledsCursor.getString(5));
                sled.setMileage(sledsCursor.getDouble(6));
                sled.setNotes(sledsCursor.getString(7));

                // get associated maintenance log data
                MaintenanceLogsDao maintenanceLogsDao = new MaintenanceLogsDao(dbHelper);
                MaintenanceLogsDB maintenanceLog = maintenanceLogsDao.getMaintenanceLogById(sledsCursor.getLong(8));

                // set maintenance log in sled
                sled.setMaintenanceLog(maintenanceLog);
                sledsList.add(sled);
            } while (sledsCursor.moveToNext());
        }

        db.close();
        return sledsList;
    }

    public boolean saveOrUpdateSled(SledsDB sled) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.UPDATED_AT, System.currentTimeMillis());
        cv.put(dbHelper.SLED_YEAR, sled.getYear());
        cv.put(dbHelper.SLED_MAKE, sled.getMake());
        cv.put(dbHelper.SLED_MODEL, sled.getModel());
        cv.put(dbHelper.SLED_MILEAGE, sled.getMileage());
        cv.put(dbHelper.SLED_NOTES, sled.getNotes());

        MaintenanceLogsDB maintenanceLog = sled.getMaintenanceLog();
        if (maintenanceLog != null) {
            cv.put(dbHelper.SLED_MAINTENANCE_LOG_ID, maintenanceLog.getId());
            MaintenanceLogsDao logsDao = new MaintenanceLogsDao(dbHelper);
            logsDao.saveOrUpdateMaintenanceLog(maintenanceLog);
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
