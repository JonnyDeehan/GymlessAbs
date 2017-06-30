package com.jdblogs.gymlessabs.activities.workout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.jdblogs.gymlessabs.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class ShuffleWorkoutActivity extends AppCompatActivity {

    private WorkoutGenerator workoutGenerator;
    private List<Exercise> exerciseList = new ArrayList<Exercise>();
    private GlobalVariables appContext;
    private TextView workoutDurationTextView;
    private Button startWorkoutButton;
    private int totalWorkoutDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_workout);
        appContext = (GlobalVariables) getApplicationContext();
        generateExerciseData();

        // Exercises List
        ListView listView = (ListView) findViewById(R.id.shuffleExerciseList);
        ShuffleCustomAdapter customAdapter = new ShuffleCustomAdapter(this,exerciseList);
        listView.setAdapter(customAdapter);

        for(Exercise exercise: exerciseList){
            totalWorkoutDuration+=exercise.getDuration();
        }

        workoutDurationTextView = (TextView) findViewById(R.id.workoutDurationTextView);
        workoutDurationTextView.setText("Workout Duration: " + (double)(totalWorkoutDuration/60) + " min");
    }

    private void generateExerciseData(){
        workoutGenerator = new WorkoutGenerator();
        String exerciseData = getResources().getString(R.string.exercise_list);
        exerciseList = workoutGenerator.createRandomWorkout(exerciseData);
        for(Exercise exercise: exerciseList)
            logMessage("Shuffle workout video files: " + exercise.getVideoFileName());
    }

    public void startWorkout(View view){
        appContext.setShuffleWorkout(true);
        appContext.setCurrentWorkout(exerciseList);
        // Move to start workout activity
        Intent intent = new Intent(ShuffleWorkoutActivity.this, StartWorkoutActivity.class);
        moveToActivity(intent);
    }

    private class ShuffleCustomAdapter extends ArrayAdapter<Exercise> {

        ShuffleCustomAdapter(Context context, List<Exercise> exercises){
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
                    logMessage("Exercise selected: " + exercise.getName());
                    appContext.setCurrentExerciseSelected(exercise);

                    Intent intent = new Intent(ShuffleWorkoutActivity.this, VideoPlayerActivity.class);
                    moveToActivity(intent);
                }
            });

            return customView;
        }
    }

    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }

    private void moveToActivity(android.content.Intent intent){
        if(intent != null) {
            startActivity(intent);
        }
    }
}