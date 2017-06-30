package com.jdblogs.gymlessabs.datahandling;

import android.app.Application;

import com.jdblogs.gymlessabs.models.Exercise;

import java.util.List;

/**
 * Created by jonathandeehan on 12/06/2017.
 */

public class GlobalVariables extends Application{

    private boolean exerciseReminder = false;
    private int reminderTime = 8; // TODO: make into date format (currently in minutes)
    private String weekSelected = "Week 1";
    private String daySelected = "Day 1";
    private Exercise currentExerciseSelected;
    private List<Exercise> currentWorkout;
    private double currentTotalWorkoutDuration;
    private boolean isShuffleWorkout;

    public boolean isShuffleWorkout() {
        return isShuffleWorkout;
    }

    public void setShuffleWorkout(boolean shuffleWorkout) {
        this.isShuffleWorkout = shuffleWorkout;
    }

    public List<Exercise> getCurrentWorkout() {
        return currentWorkout;
    }

    public void setCurrentWorkout(List<Exercise> currentWorkout) {
        this.currentWorkout = currentWorkout;
    }

    public double getCurrentTotalWorkoutDuration() {
        return currentTotalWorkoutDuration;
    }

    public void setCurrentTotalWorkoutDuration(double currentTotalWorkoutDuration) {
        this.currentTotalWorkoutDuration = currentTotalWorkoutDuration;
    }

    public Exercise getCurrentExerciseSelected() {
        return currentExerciseSelected;
    }

    public void setCurrentExerciseSelected(Exercise currentExerciseSelected) {
        this.currentExerciseSelected = currentExerciseSelected;
    }

    public String getWeekSelected() {
        return weekSelected;
    }

    public void setWeekSelected(String weekSelected) {
        this.weekSelected = weekSelected;
    }

    public String getDaySelected() {
        return daySelected;
    }

    public void setDaySelected(String daySelected) {
        this.daySelected = daySelected;
    }

    public void setExerciseReminder(boolean state){
        this.exerciseReminder = state;
    }

    public boolean getExerciseReminder(){
        return this.exerciseReminder;
    }

    public void setReminderTime(int time){
        this.reminderTime = time;
    }

    public int getReminderTime(){
        return this.reminderTime;
    }

}