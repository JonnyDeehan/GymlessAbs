/*
 * Created by Jonny Deehan on 12/06/2017.
 * Copyright (c) 2017. All rights reserved.
 */

package com.jdblogs.gymlessabs.datahandling.sqldatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseAssistant extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GymlessAbs";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String EXERCISE_DATABASE_CREATE =
            "CREATE TABLE " + ExerciseLocalData.EXERCISE_TABLE + " ("
                    + ExerciseLocalData.EXERCISE_ID + " INTEGER PRIMARY KEY, "
                    + ExerciseLocalData.EXERCISE_NAME + " TEXT NOT NULL, "
                    + ExerciseLocalData.EXERCISE_EXPERIENCE_LEVEL + " INTEGER, "
                    + ExerciseLocalData.EXERCISE_DURATION + " INTEGER, "
                    + ExerciseLocalData.EXERCISE_EQUIPMENT + " TEXT NOT NULL, "
                    + ExerciseLocalData.EXERCISE_VIDEO_FILE_NAME + " TEXT NOT NULL);";

    private static final String FAVOURITES_DATABASE_CREATE =
            "CREATE TABLE " + FavouritesLocalData.FAVOURITES_TABLE + " ("
                    + FavouritesLocalData.EXERCISE_ID + " INTEGER PRIMARY KEY, "
                    + FavouritesLocalData.EXERCISE_NAME + " TEXT NOT NULL, "
                    + FavouritesLocalData.EXERCISE_EXPERIENCE_LEVEL + " INTEGER, "
                    + FavouritesLocalData.EXERCISE_DURATION + " INTEGER, "
                    + FavouritesLocalData.EXERCISE_EQUIPMENT + " TEXT NOT NULL, "
                    + FavouritesLocalData.EXERCISE_VIDEO_FILE_NAME + " TEXT NOT NULL, "
                    + FavouritesLocalData.WORKOUT_ID + " INTEGER);";

    public DatabaseAssistant(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(EXERCISE_DATABASE_CREATE);
        database.execSQL(FAVOURITES_DATABASE_CREATE);

        logMessage("Created Database");
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + ExerciseLocalData.EXERCISE_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + FavouritesLocalData.FAVOURITES_TABLE);
        onCreate(database);
    }

    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }
}
