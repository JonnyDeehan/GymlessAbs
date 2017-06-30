package com.jdblogs.gymlessabs.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jdblogs.gymlessabs.activities.meal.SelectMealDateActivity;
import com.jdblogs.gymlessabs.activities.workout.ExercisePlanActivity;
import com.jdblogs.gymlessabs.activities.workout.FavouritesActivity;
import com.jdblogs.gymlessabs.activities.workout.SearchActivity;
import com.jdblogs.gymlessabs.activities.workout.ShuffleWorkoutActivity;
import com.jdblogs.gymlessabs.activities.workout.WorkoutActivity;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.datahandling.LocalData;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.WorkoutGenerator;
import com.jdblogs.gymlessabs.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LocalData localData;
    private WorkoutGenerator workoutGenerator;
    private GlobalVariables appContext;
    private List<Exercise> exerciseList = new ArrayList<Exercise>();
    private String [] homeItemsList = new String[]{"Exercise Plan", "Meal Plan", "Shuffle Workout",
            "Favourites", "Progress Pictures"};
    private TextView workoutWeek;
    private TextView workoutDay;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appContext = (GlobalVariables) getApplicationContext();

        workoutWeek = (TextView) findViewById(R.id.weekTextView);
        workoutDay = (TextView) findViewById(R.id.dayTextView);
        workoutWeek.setText(appContext.getWeekSelected());
        workoutDay.setText(appContext.getDaySelected());

        ListView listView = (ListView) findViewById(R.id.homeItemsList);
        CustomAdapter customAdapter = new CustomAdapter(this,homeItemsList);
        listView.setAdapter(customAdapter);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "modernesans.ttf");
        titleTextView= (TextView) findViewById(R.id.titleText);
        titleTextView.setTypeface(tf);

        createDatabase();
    }

    private void createDatabase(){
        String exerciseData = getResources().getString(R.string.exercise_list);
        workoutGenerator = new WorkoutGenerator();
        exerciseList = workoutGenerator.generateListOfAllExercises(exerciseData);

        localData = new LocalData(this);
        localData.initData(exerciseList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localData.closeDatabase();
    }

    // example reading data from local sqlite database
    public void searchDatabase(int indexValue ) {

        Cursor cursor = localData.selectRecord(indexValue);
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(LocalData.EXERCISE_NAME));
            int experienceLevel = cursor.getInt(cursor.getColumnIndex(LocalData.EXERCISE_EXPERIENCE_LEVEL));
            int duration = cursor.getInt(cursor.getColumnIndex(LocalData.EXERCISE_DURATION));
            String equipment = cursor.getString(cursor.getColumnIndex(LocalData.EXERCISE_EQUIPMENT));
            String videoFileName = cursor.getString(cursor.getColumnIndex(LocalData.EXERCISE_VIDEO_FILE_NAME));
            logMessage("Exercise " + indexValue);
            logMessage("Name: " + name);
            logMessage("Experience Level: " + experienceLevel);
            logMessage("Duration: " + duration);
            logMessage("Equipment: " + equipment);
            logMessage("VideoFileName: " + videoFileName);

        } else {
            Log.i(getClass().getSimpleName(), "Exercise not found");
        }

    }

    // == onClick Methods =========================================================================


    public void onSettingsClick(View view){
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        moveToActivity(intent);
    }

    public void onSearchClick(View view){
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        moveToActivity(intent);
    }

    public void onCurrentWorkoutClick(View view){
        Intent intent = new Intent(HomeActivity.this, WorkoutActivity.class);
        moveToActivity(intent);
    }

    private void moveToActivity(android.content.Intent intent){
        if(intent != null) {
            startActivity(intent);
        }
    }

    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }

    class CustomAdapter extends ArrayAdapter<String> {

        CustomAdapter(Context context, String[] homeItems){
            super(context,R.layout.custom_home_items_layout,homeItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            final View customView = inflater.inflate(R.layout.custom_home_items_layout,parent,false);

            final String homeItem = getItem(position);
            TextView homeItemTextView = (TextView) customView.findViewById(R.id.itemTextView);

            try{
                homeItemTextView.setText(homeItem);
            } catch(Error e){
                logMessage(e.toString());
            }

            ImageView iconImage = (ImageView) customView.findViewById(R.id.itemImageView);

            if(homeItem.equals("Exercise Plan")){
                iconImage.setImageResource(R.drawable.ic_exercise);
            } else if(homeItem.equals("Meal Plan")) {
                iconImage.setImageResource(R.drawable.ic_meal);
            } else if(homeItem.equals("Shuffle Workout")) {
                iconImage.setImageResource(R.drawable.ic_shuffle);
            } else if(homeItem.equals("Favourites")) {
                iconImage.setImageResource(R.drawable.ic_favourite);
            } else if(homeItem.equals("Progress Pictures")) {
                iconImage.setImageResource(R.drawable.ic_camera);
            }

            Button homeItemButton = (Button) customView.findViewById(R.id.itemButton);
            homeItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(homeItem.equals("Exercise Plan")){
                        Intent intent = new Intent(HomeActivity.this, ExercisePlanActivity.class);
                        moveToActivity(intent);
                    } else if(homeItem.equals("Meal Plan")) {
                        Intent intent = new Intent(HomeActivity.this, SelectMealDateActivity.class);
                        moveToActivity(intent);
                    } else if(homeItem.equals("Shuffle Workout")) {
                        Intent intent = new Intent(HomeActivity.this, ShuffleWorkoutActivity.class);
                        moveToActivity(intent);
                    } else if(homeItem.equals("Favourites")) {
                        Intent intent = new Intent(HomeActivity.this, FavouritesActivity.class);
                        moveToActivity(intent);
                    } else if(homeItem.equals("Progress Pictures")) {
                        Intent intent = new Intent(HomeActivity.this, ProgressPicturesActivity.class);
                        moveToActivity(intent);
                    }

                }
            });

            return customView;
        }
    }

}

