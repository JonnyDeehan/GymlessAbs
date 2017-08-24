/*
 * Created by Jonny Deehan
 * Copyright (c) 2017. All rights reserved.
 */

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
import com.jdblogs.gymlessabs.datahandling.sqldatabase.ExerciseLocalData;
import com.jdblogs.gymlessabs.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Exercise> exerciseList = new ArrayList<Exercise>();
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
                listExercisesOnQuery(query);
                customAdapter.notifyDataSetChanged();
                exerciseSearch.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                customAdapter.clear();
                listExercisesOnQuery(newText);
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customAdapter = null;
        exerciseLocalData.closeDatabase();
    }

    private void setUpDatabase(){
        exerciseLocalData = new ExerciseLocalData(this);
        exerciseList = exerciseLocalData.getCompleteExerciseList();
    }

    private void listExercisesOnQuery(String query){
        this.exerciseList.clear();
        Cursor cursor = exerciseLocalData.fetchExerciseByName(query);

        int cursorCount = cursor.getCount();
        int exerciseCount = 0;

        if(cursor!=null && cursorCount>0) {
            do {
                exerciseCount++;
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int experienceLevel = cursor.getInt(2);
                int duration = cursor.getInt(3);
                String equipment = cursor.getString(4);
                String videoFileName = cursor.getString(5);

                exerciseList.add(new Exercise(id, name, experienceLevel,
                        duration, equipment, videoFileName));

            } while (cursor.moveToNext());
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
            // Set color and rounded edges
            videoButton.setBackgroundResource(R.drawable.customshape);

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
