package com.jcoapps.snowmobile_trail_maps.schema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jcoapps.snowmobile_trail_maps.types.ConditionTypes;
import com.jcoapps.snowmobile_trail_maps.types.MaintenanceTypes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jcolon on 7/30/2016.
 */
public class SnowmobileTrailDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "snowmobile_trail_maps";
    private static final int DATABASE_VERSION = 1;

    // Columns that belong in every table
    public static final String ID = "id"; // int
    public static final String CREATED_AT = "created_at"; // timestamp
    public static final String UPDATED_AT = "updated_at"; // timestamp

    // UsersDB table
    // Stores user login information
    public static final String USERS_TABLE = "users";
    public static final String USER_EMAIL = "email"; // string
    public static final String USER_NAME = "name"; // string
    public static final String USER_PASSWORD = "password"; // string

    // TrailJournal table
    // Stores statistics for riding sessions
    public static final String TRAIL_JOURNALS_TABLE = "trail_journals";
    public static final String TRAIL_JOURNAL_ENTRY_NAME = "entry_name"; // string
    public static final String TRAIL_JOURNAL_MILES = "miles"; // decimal
    public static final String TRAIL_JOURNAL_MAX_SPEED = "max_speed"; // decimal
    public static final String TRAIL_JOURNAL_MIN_SPEED = "min_speed"; // decimal
    public static final String TRAIL_JOURNAL_AVG_SPEED = "avg_speed"; // decimal
    public static final String TRAIL_JOURNAL_CONDITION_ID = "condition_type_id"; // int

    // ConditionType table
    // Stores codes for trail conditions (smooth, grassy, ice, etc.)
    public static final String CONDITION_TYPES_TABLE = "condition_types";
    public static final String CONDITION_TYPE_NAME = "name"; // string

    // SledsDB table
    // Stores information about snowmobiles
    // TODO: add year, make, model as fields instead of just name
    public static final String SLEDS_TABLE = "sleds";
    public static final String SLED_NAME = "name"; // string
    public static final String SLED_MILEAGE = "mileage"; // decimal
    public static final String SLED_NOTES = "notes"; // string
    public static final String SLED_MAINTENANCE_LOG_ID = "maintenance_log_id"; // int

    // MaintenanceLog table
    // Stores maintenance data for a sled
    public static final String MAINTENANCE_LOGS_TABLE = "maintenance_logs";
    public static final String MAINTENANCE_LOG_NAME = "name"; // string
    public static final String MAINTENANCE_LOG_NOTES = "notes"; // string

    // MaintenanceLogEntry table
    // Stores an entry for a maintenance log
    public static final String MAINTENANCE_ENTRIES_TABLE = "maintenance_entries";
    public static final String MAINTENANCE_ENTRY_NOTES = "notes"; // string
    public static final String MAINTENANCE_ENTRY_TYPE_ID = "maintenance_type_id"; // int
    public static final String MAINTENANCE_ENTRY_LOG_ID = "maintenance_log_id"; // int

    // MaintenanceType table
    // Stores codes for maintenance types (oil change, belt change, etc.)
    public static final String MAINTENANCE_TYPES_TABLE = "maintenance_types";
    public static final String MAINTENANCE_TYPE_NAME = "name"; // string

    // FriendsDB table
    // Stores users who are friends of the user
    public static final String FRIENDS_TABLE = "friends";
    public static final String FRIEND_USER_NAME = "name"; // string
    public static final String FRIEND_FINDER_ACTIVE = "active"; // boolean

    public SnowmobileTrailDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        HashMap<String, String> columns = new HashMap<String, String>();

        columns.put(USER_EMAIL, "TEXT");
        columns.put(USER_NAME, "TEXT NOT NULL");
        columns.put(USER_PASSWORD, "TEXT");
        createTable(db, USERS_TABLE, columns);

        // Clear columns hash and add columns for next table to be created
        columns.clear();

        columns.put(TRAIL_JOURNAL_ENTRY_NAME, "TEXT NOT NULL DEFAULT 'unnamed entry'");
        columns.put(TRAIL_JOURNAL_MILES, "DOUBLE");
        columns.put(TRAIL_JOURNAL_MAX_SPEED, "DOUBLE");
        columns.put(TRAIL_JOURNAL_MIN_SPEED, "DOUBLE");
        columns.put(TRAIL_JOURNAL_AVG_SPEED, "DOUBLE");
        columns.put(TRAIL_JOURNAL_CONDITION_ID, "INT");
        createTable(db, TRAIL_JOURNALS_TABLE, columns);

        columns.clear();

        columns.put(CONDITION_TYPE_NAME, "TEXT NOT NULL");
        createTable(db, CONDITION_TYPES_TABLE, columns);

        columns.clear();

        columns.put(SLED_NAME, "TEXT NOT NULL");
        columns.put(SLED_MILEAGE, "DOUBLE");
        columns.put(SLED_NOTES, "TEXT");
        columns.put(SLED_MAINTENANCE_LOG_ID, "INT");
        createTable(db, SLEDS_TABLE, columns);

        columns.clear();

        columns.put(MAINTENANCE_LOG_NAME, "TEXT");
        columns.put(MAINTENANCE_LOG_NOTES, "TEXT");
        createTable(db, MAINTENANCE_LOGS_TABLE, columns);

        columns.clear();

        columns.put(MAINTENANCE_ENTRY_NOTES, "TEXT");
        columns.put(MAINTENANCE_ENTRY_TYPE_ID, "INT");
        columns.put(MAINTENANCE_ENTRY_LOG_ID, "INT");
        createTable(db, MAINTENANCE_ENTRIES_TABLE, columns);

        columns.clear();

        columns.put(MAINTENANCE_TYPE_NAME, "TEXT NOT NULL");
        createTable(db, MAINTENANCE_TYPES_TABLE, columns);

        columns.clear();

        columns.put(FRIEND_USER_NAME, "TEXT NOT NULL");
        columns.put(FRIEND_FINDER_ACTIVE, "BOOLEAN NOT NULL");
        createTable(db, FRIENDS_TABLE, columns);

        columns.clear();

        // Insert predefined types into type tables
        insertConditionTypes(db);
        insertMaintenanceTypes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: For upgrade, drop tables and recreate database
        onCreate(db);
    }

    private void insertConditionTypes(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(CREATED_AT, System.currentTimeMillis());
        cv.put(UPDATED_AT, System.currentTimeMillis());

        cv.put(CONDITION_TYPE_NAME, ConditionTypes.GRASSY);
        db.insert(CONDITION_TYPES_TABLE, CONDITION_TYPE_NAME, cv);

        cv.put(CONDITION_TYPE_NAME, ConditionTypes.ICE);
        db.insert(CONDITION_TYPES_TABLE, CONDITION_TYPE_NAME, cv);

        cv.put(CONDITION_TYPE_NAME, ConditionTypes.ROUGH);
        db.insert(CONDITION_TYPES_TABLE, CONDITION_TYPE_NAME, cv);

        cv.put(CONDITION_TYPE_NAME, ConditionTypes.SMOOTH);
        db.insert(CONDITION_TYPES_TABLE, CONDITION_TYPE_NAME, cv);
    }

    private void insertMaintenanceTypes(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(CREATED_AT, System.currentTimeMillis());
        cv.put(UPDATED_AT, System.currentTimeMillis());

        cv.put(MAINTENANCE_TYPE_NAME, MaintenanceTypes.OIL_CHANGE);
        db.insert(MAINTENANCE_TYPES_TABLE, MAINTENANCE_TYPE_NAME, cv);

        cv.put(MAINTENANCE_TYPE_NAME, MaintenanceTypes.PRE_SEASON_CHECKUP);
        db.insert(MAINTENANCE_TYPES_TABLE, MAINTENANCE_TYPE_NAME, cv);
    }

    private void createTable(SQLiteDatabase db, String tableName, HashMap<String, String> columns) {
        // Create table
        String createStatement = "CREATE TABLE " + tableName + " ";
        // Add ID timestamp columns
        createStatement += "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CREATED_AT + " INT NOT NULL , " + UPDATED_AT + " INT NOT NULL, ";

        // Add the rest of the colums
        for (HashMap.Entry<String, String> column : columns.entrySet()) {
            String columnName = column.getKey();
            String columnType = column.getValue();

            createStatement += columnName + " " + columnType + ", ";
        }

        // Strip off the trailing comma created in the for loop
        createStatement = createStatement.substring(0, createStatement.length() - 2);
        // Close the create table statement
        createStatement += ");";

        // Execute the create statement
        db.execSQL(createStatement);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
