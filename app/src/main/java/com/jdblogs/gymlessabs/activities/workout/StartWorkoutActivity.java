package com.jdblogs.gymlessabs.activities.workout;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import com.jdblogs.gymlessabs.R;
import com.jdblogs.gymlessabs.datahandling.GlobalVariables;
import com.jdblogs.gymlessabs.models.Exercise;
import java.util.List;
import java.util.Locale;

public class StartWorkoutActivity extends AppCompatActivity {

    private VideoView exerciseVideo;
    private TextView exerciseTimerTextView;
    private TextView dayTextView;
    private TextView exerciseNameTextView;

    private GlobalVariables appContext;
    private List<Exercise> exerciseList;
    private String currentDay;
    private Exercise currentExercise;
    private int currentExerciseDuration;
    private int currentExerciseIndex;
    private boolean isCurrentWorkoutShuffleWorkout;

    private boolean workoutPaused;
    private boolean didBeginTimer;
    private int intermediateTime;
    private Handler timerHandler;
    private long startTime;
    private TextToSpeech textToSpeech;
    private int textToSpeechResult;
    private int countDownTimeLeft;
    private Context context;

    private static final String SHUFFLE_WORKOUT_TITLE_TEXT = "Shuffle Workout";
    private static final String TEXT_TO_SPEECH_COUNTDOWN_GO = "Go";
    private static final int FIRST_EXERCISE_INDEX = 0;
    private static final int COUNTDOWN_BEFORE_TIMER_BEGINS = 5;
    private static final int MILLI_SECOND_CONVERSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        appContext = (GlobalVariables) getApplicationContext();
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
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void setupUIComponents(){
        exerciseVideo = (VideoView) findViewById(R.id.exerciseVideo);
        exerciseTimerTextView = (TextView) findViewById(R.id.exerciseDurationTextView);
        dayTextView = (TextView) findViewById(R.id.dayTextView);
        exerciseNameTextView = (TextView) findViewById(R.id.exerciseNameTextView);

        //Video Loop
        exerciseVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                exerciseVideo.start(); //need to make transition seamless.
            }
        });

        timerHandler = new Handler();
    }

    private void fetchWorkoutData(){
        exerciseList = appContext.getCurrentWorkout();
        currentDay = appContext.getDaySelected();
        isCurrentWorkoutShuffleWorkout = appContext.isShuffleWorkout();
        currentExercise = exerciseList.get(FIRST_EXERCISE_INDEX);
    }

    private void updateUIInfo(){
        // Title text
        if(isCurrentWorkoutShuffleWorkout)
            dayTextView.setText(SHUFFLE_WORKOUT_TITLE_TEXT);
        else
            dayTextView.setText(currentDay);

        // Exercise Info
        exerciseTimerTextView.setText(Integer.toString(currentExercise.getDuration()));
        exerciseNameTextView.setText(currentExercise.getName());

        // Video
        String path = "android.resource://" + getPackageName() + "/raw/" +
                removeFirstChar(currentExercise.getVideoFileName());
        exerciseVideo.setVideoURI(Uri.parse(path));
        exerciseVideo.requestFocus();
        exerciseVideo.start();

        // Countdown before timer
        currentExerciseDuration = currentExercise.getDuration();
        exerciseTimerTextView.setText(Integer.toString(currentExerciseDuration));
        didBeginTimer = false;
        onCountDownBeforeTimer();
    }

    private void textToSpeechSetup(){
        textToSpeech = new TextToSpeech(StartWorkoutActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    textToSpeechResult = textToSpeech.setLanguage(Locale.UK);
                } else {
                    logMessage("Feature not supported on this device");
                }
            }
        });


    }


    public void onEndWorkout(View view){
        Intent intent = new Intent(StartWorkoutActivity.this, WorkoutActivity.class);
        moveToActivity(intent);
    }

    public void onNextExercise(View view){
        goToNextExercise();
    }

    private void goToNextExercise(){
        if(currentExerciseIndex<exerciseList.size()-1) {
            currentExerciseIndex++;
            currentExercise = exerciseList.get(currentExerciseIndex);
            updateUIInfo();
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
        if(workoutPaused){ // Play
            exerciseVideo.start();
            currentExerciseDuration = intermediateTime;
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
        } else { // Pause
            exerciseVideo.pause();
            intermediateTime = Integer.parseInt(exerciseTimerTextView.getText().toString());
            onPause();
        }
        workoutPaused = !workoutPaused;
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

    private void countdownTextToSpeech(){


//        if(didBeginTimer){
//            textToSpeech.speak(TEXT_TO_SPEECH_COUNTDOWN_GO,TextToSpeech.QUEUE_FLUSH,null);
//        } else {
//            textToSpeech.speak(Integer.toString(countDownTimeLeft),TextToSpeech.QUEUE_ADD,null);
//        }
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
            } else { // Countdown before timer with TTS
                long millis = System.currentTimeMillis() - startTime;
                countDownTimeLeft = COUNTDOWN_BEFORE_TIMER_BEGINS -
                        ((int) millis / MILLI_SECOND_CONVERSION);
                countdownTextToSpeech();
                timerHandler.postDelayed(this, 500);

                // When countdown ends, start the exercise timer
                if(countDownTimeLeft == 0){
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


    public void logMessage(String message){
        Log.i(getClass().getSimpleName(), message);
    }

    private String removeFirstChar(String s){
        return s.substring(1);
    }

    private void moveToActivity(android.content.Intent intent){
        if(intent != null) {
            startActivity(intent);
        }
    }

}