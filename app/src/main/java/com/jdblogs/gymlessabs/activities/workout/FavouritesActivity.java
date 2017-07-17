package com.jdblogs.gymlessabs.activities.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.datahandling.sqldatabase.FavouritesLocalData;
import com.jdblogs.gymlessabs.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    List<String> favouritesList = new ArrayList<String>();
    // List of favourite workouts
    private List<List<Exercise>> workoutList = new ArrayList<List<Exercise>>();
    // Access favourites data
    private FavouritesLocalData favouritesLocalData;
    private GlobalVariables appContext;
    private static final int WORKOUT_ACTIVITY_TYPE = 2;
    private TextView noFavouritesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        appContext = GlobalVariables.getInstance();
        noFavouritesTextView = (TextView) findViewById(R.id.noFavourites);

        fetchDataFromDatabase();
        setUpListAdapter();
    }

    private void setUpListAdapter(){
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,favouritesList);

        final ListView listView = (ListView) findViewById(R.id.favouritesListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavouritesActivity.this, WorkoutActivity.class);
                Log.i(getClass().getSimpleName(), "WorkoutList size before sending: " + workoutList.size());
                appContext.setCurrentWorkout(workoutList.get(position));
                appContext.setWorkoutActivityType(WORKOUT_ACTIVITY_TYPE);

                moveToActivity(intent);
            }
        });
    }

    private void fetchDataFromDatabase(){
        favouritesLocalData = new FavouritesLocalData(this);
        workoutList = favouritesLocalData.getAllFavourites();

        Log.i(getClass().getSimpleName(), "WorkoutList fetched from FavouritesLocalData has size: "
        + workoutList.size());

        int workoutNum=1;

        for(List<Exercise> workout: workoutList){
            Log.i(getClass().getSimpleName(), "Adding workout " + workoutNum);
            favouritesList.add("Workout: " + workoutNum);
            for (Exercise exercise: workout) {
                Log.i(getClass().getSimpleName(), "Adding exercise to workoutList ");
                Log.i(getClass().getSimpleName(), exercise.getName() + " " +
                        exercise.getVideoFileName());
            }
            workoutNum++;
        }


        if(workoutList.size()==0){
            noFavouritesTextView.setText("No Workouts Favourited");
        } else {
            noFavouritesTextView.setText("");
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

}
