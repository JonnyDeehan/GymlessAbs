package com.jdblogs.gymlessabs.activities.workout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.models.Exercise;
import java.util.List;

public class StartWorkoutActivity extends AppCompatActivity {

    private VideoView exerciseVideo;
    private TextView exerciseTimerTextView;
    private TextView dayTextView;
    private TextView exerciseNameTextView;
    private ImageButton pausePlayButton;

    private GlobalVariables appContext;
    private List<Exercise> exerciseList;
    private String currentDay;
    private Exercise currentExercise;
    private int currentExerciseDuration;
    private int currentExerciseIndex;

    private boolean workoutPaused;
    private boolean didBeginTimer;
    private boolean didBeginCountdown;
    private boolean canSpeakExerciseName;
    private int intermediateTime;
    private Handler timerHandler;
    private long startTime;
    private int countDownTimeLeft;
    private Context context;
    private MediaPlayer mediaPlayer;

    private static final String SHUFFLE_WORKOUT_TITLE_TEXT = "Shuffle Workout";
    private static final int FIRST_EXERCISE_INDEX = 0;
    private static final int COUNTDOWN_BEFORE_TIMER_BEGINS = 7;
    private static final int MILLI_SECOND_CONVERSION = 1000;
    private static final int NORMAL_WORKOUT_ACTIVITY_TYPE = 0;
    private static final int SHUFFLE_WORKOUT_ACTIVITY_TYPE = 1;
    private static final int FAVOURITE_WORKOUT_ACTIVITY_TYPE = 2;

    final int ic_media_pause = android.R.drawable.ic_media_pause;
    final int ic_media_play = android.R.drawable.ic_media_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        appContext = GlobalVariables.getInstance();
        context = getApplicationContext();

