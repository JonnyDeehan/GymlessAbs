package com.jdblogs.gymlessabs.datahandling.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.jdblogs.gymlessabs.models.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathandeehan on 12/06/2017.
 */

public class FavouritesLocalData {

    private DatabaseAssistant dbHelper;
    private SQLiteDatabase database;
    private List<List<Exercise>> workoutList = new ArrayList<List<Exercise>>();

    public final static String FAVOURITES_TABLE = "FAVOURITESTABLE"; // name of table
    public final static String EXERCISE_ID = "exerciseId";
    public final static String EXERCISE_NAME = "exerciseName";
    public final static String EXERCISE_EXPERIENCE_LEVEL = "exerciseLevel";
    public final static String EXERCISE_DURATION = "exerciseDuration";
    public final static String EXERCISE_EQUIPMENT = "exerciseEquipment";
    public final static String EXERCISE_VIDEO_FILE_NAME = "exerciseVideoFileName";
    public final static String WORKOUT_ID = "workoutId";
    private final static int NUMBER_OF_EXERCISES_IN_WORKOUT = 7;

    public FavouritesLocalData(Context context) {
        dbHelper = new DatabaseAssistant(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecord(int exerciseId,String name, int level, int duration,
                             String equipment, String videoFileName,int workoutId) {
        ContentValues values = new ContentValues();
        values.put(EXERCISE_ID,exerciseId);
        values.put(EXERCISE_NAME, name);
        values.put(EXERCISE_EXPERIENCE_LEVEL, level);
        values.put(EXERCISE_DURATION,duration);
        values.put(EXERCISE_EQUIPMENT,equipment);
        values.put(EXERCISE_VIDEO_FILE_NAME,videoFileName);
        values.put(WORKOUT_ID,workoutId);

        return database.insert(FAVOURITES_TABLE, null, values);
    }

    public Cursor selectRecord(int workoutId) {
        String[] cols = new String[]{EXERCISE_ID, EXERCISE_NAME,
                EXERCISE_EXPERIENCE_LEVEL, EXERCISE_DURATION,
                EXERCISE_EQUIPMENT, EXERCISE_VIDEO_FILE_NAME,WORKOUT_ID};
        return database.query(true, FAVOURITES_TABLE, cols,
                WORKOUT_ID + "=" + workoutId,
                null, null, null, null, null);
    }

    public void closeDatabase(){
        this.database.close();
    }

    public long numberOfEntries(){
        return DatabaseUtils.queryNumEntries(database, FAVOURITES_TABLE);
    }

    public void listAllExercises(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + FAVOURITES_TABLE, null);
        while(cursor.moveToNext()){
            Log.i(getClass().getSimpleName(), cursor.getString(0)
                    + "\n" + cursor.getString(1)
                    + "\n" + cursor.getString(2)
                    + "\n" + cursor.getString(3)
                    + "\n" + cursor.getString(4)
                    + "\n" + cursor.getString(5)
                    + "\n" + cursor.getString(6));
        }
    }

    public void clearFavouritesTableEntries(){
        database.execSQL("delete from "+ FAVOURITES_TABLE);
    }


    public List<List<Exercise>> getAllFavourites(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + FAVOURITES_TABLE, null);
        List<Exercise> exercises = new ArrayList<Exercise>();
        while(cursor.moveToNext()){

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int experienceLevel = cursor.getInt(2);
            int duration = cursor.getInt(3);
            String equipment = cursor.getString(4);
            String videoFileName = cursor.getString(5);
            int workoutId = cursor.getInt(6);

            exercises.add(new Exercise(id,name,experienceLevel,duration,equipment,videoFileName));

            if(exercises.size() == NUMBER_OF_EXERCISES_IN_WORKOUT){
                addWorkoutToWorkoutList(exercises);
                exercises.clear();
            }

        }

        return workoutList;
    }

    // ensure we pass by final value after original reference is cleared
    private void addWorkoutToWorkoutList(final List<Exercise> workout){
        List<Exercise> workoutCopy = new ArrayList(workout);
        workoutList.add(workoutCopy);
    }
}