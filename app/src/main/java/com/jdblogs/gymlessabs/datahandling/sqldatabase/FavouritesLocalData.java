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

    public final static String FAVOURITES_TABLE = "FAVOURITESTABLE"; // name of table
    public final static String EXERCISE_ID = "exerciseId";
    public final static String EXERCISE_NAME = "exerciseName";
    public final static String EXERCISE_EXPERIENCE_LEVEL = "exerciseLevel";
    public final static String EXERCISE_DURATION = "exerciseDuration";
    public final static String EXERCISE_EQUIPMENT = "exerciseEquipment";
    public final static String EXERCISE_VIDEO_FILE_NAME = "exerciseVideoFileName";
    public final static String WORKOUT_ID = "workoutId";

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

    public void initData(List<Exercise> exercises, int workoutId) {
        long count = DatabaseUtils.queryNumEntries(database, FAVOURITES_TABLE);
        if (count == 0) {
            Log.i("FavouritesLocalData", "Initializing data");
            for (Exercise exercise: exercises) {

                createRecord((int)count++, exercise.getName(),exercise.getExperienceLevel(),
                        exercise.getDuration(),exercise.getEquipment(),
                        exercise.getVideoFileName(),workoutId);
            }
        } else {
            Log.i("FavouritesLocalData", "Data already initialized with " + count + " rows");
        }
    }

    public void listAllExercises(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + FAVOURITES_TABLE, null);
        while(cursor.moveToNext()){
            Log.i(getClass().getSimpleName(), cursor.getString(1)
                    + " :: " + cursor.getString(2)
                    + " :: " + cursor.getString(3)
                    + " :: " + cursor.getString(4)
                    + " :: " + cursor.getString(5)
                    + " :: " + cursor.getString(6));
        }
    }

    public void deleteFavourite(String workout){
//        database.delete("FAVOURITES", "WORKOUT='" + workout + "'",null);
    }


    public List<List<Exercise>> getAllFavourites(){
        List<List<Exercise>> workoutList = new ArrayList<List<Exercise>>();
        workoutList.clear();
        int currentWorkout=1;
        int totalNumberOfWorkouts = (int)numberOfEntries();
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

            if(currentWorkout != workoutId){ // cursor at this point is the problem
                currentWorkout++;
                workoutList.add(exercises);
                exercises.clear();
            }
            exercises.add(new Exercise(id,name,experienceLevel,duration,equipment,videoFileName));

            System.out.println("Exercise: " + name);

        }
        return workoutList;
    }
}