package com.jcoapps.snowmobile_trail_maps.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcoapps.snowmobile_trail_maps.models.ConditionTypesDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 7/30/2016.
 */
public class ConditionTypesDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public ConditionTypesDao(SnowmobileTrailDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<ConditionTypesDB> getAllConditionTypes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ConditionTypesDB> conditionTypesList = new ArrayList<ConditionTypesDB>();
        String selectQuery = "SELECT * FROM " + dbHelper.CONDITION_TYPES_TABLE + ";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ConditionTypesDB conditionType = new ConditionTypesDB();
                conditionType.setId(Long.parseLong(cursor.getString(0)));
                conditionType.setCreatedAt(new Timestamp(Long.parseLong(cursor.getString(1))));
                conditionType.setUpdatedAt(new Timestamp(Long.parseLong(cursor.getString(2))));
                conditionType.setName(cursor.getString(3));
                conditionTypesList.add(conditionType);
            } while (cursor.moveToNext());
        }

        return conditionTypesList;
    }
}
