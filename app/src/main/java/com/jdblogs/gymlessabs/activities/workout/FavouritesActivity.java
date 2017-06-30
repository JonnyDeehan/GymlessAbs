package com.jdblogs.gymlessabs.activities.workout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jdblogs.gymlessabs.R;

public class FavouritesActivity extends AppCompatActivity {

    String[] favouritesList = new String[] {"FirstOne", "Second", "Third"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,favouritesList);

        ListView listView = (ListView) findViewById(R.id.favouritesListView);
        listView.setAdapter(adapter);

    }

}
