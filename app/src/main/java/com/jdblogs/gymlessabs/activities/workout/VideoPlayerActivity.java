/*
 * Created by Jonny Deehan
 * Copyright (c) 2017. All rights reserved.
 */

package com.jdblogs.gymlessabs.activities.workout;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.models.Exercise;

public class VideoPlayerActivity extends AppCompatActivity {

    private GlobalVariables appContext;
    private Exercise selectedExercise;
    private TextView currentExerciseNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        appContext = GlobalVariables.getInstance();
        selectedExercise = appContext.getCurrentExerciseSelected();
        currentExerciseNameTextView = (TextView) findViewById(R.id.currentExerciseName);
        currentExerciseNameTextView.setText(selectedExercise.getName());

        setUpVideoView();
    }

    private void setUpVideoView(){
        VideoView videoView = (VideoView)findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/raw/" +
                removeFirstChar(selectedExercise.getVideoFileName());
        videoView.setVideoURI(Uri.parse(path));
        videoView.requestFocus();
        videoView.setMediaController(new MediaController(this));

        try {
            videoView.start();
        } catch(Error e){
            logMessage(e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appContext.setCurrentExerciseSelected(selectedExercise);
    }

    public void onBackButton(View view){
        finish();
    }

    private String removeFirstChar(String s){
        return s.substring(1);
    }

    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }
}