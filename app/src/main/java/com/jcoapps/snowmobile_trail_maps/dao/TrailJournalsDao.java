package com.jcoapps.snowmobile_trail_maps.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcoapps.snowmobile_trail_maps.models.ConditionTypesDB;
import com.jcoapps.snowmobile_trail_maps.models.SledsDB;
import com.jcoapps.snowmobile_trail_maps.models.TrailJournalsDB;
import com.jcoapps.snowmobile_trail_maps.models.TrailsDB;
import com.jcoapps.snowmobile_trail_maps.schema.SnowmobileTrailDatabaseHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 8/7/2016.
 */
public class TrailJournalsDao {

    SnowmobileTrailDatabaseHelper dbHelper;

    public TrailJournalsDao(SnowmobileTrailDatabaseHelper dbHelper) { this.dbHelper = dbHelper; }

    public List<TrailJournalsDB> getTrailJournalsByTrailId(Long trailId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<TrailJournalsDB> journals = new ArrayList<TrailJournalsDB>();

        String selectJournalsQuery = "SELECT id, created_at, updated_at, entry_name, miles, max_speed, min_speed, avg_speed, trail_id, sled_id, condition_type_id " +
                                     "FROM " + dbHelper.TRAIL_JOURNALS_TABLE + " WHERE trail_id = " + trailId + ";";

        Cursor journalsCursor = db.rawQuery(selectJournalsQuery, null);

        if (journalsCursor.moveToFirst()) {
            do {
                TrailJournalsDB journal = new TrailJournalsDB();
                journal.setId(journalsCursor.getLong(0));
                journal.setCreatedAt(new Timestamp(Long.parseLong(journalsCursor.getString(1))));
                journal.setUpdatedAt(new Timestamp(Long.parseLong(journalsCursor.getString(2))));
                journal.setEntryName(journalsCursor.getString(3));
                journal.setMiles(journalsCursor.getDouble(4));
                journal.setMaxSpeed(journalsCursor.getInt(5));
                journal.setMinSpeed(journalsCursor.getInt(6));
                journal.setAvgSpeed(journalsCursor.getInt(7));

                /*
                // TODO call TrailsDao
                TrailsDao trailsDao = new TrailsDao(dbHelper);
                TrailsDB trail = trailsDao.getTrailById(journalsCursor.getLong(8));
                journal.setTrail(trail);

                // TODO call SledsDao
                SledsDao sledsDao = new SledsDao(dbHelper);
                SledsDB sled = sledsDao.getSledById(journalsCursor.getLong(9));
                journal.setSled(sled);

                // TODO call ConditionTypesDao
                ConditionTypesDao conditionTypesDao = new ConditionTypesDao(dbHelper);
                ConditionTypesDB conditionType = conditionTypesDao.getConditionTypeById(journalsCursor.getLong(10));
                journal.setConditionType(conditionType);
                */

                journals.add(journal);
            } while (journalsCursor.moveToNext());
        }

        return journals;
    }

    public boolean saveOrUpdateTrailJournal(TrailJournalsDB journal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.UPDATED_AT, System.currentTimeMillis());
        cv.put(dbHelper.TRAIL_JOURNAL_ENTRY_NAME, journal.getEntryName());
        cv.put(dbHelper.TRAIL_JOURNAL_AVG_SPEED, journal.getAvgSpeed());
        cv.put(dbHelper.TRAIL_JOURNAL_MAX_SPEED, journal.getMaxSpeed());
        cv.put(dbHelper.TRAIL_JOURNAL_MIN_SPEED, journal.getMinSpeed());
        cv.put(dbHelper.TRAIL_JOURNAL_MILES, journal.getMiles());

        if (journal.getConditionType() != null) {
            cv.put(dbHelper.TRAIL_JOURNAL_CONDITION_ID, journal.getConditionType().getId());
        }
        if (journal.getTrail() != null) {
            cv.put(dbHelper.TRAIL_JOURNAL_TRAIL_ID, journal.getTrail().getId());
        }
        if (journal.getSled() != null) {
            cv.put(dbHelper.TRAIL_JOURNAL_SLED_ID, journal.getSled().getId());
        }

        if (journal.getId() != null) {
            int nRowsAffected = db.update(dbHelper.TRAIL_JOURNALS_TABLE, cv, dbHelper.ID + "=?", new String[]{journal.getId().toString()});

            db.close();

            return nRowsAffected > 0;
        }
        else {
            // Otherwise if the SledDB object has no ID, create the record.
            cv.put(dbHelper.CREATED_AT, System.currentTimeMillis());

            long id = db.insert(dbHelper.TRAIL_JOURNALS_TABLE, null, cv);
            journal.setId(id);

            db.close();

            return id > 0;
        }
    }
}
