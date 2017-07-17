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

        Log.i(getClass().getSimpleName(), "createRecord: " + exerciseId + " : " + name);

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

    public void deleteFavourite(String workout){
//        database.delete("FAVOURITES", "WORKOUT='" + workout + "'",null);
    }

    public void clearFavouritesTableEntries(){
        database.execSQL("delete from "+ FAVOURITES_TABLE);
    }


    public List<List<Exercise>> getAllFavourites(){
        List<List<Exercise>> workoutList = new ArrayList<List<Exercise>>();
        int currentWorkout=1;
        Cursor cursor = database.rawQuery("SELECT * FROM " + FAVOURITES_TABLE, null);
        List<Exercise> exercises = new ArrayList<Exercise>();
        Log.i(getClass().getSimpleName(), "Proceed to getAllFavourites: ");
        while(cursor.moveToNext()){

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int experienceLevel = cursor.getInt(2);
            int duration = cursor.getInt(3);
            String equipment = cursor.getString(4);
            String videoFileName = cursor.getString(5);
            int workoutId = cursor.getInt(6);

            Log.i(getClass().getSimpleName(), "==========================================================");
            Log.i(getClass().getSimpleName(), "Exercise id: " + id + "\n"
            + "Name: " + name + "\n"
            + "Experience Level: " + experienceLevel + "\n"
            + "Duration: " + duration + "\n"
            + "Equipment: " + equipment + "\n"
            + "Video file name: " + videoFileName + "\n"
            + "WorkoutId: " + workoutId);

//            if(currentWorkout != workoutId){
//                Log.i(getClass().getSimpleName(), "Identified next workoutId, " +
//                        " adding exercises to workoutList and clearing exercises for next workout set ");
//                currentWorkout++;
//                workoutList.add(exercises);
//                exercises.clear();
//            }
            exercises.add(new Exercise(id,name,experienceLevel,duration,equipment,videoFileName));

            // If exercises equals number of exercises in workout
            // or make a check for the exercise id
            if(exercises.size() == 7){
                Log.i(getClass().getSimpleName(), "WorkoutList Size before adding exercise list: "
                        + workoutList.size());
                workoutList.add(exercises);
                Log.i(getClass().getSimpleName(), "WorkoutList Size after adding exercise list: "
                        + workoutList.size());
                exercises.clear();
                Log.i(getClass().getSimpleName(), "WorkoutList Size after clearing exercise list: "
                        + workoutList.size());
            }

        }
        Log.i(getClass().getSimpleName(), "Exercises size before returning: " + exercises.size());
        Log.i(getClass().getSimpleName(), "WorkoutList Size before returning: " + workoutList.size());

        return workoutList;
    }
}