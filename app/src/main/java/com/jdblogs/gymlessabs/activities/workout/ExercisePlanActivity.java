package com.jdblogs.gymlessabs.activities.workout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;

public class ExercisePlanActivity extends AppCompatActivity {

    private String[] weekList = new String[] {"Week 1", "Week 2", "Week 3", "Week 4",
            "Week 5", "Week 6", "Week 7", "Week 8"};

    private String[] dayList = new String[] {"Day 1", "Day 2", "Day 3", "Day 4", "Day 5",
            "Day 6", "Day 7"};

    private GlobalVariables appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_plan);
        appContext = (GlobalVariables) getApplicationContext();

        // Drop Down Week Selection
        final Spinner weekListSpinner = (Spinner) findViewById(R.id.weekSpinner);
        ListAdapter weekAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,weekList);
        weekListSpinner.setAdapter((SpinnerAdapter) weekAdapter);
        weekListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appContext.setWeekSelected(weekListSpinner.getItemAtPosition(position).toString());
                System.out.println(appContext.getWeekSelected());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Day of the Week List
        ListAdapter dayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,dayList);
        final ListView listView = (ListView) findViewById(R.id.dayListView);
        listView.setAdapter(dayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExercisePlanActivity.this, WorkoutActivity.class);
                appContext.setDaySelected(listView.getItemAtPosition(position).toString());
                System.out.println(appContext.getDaySelected());
                moveToActivity(intent);
            }
        });
    }
    private void moveToActivity(android.content.Intent intent){
        if(intent != null) {
            startActivity(intent);
        }
    }
}
