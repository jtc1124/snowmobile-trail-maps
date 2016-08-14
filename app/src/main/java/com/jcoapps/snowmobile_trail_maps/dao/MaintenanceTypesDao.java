package com.jcoapps.snowmobile_trail_maps.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcoapps.snowmobile_trail_maps.models.MaintenanceTypesDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.sql.Timestamp;

/**
 * Created by Jeremy on 8/14/2016.
 */
public class MaintenanceTypesDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public MaintenanceTypesDao(SnowmobileTrailDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public MaintenanceTypesDB getMaintenanceTypeById(Long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        MaintenanceTypesDB maintenanceType = new MaintenanceTypesDB();

        String selectMaintenanceTypeByIdQuery = "SELECT id, created_at, updated_at, name FROM " + dbHelper.MAINTENANCE_TYPES_TABLE + " WHERE id = " +id + ";";
        Cursor typeCursor = db.rawQuery(selectMaintenanceTypeByIdQuery, null);

        if (typeCursor.moveToFirst()) {
            maintenanceType.setId(typeCursor.getLong(0));
            maintenanceType.setCreatedAt(new Timestamp(typeCursor.getLong(1)));
            maintenanceType.setUpdatedAt(new Timestamp(typeCursor.getLong(2)));
            maintenanceType.setName(typeCursor.getString(3));
        }

        db.close();
        return maintenanceType;
    }
}
