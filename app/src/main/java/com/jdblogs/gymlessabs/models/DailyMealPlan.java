/*
 * Created by Jonny Deehan 14/06/2017.
 * Copyright (c) 2017. All rights reserved.
 */

package com.jdblogs.gymlessabs.models;

public class DailyMealPlan {

    private String week;
    private String day;
    private String breakfast;
    private String snack1;
    private String lunch;
    private String snack2;
    private String dinner;

    public DailyMealPlan(String week, String day, String breakfast, String snack1, String lunch,
                         String snack2, String dinner){
        this.week = week;
        this.day = day;
        this.breakfast = breakfast;
        this.snack1 = snack1;
        this.snack2 = snack2;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getSnack1() {
        return snack1;
    }

    public void setSnack1(String snack1) {
        this.snack1 = snack1;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getSnack2() {
        return snack2;
    }

    public void setSnack2(String snack2) {
        this.snack2 = snack2;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

}
