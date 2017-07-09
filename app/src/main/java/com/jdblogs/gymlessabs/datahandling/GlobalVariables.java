package com.jdblogs.gymlessabs.datahandling;

import android.app.Application;

import com.jdblogs.gymlessabs.models.Exercise;

import java.util.List;

/**
 * Created by jonathandeehan on 12/06/2017.
 */

public class GlobalVariables{

    private String weekSelected = "Week 1";
    private String daySelected = "Day 1";
    private Exercise currentExerciseSelected;
    private List<Exercise> currentWorkout;
    private double currentTotalWorkoutDuration;
    private int workoutActivityType;    // 0 - Day/Week Workout 1 - Shuffle Workout 2 - Favourite

    protected GlobalVariables(){};

    public int getWorkoutActivityType() {
        return workoutActivityType;
    }

    public void setWorkoutActivityType(int workoutActivityType) {
        this.workoutActivityType = workoutActivityType;
    }

    private static GlobalVariables ourInstance = new GlobalVariables();

    public static GlobalVariables getInstance(){
        return ourInstance;
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


}