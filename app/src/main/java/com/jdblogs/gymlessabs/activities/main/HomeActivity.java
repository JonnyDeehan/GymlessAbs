/*
 * Created by Jonny Deehan
 * Copyright (c) 2017. All rights reserved.
 */

package com.jdblogs.gymlessabs.activities.main;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jdblogs.gymlessabs.activities.meal.SelectMealDateActivity;
import com.jdblogs.gymlessabs.activities.workout.ExercisePlanActivity;
import com.jdblogs.gymlessabs.activities.workout.FavouritesActivity;
import com.jdblogs.gymlessabs.activities.workout.SearchActivity;
import com.jdblogs.gymlessabs.activities.workout.WorkoutActivity;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.WorkoutGenerator;
import com.jdblogs.gymlessabs.datahandling.sqldatabase.ExerciseLocalData;
import com.jdblogs.gymlessabs.models.Exercise;
import com.google.android.gms.ads.MobileAds;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<Exercise> exerciseList = new ArrayList<Exercise>();

    private TextView workoutWeek;
    private TextView workoutDay;
    private AdView mAdView;

    private WorkoutGenerator workoutGenerator;
    private GlobalVariables globalVariables;
    private ExerciseLocalData exerciseLocalData;
    private SharedPreferences sharedPreferences;

    private static final int NORMAL_WORKOUT_ACTIVITY_TYPE = 0;
    private static final int SHUFFLE_WORKOUT_ACTIVITY_TYPE = 1;

    private String [] homeItemsList = new String[]{"Exercise Plan", "Meal Plan", "Shuffle Workout",
            "Favourites"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        globalVariables = GlobalVariables.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initializeAdMob();
        setCurrentWeekAndDay();
        updateUI();
        onUpdateLocalData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentWeekAndDay();
        onUpdateLocalData();
    }

    private void initializeAdMob(){
        MobileAds.initialize(this, globalVariables.ADMOB_APP_ID);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setCurrentWeekAndDay(){
        globalVariables.setWeekSelected(sharedPreferences.
                getString(globalVariables.PREFERENCES_CURRENT_WEEK_KEY,""));
        globalVariables.setDaySelected(sharedPreferences.
                getString(globalVariables.PREFERENCES_CURRENT_DAY_KEY,""));
        workoutWeek = (TextView) findViewById(R.id.weekTextView);
        workoutDay = (TextView) findViewById(R.id.dayTextView);
        workoutWeek.setText(globalVariables.getWeekSelected());
        workoutDay.setText(globalVariables.getDaySelected());

        float progressPercentage = 0;
        Log.i(getClass().getSimpleName(), "Current Week and Day: " +
                globalVariables.getWeekSelected() + " " + globalVariables.getDaySelected());
        if(!globalVariables.getWeekSelected().equals("") &&
                !globalVariables.getDaySelected().equals("") ) {
            float currentWeekNumber = Integer.parseInt(globalVariables.getWeekSelected().substring(5));
            float currentDayNumber = Integer.parseInt(globalVariables.getDaySelected().substring(4));
            // 56 days in total (100%) first week starts at 0%, therefore -7 days to set this initially
            progressPercentage += (((currentWeekNumber * 7) - 7) + currentDayNumber) / 56 * 100;
        }
        Log.i(getClass().getSimpleName(), "Progress Percentage: " + progressPercentage + "%");
        CircularProgressBar circularProgressBar = (CircularProgressBar)findViewById(R.id.progressBar);
        int animationDuration = 2500; // 2500ms = 2,5s
        circularProgressBar.setProgressWithAnimation(progressPercentage, animationDuration); // Default duration = 1500ms
    }

    private void updateUI(){
        setCurrentWeekAndDay();
        ListView listView = (ListView) findViewById(R.id.homeItemsList);
        CustomAdapter customAdapter = new CustomAdapter(this,homeItemsList);
        listView.setAdapter(customAdapter);
    }

    private void onUpdateLocalData(){
        //the app is being launched for first time or settings have been refreshed
        if (sharedPreferences.getBoolean(globalVariables.PREFERENCES_INITIAL_STATE_KEY, true)) {
            // first time task
            saveStringPreferences(globalVariables.PREFERENCES_CURRENT_WEEK_KEY,"Week 1");
            saveStringPreferences(globalVariables.PREFERENCES_CURRENT_DAY_KEY,"Day 1");
            setCurrentWeekAndDay();
            // record the fact that the app has been started at least once
            sharedPreferences.edit()
                    .putBoolean(globalVariables.PREFERENCES_INITIAL_STATE_KEY, false).commit();
            sharedPreferences.edit().commit();

            displayEquipmentSelection();
            setUpNotificationReminder();
        }
        globalVariables.setEquipmentAvailable(sharedPreferences
                .getInt(globalVariables.PREFERENCES_EQUIPMENT_KEY,0));
    }

    private void setUpNotificationReminder(){
        Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE,21);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void displayEquipmentSelection(){
        // Equipment to select from: none [0], Exercise Ball [1], Chin Up Bar [2], both [3]
        CharSequence equipment[] = new CharSequence[] {"None", "Exercise Ball", "Chin Up Bar",
                "Both"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the equipment you have");
        builder.setCancelable(false);
        builder.setItems(equipment, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on selected equipment
                System.out.println("Equipment Available Selected: " + which);
                int equipment_selected=which;
                saveIntPreferences(globalVariables.PREFERENCES_EQUIPMENT_KEY,equipment_selected);
                createDatabase();
            }
        });
        builder.show();
    }

    private void createDatabase(){
        int equipmentAvailable = sharedPreferences
                .getInt(globalVariables.PREFERENCES_EQUIPMENT_KEY,0);
        globalVariables.setEquipmentAvailable(equipmentAvailable);

        String exerciseData = getResources().getString(R.string.exercise_list);
        workoutGenerator = new WorkoutGenerator(equipmentAvailable);
        exerciseList = workoutGenerator.generateListOfAllExercises(exerciseData);
        exerciseLocalData = new ExerciseLocalData(this);
        exerciseLocalData.initData(exerciseList);
        exerciseLocalData.listAllExercises();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        // Ensure the current day is not a rest day before proceeding (days 3 and 7)
        if((!workoutDay.getText().toString().equals("Day 3")) &&
                (!workoutDay.getText().toString().equals("Day 7"))) {
            globalVariables.setWorkoutActivityType(NORMAL_WORKOUT_ACTIVITY_TYPE);
            Intent intent = new Intent(HomeActivity.this, WorkoutActivity.class);
            moveToActivity(intent);
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
            }

            customView.setBackgroundResource(R.drawable.customshape); // rounded edges

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
                        globalVariables.setWorkoutActivityType(SHUFFLE_WORKOUT_ACTIVITY_TYPE);
                        Intent intent = new Intent(HomeActivity.this, WorkoutActivity.class);
                        moveToActivity(intent);
                    } else if(homeItem.equals("Favourites")) {
                        Intent intent = new Intent(HomeActivity.this, FavouritesActivity.class);
                        moveToActivity(intent);
                    }
                }
            });

            return customView;
        }
    }

    // == Shared Preferences Methods ===============================================================

    private void saveIntPreferences(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void saveStringPreferences(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}

