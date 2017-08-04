package com.jdblogs.gymlessabs.activities.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.sqldatabase.ExerciseLocalData;
import com.jdblogs.gymlessabs.datahandling.sqldatabase.FavouritesLocalData;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupButtonAppearance();

    }

    private void setupButtonAppearance(){
        Button resetExerciseData = (Button) findViewById(R.id.resetDataButton);
        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        resetExerciseData.setBackgroundResource(R.drawable.customshape);
        aboutButton.setBackgroundResource(R.drawable.customshape);
    }

    public void onAboutButton(View view){
        Intent intent = new Intent(SettingsActivity.this, AboutPageActivity.class);
        moveToActivity(intent);
    }

    public void onResetSettingsButton(View view){
        // Clear SQLite Database (Exercises and Favourites)
        ExerciseLocalData exerciseLocalData = new ExerciseLocalData(this);
        FavouritesLocalData favouritesLocalData = new FavouritesLocalData(this);
        exerciseLocalData.clearExerciseTableEntries();
        favouritesLocalData.clearFavouritesTableEntries();

        // Clear Shared Preferences (Current Week/Day and Equipment Available)
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().clear().commit();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Successfully Reset Settings")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create();
        builder.show();
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