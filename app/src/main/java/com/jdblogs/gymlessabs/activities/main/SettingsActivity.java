package com.jdblogs.gymlessabs.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jdblogs.gymlessabs.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onAboutButton(View view){
        Intent intent = new Intent(SettingsActivity.this, AboutPageActivity.class);
        moveToActivity(intent);
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