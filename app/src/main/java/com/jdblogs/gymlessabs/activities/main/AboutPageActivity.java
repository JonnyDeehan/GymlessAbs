package com.jdblogs.gymlessabs.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdblogs.gymlessabs.R;

public class AboutPageActivity extends AppCompatActivity {

    private TextView aboutTextView;
    private static final String ABOUT_TEXT = "Gymless Abs was created by Jonathan Deehan" +
            " & Christopher Georgiou";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        aboutTextView = (TextView) findViewById(R.id.aboutTextView);
        aboutTextView.setText(ABOUT_TEXT);
    }

    public void onBackButton(View view){
        finish();
    }
}
