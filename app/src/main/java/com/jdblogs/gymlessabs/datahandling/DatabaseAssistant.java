package com.jdblogs.gymlessabs.datahandling;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jonathandeehan on 12/06/2017.
 */

public class DatabaseAssistant extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "exercises";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + LocalData.EXERCISE_TABLE + " ("
                    + LocalData.EXERCISE_ID + " INTEGER PRIMARY KEY, "
                    + LocalData.EXERCISE_NAME + " TEXT NOT NULL, "
                    + LocalData.EXERCISE_EXPERIENCE_LEVEL + " INTEGER, "
                    + LocalData.EXERCISE_DURATION + " INTEGER, "
                    + LocalData.EXERCISE_EQUIPMENT + " TEXT NOT NULL, "
                    + LocalData.EXERCISE_VIDEO_FILE_NAME + " TEXT NOT NULL);";

    public DatabaseAssistant(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + LocalData.EXERCISE_TABLE);
        onCreate(database);
    }
}
