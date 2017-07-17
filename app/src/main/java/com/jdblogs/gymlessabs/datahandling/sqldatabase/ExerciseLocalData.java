package com.jdblogs.gymlessabs.datahandling.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jdblogs.gymlessabs.models.Exercise;

import java.util.List;

/**
 * Created by jonathandeehan on 12/06/2017.
 */

public class ExerciseLocalData {
    private DatabaseAssistant dbHelper;

    private SQLiteDatabase database;

    public final static String EXERCISE_TABLE = "exercises"; // name of table
    public final static String EXERCISE_ID = "exerciseId";
    public final static String EXERCISE_NAME = "exerciseName";
    public final static String EXERCISE_EXPERIENCE_LEVEL = "exerciseLevel";
    public final static String EXERCISE_DURATION = "exerciseDuration";
    public final static String EXERCISE_EQUIPMENT = "exerciseEquipment";
    public final static String EXERCISE_VIDEO_FILE_NAME = "exerciseVideoFileName";


    public ExerciseLocalData(Context context) {
        dbHelper = new DatabaseAssistant(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecord(int id, String name, int level, int duration,
                             String equipment, String videoFileName) {
        ContentValues values = new ContentValues();
        values.put(EXERCISE_ID, id);
        values.put(EXERCISE_NAME, name);
        values.put(EXERCISE_EXPERIENCE_LEVEL, level);
        values.put(EXERCISE_DURATION,duration);
        values.put(EXERCISE_EQUIPMENT,equipment);
        values.put(EXERCISE_VIDEO_FILE_NAME,videoFileName);

        return database.insert(EXERCISE_TABLE, null, values);
    }

    public Cursor selectRecord(int exerciseId) {
        String[] cols = new String[]{EXERCISE_ID, EXERCISE_NAME,
                EXERCISE_EXPERIENCE_LEVEL, EXERCISE_DURATION,
                EXERCISE_EQUIPMENT, EXERCISE_VIDEO_FILE_NAME};
        return database.query(true, EXERCISE_TABLE, cols,
                ExerciseLocalData.EXERCISE_ID + "=" + exerciseId,
                null, null, null, null, null);
    }

    public Cursor searchExercises(String stringQuery){
        String[] cols = new String[]{EXERCISE_ID, EXERCISE_NAME,
                EXERCISE_EXPERIENCE_LEVEL, EXERCISE_DURATION,
                EXERCISE_EQUIPMENT, EXERCISE_VIDEO_FILE_NAME};
        Cursor searchCursor = database.query(true, EXERCISE_TABLE, cols, EXERCISE_NAME + " LIKE ?",
                new String[] {"%"+ stringQuery+ "%" }, null, null, null,
                null);
        if(searchCursor!=null){
            searchCursor.moveToFirst();
        }
        return searchCursor;
    }

    public Cursor fetchExerciseByName(String inputText) throws SQLException {
        Log.w(getClass().getSimpleName(), inputText);
        Cursor mCursor = null;
        String[] cols = new String[]{EXERCISE_ID, EXERCISE_NAME,
                EXERCISE_EXPERIENCE_LEVEL, EXERCISE_DURATION,
                EXERCISE_EQUIPMENT, EXERCISE_VIDEO_FILE_NAME};
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = database.query(EXERCISE_TABLE, cols,
                    null, null, null, null, null);

        }
        else {
            mCursor = database.query(true, EXERCISE_TABLE, cols,
                    EXERCISE_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public void clearExerciseTableEntries(){
        database.execSQL("delete from "+ EXERCISE_TABLE);
    }

    public void closeDatabase(){
        this.database.close();
    }

    public void initData(List<Exercise> exercises) {
        long count = DatabaseUtils.queryNumEntries(database, EXERCISE_TABLE);
        if (count == 0) {
            Log.i("ExerciseLocalData", "Initializing data");
            for (Exercise exercise: exercises) {

                createRecord(exercise.getExerciseId(), exercise.getName(),exercise.getExperienceLevel(),
                        exercise.getDuration(),exercise.getEquipment(),
                        exercise.getVideoFileName());
            }
        } else {
            Log.i("ExerciseLocalData", "Data already initialized with " + count + " rows");
        }
    }

    public void listAllExercises(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + EXERCISE_TABLE, null);
        while(cursor.moveToNext()){
            Log.i(getClass().getSimpleName(), cursor.getString(1) + " :: " + cursor.getString(2)
                    + " :: " + cursor.getString(3)
                    + " :: " + cursor.getString(4)
                    + " :: " + cursor.getString(5));
        }
    }

    public void getAllExercises(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + EXERCISE_TABLE, null);
        while(cursor.moveToNext()){

        }
    }
}