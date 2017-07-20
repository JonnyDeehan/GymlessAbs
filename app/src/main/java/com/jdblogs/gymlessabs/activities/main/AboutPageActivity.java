package com.jdblogs.gymlessabs.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;

public class AboutPageActivity extends AppCompatActivity {

    private TextView aboutTextView;
    private GlobalVariables globalVariables;
    private AdView mAdView;
    private static final String ABOUT_TEXT = "Our 8 week exercise and meal plan for your abdominal " +
            "core is the routine you've been looking for. This guided routine will help " +
            "you reach your goal of building a stronger core and having defined abs.\n"
            +"\nGymless Abs was created by Jonathan Deehan (Developer)" +
            " & Christopher Georgiou (Content Creator)\n" +
            "\nCheck out www.weightlossyoda.com for more health and fitness advice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        initializeAdMob();
        setUpUI();
    }


    private void setUpUI(){
        aboutTextView = (TextView) findViewById(R.id.aboutTextView);
        aboutTextView.setText(ABOUT_TEXT);
    }

    private void initializeAdMob(){
        MobileAds.initialize(this, globalVariables.ADMOB_APP_ID);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void onBackButton(View view){
        finish();
    }
}