        textToSpeechSetup();
        setupUIComponents();
        fetchWorkoutData();
        updateUIInfo();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appContext.setCurrentWorkout(exerciseList);
    }

    private void setupUIComponents(){
        exerciseVideo = (VideoView) findViewById(R.id.exerciseVideo);
        exerciseTimerTextView = (TextView) findViewById(R.id.exerciseDurationTextView);
        dayTextView = (TextView) findViewById(R.id.dayTextView);
        exerciseNameTextView = (TextView) findViewById(R.id.exerciseNameTextView);
        pausePlayButton = (ImageButton) findViewById(R.id.pause_playButton);

        int activityType = appContext.getWorkoutActivityType();
        // Title text
        if( activityType== NORMAL_WORKOUT_ACTIVITY_TYPE) {
            dayTextView.setText(currentDay);
        } else if(activityType ==SHUFFLE_WORKOUT_ACTIVITY_TYPE){
            dayTextView.setText(SHUFFLE_WORKOUT_TITLE_TEXT);
        } else if(activityType == FAVOURITE_WORKOUT_ACTIVITY_TYPE){
            dayTextView.setText("");
        }

        //Video Loop
        exerciseVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                exerciseVideo.start(); //need to make transition seamless.
                mp.setVolume(0f,0f);
            }
        });

        timerHandler = new Handler();

        // Appearance of buttons to be blue and have rounded edges
        pausePlayButton.setBackgroundResource(R.drawable.customshape);
        Button endWorkoutButton = (Button) findViewById(R.id.endWorkoutButton);
        ImageButton nextExerciseButton = (ImageButton) findViewById(R.id.nextExercise);
        ImageButton previousExerciseButton = (ImageButton) findViewById(R.id.previousExercise);
        ImageButton refreshTimerButton = (ImageButton) findViewById(R.id.resetExerciseButton);
        endWorkoutButton.setBackgroundResource(R.drawable.customshape);
        nextExerciseButton.setBackgroundResource(R.drawable.customshape);
        previousExerciseButton.setBackgroundResource(R.drawable.customshape);
        refreshTimerButton.setBackgroundResource(R.drawable.customshape);
    }

    private void fetchWorkoutData(){
        exerciseList = appContext.getCurrentWorkout();
        currentDay = appContext.getDaySelected();
        currentExercise = exerciseList.get(FIRST_EXERCISE_INDEX);
    }

    private void updateUIInfo(){

        // Exercise Info
        exerciseTimerTextView.setText(Integer.toString(currentExercise.getDuration()));
        exerciseNameTextView.setText(currentExercise.getName());

        // Video
        String path = "android.resource://" + getPackageName() + "/raw/" +
                removeFirstChar(currentExercise.getVideoFileName());
        exerciseVideo.setVideoURI(Uri.parse(path));
        exerciseVideo.requestFocus();
        exerciseVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f,0f);
            }
        });
        exerciseVideo.start();

        // Audio
        if(mediaPlayer != null){
            stopAudioMedia();
        }

        // Countdown before timer
        currentExerciseDuration = currentExercise.getDuration();
        exerciseTimerTextView.setText(Integer.toString(currentExerciseDuration));
        didBeginTimer = false;
        onCountDownBeforeTimer();

        // Refresh Pause State Info
        workoutPaused = false;
        pausePlayButton.setImageResource(ic_media_pause);

    }

    private void textToSpeechSetup(){
        mediaPlayer = MediaPlayer.create(this,R.raw.count_down);
    }

    public void onEndWorkout(View view){
        if(mediaPlayer !=null){
            mediaPlayer.stop();
        }
        finish();
    }

    public void onNextExercise(View view){
        goToNextExercise();
    }

    private void goToNextExercise(){
        if(currentExerciseIndex<exerciseList.size()-1) {
            currentExerciseIndex++;
            currentExercise = exerciseList.get(currentExerciseIndex);
            updateUIInfo();
        } else {
            onCompletedWorkout();
        }
    }

    public void onPreviousExercise(View view){
        goToPreviousExercise();
    }

    private void goToPreviousExercise(){
        if(currentExerciseIndex>FIRST_EXERCISE_INDEX){
            currentExerciseIndex--;
            currentExercise = exerciseList.get(currentExerciseIndex);
            updateUIInfo();
        }
    }

    public void onPausePlay(View view){
        if(didBeginTimer) {
            if (workoutPaused) { // Play
                exerciseVideo.start();
                currentExerciseDuration = intermediateTime;
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                pausePlayButton.setImageResource(ic_media_pause);

            } else { // Pause
                exerciseVideo.pause();
                intermediateTime = Integer.parseInt(exerciseTimerTextView.getText().toString());
                onPause();
                pausePlayButton.setImageResource(ic_media_play);
            }
            workoutPaused = !workoutPaused;
        }
    }

    public void onResetExerciseTimer(View view){
        startExerciseTimer();
    }

    private void startExerciseTimer(){
        if(didBeginTimer){
            onPause();
            currentExerciseDuration = currentExercise.getDuration();
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    private void onCountDownBeforeTimer(){
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void onExerciseFinish(){
        timerHandler.removeCallbacks(timerRunnable);
        goToNextExercise();
    }

    private void startAudioCountDown(){
        if(!didBeginCountdown) {
            didBeginCountdown = true;

            // CountDownAudio
            mediaPlayer = MediaPlayer.create(this, R.raw.count_down);
            mediaPlayer.start();
        }
    }

    private void announceExerciseName(){
        if(!canSpeakExerciseName) {
            canSpeakExerciseName = true;
            String path = "android.resource://" + getPackageName() + "/raw/" +
                    removeFirstChar(currentExercise.getVideoFileName()) + "_audio";

            mediaPlayer = MediaPlayer.create(this, Uri.parse(path));
            mediaPlayer.start();
        }
    }

    private void stopAudioMedia(){
        didBeginCountdown = false;
        canSpeakExerciseName = false;
        mediaPlayer.stop();
    }

    private void onCompletedWorkout(){
        exerciseVideo.pause();
        mediaPlayer.stop();
        onPause();
        mediaPlayer = MediaPlayer.create(this,R.raw.workout_completed);
        mediaPlayer.start();

        // Display PopUp Message
        AlertDialog.Builder finishedWorkoutAlert = new AlertDialog.Builder(this);
        finishedWorkoutAlert.setCancelable(false);
        finishedWorkoutAlert.setMessage("Workout Completed!\nYour progress has been logged")
                            .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    incrementRoutineDate();
                                    mediaPlayer.stop();
                                    finish();
                                }
                            })
                            .create();
        finishedWorkoutAlert.show();
    }

    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            if(didBeginTimer) { // Exercise duration timer
                long millis = System.currentTimeMillis() - startTime;

                int timeLeft = currentExerciseDuration - ((int) millis / MILLI_SECOND_CONVERSION);
                exerciseTimerTextView.setText(Integer.toString(timeLeft));

                timerHandler.postDelayed(this, 500);

                if (timeLeft == 0) {
                    onExerciseFinish();
                }
            } else {// Countdown before timer with TTS
                long millis = System.currentTimeMillis() - startTime;
                countDownTimeLeft = COUNTDOWN_BEFORE_TIMER_BEGINS -
                        ((int) millis / MILLI_SECOND_CONVERSION);
                announceExerciseName();
                timerHandler.postDelayed(this, 500);

                // When Exercise Name Announced, Speak Countdown
                if(countDownTimeLeft == 5){
                    startAudioCountDown();
                }

                // When countdown ends, start the exercise timer
                if (countDownTimeLeft == 0) {
                    didBeginTimer = true;
                    startExerciseTimer();
                }
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    private String removeFirstChar(String s){
        return s.substring(1);
    }

    public void onBackButton(View view){
        if(mediaPlayer !=null){
            mediaPlayer.stop();
        }
        finish();
    }

    private void incrementRoutineDate(){
        if(appContext.getWorkoutActivityType() == NORMAL_WORKOUT_ACTIVITY_TYPE) {
            int weekNum = Integer.parseInt(String.valueOf(appContext.getWeekSelected().charAt(5)));
            int dayNum = Integer.parseInt(String.valueOf(appContext.getDaySelected().charAt(4)));

            dayNum++;
            if(dayNum>7 && weekNum<8){
                dayNum=1;
                weekNum++;
            } else if(weekNum==8 && dayNum>7) {
                dayNum=1;
                weekNum=1;
            }

            String newWeek = "Week " + weekNum;
            String newDay = "Day " + dayNum;

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            settings.edit().putString(appContext.PREFERENCES_CURRENT_WEEK_KEY,newWeek).commit();
            settings.edit().putString(appContext.PREFERENCES_CURRENT_DAY_KEY,newDay).commit();
            appContext.setWeekSelected(newWeek);
            appContext.setDaySelected(newDay);
        }
    }
}