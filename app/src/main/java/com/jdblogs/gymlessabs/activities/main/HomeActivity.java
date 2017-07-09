package com.jdblogs.gymlessabs.activities.main;

import android.content.Context;
import android.content.Intent;
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
import com.jdblogs.gymlessabs.activities.workout.WorkoutActivity;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.WorkoutGenerator;
import com.jdblogs.gymlessabs.datahandling.sqldatabase.ExerciseLocalData;
import com.jdblogs.gymlessabs.datahandling.sqldatabase.FavouritesLocalData;
import com.jdblogs.gymlessabs.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private WorkoutGenerator workoutGenerator;
    private GlobalVariables globalVariables;
    private ExerciseLocalData exerciseLocalData;
    private FavouritesLocalData favouritesLocalData;
    private List<Exercise> exerciseList = new ArrayList<Exercise>();
    private String [] homeItemsList = new String[]{"Exercise Plan", "Meal Plan", "Shuffle Workout",
            "Favourites"};
    private TextView workoutWeek;
    private TextView workoutDay;
    private TextView titleTextView;
    private GlobalVariables appContext;
    private static final int SHUFFLE_WORKOUT_ACTIVITY_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        globalVariables = GlobalVariables.getInstance();
        appContext = GlobalVariables.getInstance();

        workoutWeek = (TextView) findViewById(R.id.weekTextView);
        workoutDay = (TextView) findViewById(R.id.dayTextView);
        workoutWeek.setText(globalVariables.getWeekSelected());
        workoutDay.setText(globalVariables.getDaySelected());

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
        exerciseLocalData = new ExerciseLocalData(this);
        exerciseLocalData.initData(exerciseList);
        exerciseLocalData.listAllExercises();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        exerciseLocalData.closeDatabase();
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
                        appContext.setWorkoutActivityType(SHUFFLE_WORKOUT_ACTIVITY_TYPE);
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

}

