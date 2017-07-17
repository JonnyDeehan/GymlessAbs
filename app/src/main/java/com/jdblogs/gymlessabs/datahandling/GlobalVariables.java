package com.jdblogs.gymlessabs.datahandling;

import android.app.Application;

import com.jdblogs.gymlessabs.models.Exercise;

import java.util.List;

/**
 * Created by jonathandeehan on 12/06/2017.
 */

public class GlobalVariables{

    private String weekSelected;
    private String daySelected;
    private Exercise currentExerciseSelected;
    // Equipment to select from: none [0], Exercise Ball [1], Chin Up Bar [2], both [3]
    private int equipmentAvailable;
    private List<Exercise> currentWorkout;
    private double currentTotalWorkoutDuration;
    // 0 - Day/Week Workout 1 - Shuffle Workout 2 - Favourite
    private int workoutActivityType;

    public static final String PREFS_NAME = "shared_prefs";
    public static final String PREFERENCES_INITIAL_STATE_KEY = "initial_state";
    public static final String PREFERENCES_CURRENT_WEEK_KEY = "current_week";
    public static final String PREFERENCES_CURRENT_DAY_KEY = "current_day";
    public static final String PREFERENCES_EQUIPMENT_KEY = "equipment";

    protected GlobalVariables(){};

    public int getEquipmentAvailable() {
        return equipmentAvailable;
    }

    public void setEquipmentAvailable(int equipmentAvailable) {
        this.equipmentAvailable = equipmentAvailable;
    }


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