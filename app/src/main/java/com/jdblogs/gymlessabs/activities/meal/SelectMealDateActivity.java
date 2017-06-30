package com.jdblogs.gymlessabs.activities.meal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;

public class SelectMealDateActivity extends AppCompatActivity {

    private String[] weekList = new String[] {"Week 1", "Week 2", "Week 3", "Week 4",
            "Week 5", "Week 6", "Week 7", "Week 8"};

    private GlobalVariables appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_meal_date);
        appContext = (GlobalVariables) getApplicationContext();

        // Week List
        ListAdapter dayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,weekList);
        final ListView listView = (ListView) findViewById(R.id.weekListView);
        listView.setAdapter(dayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectMealDateActivity.this, MealPlanActivity.class);
                appContext.setWeekSelected(listView.getItemAtPosition(position).toString());
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
