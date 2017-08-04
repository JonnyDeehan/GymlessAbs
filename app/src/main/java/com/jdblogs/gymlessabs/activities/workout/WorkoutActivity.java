package com.jdblogs.gymlessabs.activities.workout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.datahandling.WorkoutGenerator;
import com.jdblogs.gymlessabs.datahandling.sqldatabase.FavouritesLocalData;
import com.jdblogs.gymlessabs.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    private List<Exercise> exerciseList = new ArrayList<Exercise>();
    private List<List<Exercise>> workoutList = new ArrayList<List<Exercise>>();
    private GlobalVariables appContext;
    private FavouritesLocalData favouritesLocalData;
    private WorkoutGenerator workoutGenerator;
    private String currentWeek;
    private String currentDay;
    private TextView currentDateText;
    private TextView totalWorkoutDurationTextView;
    private ImageButton favouriteButton;
    private int totalWorkoutDuration;

    private static final int NORMAL_WORKOUT_ACTIVITY_TYPE = 0;
    private static final int SHUFFLE_WORKOUT_ACTIVITY_TYPE = 1;
    private static final int FAVOURITE_WORKOUT_ACTIVITY_TYPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        appContext = GlobalVariables.getInstance();

        generateExerciseData();
        setupUIComponents();
    }

    private void generateExerciseData(){
        int workoutActivityType = appContext.getWorkoutActivityType();
        int equipmentAvailable = appContext.getEquipmentAvailable();
        if(workoutActivityType == NORMAL_WORKOUT_ACTIVITY_TYPE) {
            currentWeek = appContext.getWeekSelected();
            currentDay = appContext.getDaySelected();
            workoutGenerator = new WorkoutGenerator(currentWeek,currentDay,equipmentAvailable);
            String exerciseData = getResources().getString(R.string.exercise_list);
            exerciseList = workoutGenerator.generateWorkout(exerciseData);
        } else if(workoutActivityType == FAVOURITE_WORKOUT_ACTIVITY_TYPE){
            currentDay = "";
            currentWeek = "";
            exerciseList = appContext.getCurrentWorkout();
        } else if(workoutActivityType == SHUFFLE_WORKOUT_ACTIVITY_TYPE){
            workoutGenerator = new WorkoutGenerator(equipmentAvailable);
            String exerciseData = getResources().getString(R.string.exercise_list);
            exerciseList = workoutGenerator.createRandomWorkout(exerciseData);
            currentDay = "";
            currentWeek = "";
        }
    }

    private void setupUIComponents(){
        currentDateText = (TextView) findViewById(R.id.currentDateText);
        currentDateText.setText(currentWeek + " " + currentDay);
        favouriteButton = (ImageButton) findViewById(R.id.favouriteWorkoutButton);
        if(appContext.getWorkoutActivityType() == FAVOURITE_WORKOUT_ACTIVITY_TYPE){
            favouriteButton.setVisibility(View.INVISIBLE);
        }

        // Exercises List
        ListView listView = (ListView) findViewById(R.id.exerciseList);
        CustomAdapter customAdapter = new CustomAdapter(this,exerciseList);
        listView.setAdapter(customAdapter);

        for(Exercise exercise: exerciseList){
            totalWorkoutDuration+=exercise.getDuration();
        }
        totalWorkoutDurationTextView = (TextView) findViewById(R.id.workoutDuration);
        totalWorkoutDurationTextView.setText("Workout Duration: " + (double)(totalWorkoutDuration/60) + " min");

        // Set color and rounded edges for button
        Button startWorkoutButton = (Button) findViewById(R.id.startWorkoutButton);
        startWorkoutButton.setBackgroundResource(R.drawable.customshape);
    }

    public void onStartWorkoutClick(View view){
        appContext.setCurrentTotalWorkoutDuration(totalWorkoutDuration);
        appContext.setCurrentWorkout(exerciseList);

        Intent intent = new Intent(WorkoutActivity.this, StartWorkoutActivity.class);
        moveToActivity(intent);
    }

    public void onFavouriteWorkout(View view){
        if(appContext.getWorkoutActivityType()!=FAVOURITE_WORKOUT_ACTIVITY_TYPE) {

            int workoutId;
            int exerciseId;
            favouritesLocalData = new FavouritesLocalData(this);

            favouritesLocalData.listAllExercises();

            int count = (int) favouritesLocalData.numberOfEntries();

            if (count == 0) {
                workoutId = 1;
                exerciseId = 1;
            } else {
                workoutId = (int) (favouritesLocalData.numberOfEntries() / 7) + 1;
                exerciseId = count + 1;
            }

            for (Exercise exercise : exerciseList) {
                favouritesLocalData.createRecord(exerciseId, exercise.getName(), exercise.getExperienceLevel(),
                        exercise.getDuration(), exercise.getEquipment(),
                        exercise.getVideoFileName(), workoutId);
                exerciseId++;
            }

            favouritesLocalData.listAllExercises();

            // Added Favourite Pop Up
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Added Workout to Favourites")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            builder.show();
        }
    }

    private class CustomAdapter extends ArrayAdapter<Exercise>{

        CustomAdapter(Context context,List<Exercise> exercises){
            super(context,R.layout.custom_exercise_layout,exercises);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.custom_exercise_layout,parent,false);

            final Exercise exercise = getItem(position);
            TextView workoutInfoTextView = (TextView) customView.findViewById(R.id.exerciseInfo);

            try{
                String equipment=exercise.getEquipment();
                workoutInfoTextView.setText(exercise.getName() + "\n" + exercise.getDuration() + " s" + "\n" + "Equipment: " + equipment);
            } catch(Error e){
                logMessage(e.toString());
            }

            ImageButton videoButton = (ImageButton) customView.findViewById(R.id.playVideoButton);
            // Set color and rounded edges
            videoButton.setBackgroundResource(R.drawable.customshape);
            videoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appContext.setCurrentExerciseSelected(exercise);
                    appContext.setCurrentWorkout(exerciseList);

                    Intent intent = new Intent(WorkoutActivity.this, VideoPlayerActivity.class);
                    moveToActivity(intent);
                }
            });

            return customView;
        }
    }

    public void onBackButton(View view){
        finish();
    }

    private void moveToActivity(android.content.Intent intent){
        if(intent != null) {
            startActivity(intent);
        }
    }

    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }
}