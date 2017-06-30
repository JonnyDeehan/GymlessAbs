package com.jdblogs.gymlessabs.activities.workout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.datahandling.WorkoutGenerator;
import com.jdblogs.gymlessabs.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    private List<Exercise> exerciseList = new ArrayList<Exercise>();
    private GlobalVariables appContext;
    private WorkoutGenerator workoutGenerator;
    private String currentWeek;
    private String currentDay;
    private TextView currentDateText;
    private TextView totalWorkoutDurationTextView;
    private int totalWorkoutDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        appContext = (GlobalVariables) getApplicationContext();

        generateExerciseData();
        setupUIComponents();
    }

    private void generateExerciseData(){
        currentWeek = appContext.getWeekSelected();
        currentDay = appContext.getDaySelected();
        workoutGenerator = new WorkoutGenerator(currentWeek,currentDay);
        String exerciseData = getResources().getString(R.string.exercise_list);
        exerciseList = workoutGenerator.generateWorkout(exerciseData);
    }

    private void setupUIComponents(){
        currentDateText = (TextView) findViewById(R.id.currentDateText);
        currentDateText.setText(currentWeek + "\n" + currentDay);

        // Exercises List
        ListView listView = (ListView) findViewById(R.id.exerciseList);
        CustomAdapter customAdapter = new CustomAdapter(this,exerciseList);
        listView.setAdapter(customAdapter);

        for(Exercise exercise: exerciseList){
            totalWorkoutDuration+=exercise.getDuration();
        }
        totalWorkoutDurationTextView = (TextView) findViewById(R.id.workoutDuration);
        totalWorkoutDurationTextView.setText("Workout Duration: " + (double)(totalWorkoutDuration/60) + " min");
    }

    public void onStartWorkoutClick(View view){
        appContext.setCurrentTotalWorkoutDuration(totalWorkoutDuration);
        appContext.setCurrentWorkout(exerciseList);

        Intent intent = new Intent(WorkoutActivity.this, StartWorkoutActivity.class);
        moveToActivity(intent);
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

    private void moveToActivity(android.content.Intent intent){
        if(intent != null) {
            startActivity(intent);
        }
    }

    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }
}


