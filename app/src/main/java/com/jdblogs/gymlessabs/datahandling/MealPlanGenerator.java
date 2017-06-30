package com.jdblogs.gymlessabs.datahandling;

import com.jdblogs.gymlessabs.models.DailyMealPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by jonathandeehan on 27/06/2017.
 */

public class MealPlanGenerator {

    private String week;
    private String day;

    public MealPlanGenerator(String week, String day){
        this.week = week;
        this.day = day;
    }

    public List<DailyMealPlan> generateMealPlan(String mealData){

        List<DailyMealPlan> dailyMealPlan = new ArrayList<DailyMealPlan>();
        Scanner scanner = new Scanner(mealData);

        String currentWeek;
        String currentDay;
        String breakfast;
        String snack1;
        String lunch;
        String snack2;
        String dinner;
        int noOfDaysInWeek = 7;
        int daysAdded = 0;
        int daysPassed = 0;

        while (scanner.hasNextLine()) {

            currentWeek = scanner.nextLine();
            // OR operator is current sloppy hack to deal with first index substring of " " in week
            if(currentWeek.equals(week) || currentWeek.equals(" "+ week)) {
                while (daysAdded < noOfDaysInWeek && scanner.hasNextLine()) {
                    currentDay = scanner.nextLine();
                    breakfast = scanner.nextLine();
                    snack1 = scanner.nextLine();
                    lunch = scanner.nextLine();
                    snack2 = scanner.nextLine();
                    dinner = scanner.nextLine();
                    dailyMealPlan.add(new DailyMealPlan(currentWeek, currentDay, breakfast,
                            snack1, lunch, snack2, dinner));
                    daysAdded++;
                }
                daysAdded = 0;
            } else {    // continue and pass through week
                while(daysPassed<noOfDaysInWeek && scanner.hasNextLine()){
                    for(int mealItems=0;mealItems<6;mealItems++) {
                        scanner.nextLine();
                    }
                    daysPassed++;
                }
                daysPassed=0;
            }
        }

        scanner.close();
        return dailyMealPlan;
    }
}
