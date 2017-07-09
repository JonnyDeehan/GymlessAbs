package com.jdblogs.gymlessabs.activities.workout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
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
import com.jdblogs.gymlessabs.datahandling.sqldatabase.ExerciseLocalData;
import com.jdblogs.gymlessabs.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Exercise> exerciseList = new ArrayList<Exercise>();
    private static final String DATABASE_NAME = "exercises";
    private WorkoutGenerator workoutGenerator;
    private GlobalVariables appContext;
    private SearchActivity.CustomAdapter customAdapter;
    private ExerciseLocalData exerciseLocalData;
    private SearchView exerciseSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        appContext = GlobalVariables.getInstance();

        setUpDatabase();

        // Exercises List
        ListView listView = (ListView) findViewById(R.id.exerciseList);
        customAdapter = new SearchActivity.CustomAdapter(this,exerciseList);
        listView.setAdapter(customAdapter);

        // Search View
        exerciseSearch = (SearchView) findViewById(R.id.searchView);
        exerciseSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                customAdapter.clear();
                customAdapter.addAll(listAllExercises(query));
                customAdapter.notifyDataSetChanged();
                exerciseSearch.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                customAdapter.clear();
                customAdapter.addAll(listAllExercises(newText));
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMessage("Before Custom Adapter Object destroy, reference is: "  + customAdapter);
        customAdapter = null;
        logMessage("Did destroy Custom Adapter Object, reference is: "  + customAdapter);
        exerciseLocalData.closeDatabase();
    }

    private void setUpDatabase(){
        String exerciseData = getResources().getString(R.string.exercise_list);
        workoutGenerator = new WorkoutGenerator();
        exerciseList = workoutGenerator.generateListOfAllExercises(exerciseData);
        exerciseLocalData = new ExerciseLocalData(this);
    }

    private List<Exercise> listAllExercises(String query){
        exerciseList.clear();

        Cursor cursor = exerciseLocalData.searchExercises(query);

        while(cursor.moveToNext()){

            try {
                if (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_NAME));
                    int experienceLevel = cursor.getInt(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_EXPERIENCE_LEVEL));
                    int duration = cursor.getInt(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_DURATION));
                    String equipment = cursor.getString(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_EQUIPMENT));
                    String videoFileName = cursor.getString(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_VIDEO_FILE_NAME));

                    logMessage("Exercise " + id);
                    logMessage("Name: " + name);
                    logMessage("Experience Level: " + experienceLevel);
                    logMessage("Duration: " + duration);
                    logMessage("Equipment: " + equipment);
                    logMessage("VideoFileName: " + videoFileName);

                    exerciseList.add(new Exercise(id,name,experienceLevel,
                            duration,equipment,videoFileName));
                }
            } catch(Error e){
                logMessage(e.toString());
            }
        }

        return exerciseList;
    }

    public void onBackButton(View view){
        finish();
    }

    private void moveToActivity(android.content.Intent intent){
        if(intent != null) {
            startActivity(intent);
        }
    }

    // TODO: implement relist of exercises based on search query
//    public void searchDatabase(int indexValue ) {
//
//        Cursor cursor = exerciseLocalData.selectRecord(indexValue);
//        if (cursor.moveToNext()) {
//            String name = cursor.getString(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_NAME));
//            int experienceLevel = cursor.getInt(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_EXPERIENCE_LEVEL));
//            int duration = cursor.getInt(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_DURATION));
//            String equipment = cursor.getString(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_EQUIPMENT));
//            String videoFileName = cursor.getString(cursor.getColumnIndex(ExerciseLocalData.EXERCISE_VIDEO_FILE_NAME));
//            logMessage("Exercise " + indexValue);
//            logMessage("Name: " + name);
//            logMessage("Experience Level: " + experienceLevel);
//            logMessage("Duration: " + duration);
//            logMessage("Equipment: " + equipment);
//            logMessage("VideoFileName: " + videoFileName);
//
//        } else {
//            Log.i(getClass().getSimpleName(), "Exercise not found");
//        }
//    }

    private class CustomAdapter extends ArrayAdapter<Exercise> {

        CustomAdapter(Context context, List<Exercise> exercises){
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

                    Intent intent = new Intent(SearchActivity.this, VideoPlayerActivity.class);
                    moveToActivity(intent);
                }
            });

            return customView;
        }
    }

    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }
}
