package com.jcoapps.snowmobile_trail_maps.schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by jcolon on 7/30/2016.
 */
public class snowmobileTrailDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "snowmobile-trail-maps";
    private static final int DATABASE_VERSION = 1;

    // Columns that belong in every table
    private static final String ID = "id"; // int
    private static final String CREATED_AT = "created_at"; // timestamp
    private static final String UPDATED_AT = "updated_at"; // timestamp

    // Users table
    // Stores user login information
    private static final String USERS_TABLE = "users";
    private static final String USER_EMAIL = "email"; // string
    private static final String USER_NAME = "name"; // string
    private static final String USER_PASSWORD = "password"; // string

    // TrailJournal table
    // Stores statistics for riding sessions
    private static final String TRAIL_JOURNALS_TABLE = "trail-journals";
    private static final String TRAIL_JOURNAL_ENTRY_NAME = "entry-name"; // string
    private static final String TRAIL_JOURNAL_MILES = "miles"; // decimal
    private static final String TRAIL_JOURNAL_MAX_SPEED = "max-speed"; // decimal
    private static final String TRAIL_JOURNAL_MIN_SPEED = "min-speed"; // decimal
    private static final String TRAIL_JOURNAL_AVG_SPEED = "avg-speed"; // decimal
    private static final String TRAIL_JOURNAL_CONDITION_ID = "condition-id"; // int

    // ConditionType table
    // Stores codes for trail conditions (smooth, grassy, ice, etc.)
    private static final String CONDITION_TYPES_TABLE = "condition-types";
    private static final String CONDITION_TYPE_NAME = "name"; // string

    // Sleds table
    // Stores information about snowmobiles
    private static final String SLEDS_TABLE = "sleds";
    private static final String SLED_NAME = "name"; // string
    private static final String SLED_MILEAGE = "mileage"; // decimal
    private static final String SLED_NOTES = "notes"; // string
    private static final String SLED_MAINTENANCE_LOG_ID = "maintenance-log-id"; // int

    // MaintenanceLog table
    // Stores maintenance data for a sled
    private static final String MAINTENANCE_LOGS_TABLE = "maintenance-logs";
    private static final String MAINTENANCE_LOG_TYPE = "maintenance-type-id"; // int
    private static final String MAINTENANCE_LOG_NOTES = "notes"; // string

    // MaintenanceType table
    // Stores codes for maintenance types (oil change, belt change, etc.)
    private static final String MAINTENANCE_TYPES_TABLE = "maintenance-types";
    private static final String MAINTENANCE_TYPE_NAME = "name"; // string

    // Friends table
    // Stores users who are friends of the user
    private static final String FRIENDS_TABLE = "friends";
    private static final String FRIEND_USER_NAME = "name"; // string
    private static final String FRIEND_FINDER_ACTIVE = "active"; // boolean

    public snowmobileTrailDatabaseHelper(Context context) {
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

        columns.put(TRAIL_JOURNAL_ENTRY_NAME, "TEXT NOT NULL DEFAULT '<unnamed entry>'");
        columns.put(TRAIL_JOURNAL_MILES, "DOUBLE");
        columns.put(TRAIL_JOURNAL_MAX_SPEED, "DOUBLE");
        columns.put(TRAIL_JOURNAL_MIN_SPEED, "DOUBLE");
        columns.put(TRAIL_JOURNAL_AVG_SPEED, "DOUBLE");
        columns.put(TRAIL_JOURNAL_CONDITION_ID, "INT");
        createTable(db, TRAIL_JOURNALS_TABLE, columns);

        columns.clear();

        // TODO: finish DB creation
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTable(SQLiteDatabase db, String tableName, HashMap<String, String> columns) {
        // Create table
        String createStatement = "CREATE TABLE " + tableName + " ";
        // Add ID timestamp columns
        createStatement += "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                          CREATED_AT + " TEXT NOT NULL, " + UPDATED_AT + " TEXT NOT NULL, ";

        // Add the rest of the colums
        for (HashMap.Entry<String, String> column : columns.entrySet()) {
            String columnName = column.getKey();
            String columnType = column.getValue();

            createStatement += columnName + " " + columnType + ", ";
        }

        // Strip off the trailing comma created in the for loop
        createStatement = createStatement.substring(0, createStatement.length() - 1);
        // Close the create table statement
        createStatement += ");";

        // Execute the create statement
        db.execSQL(createStatement);
    }
}