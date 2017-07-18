package com.jdblogs.gymlessabs.datahandling;

import android.util.Log;

import com.jdblogs.gymlessabs.models.Exercise;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by jonathandeehan on 16/06/2017.
 */

public class WorkoutGenerator {

    private String currentWeek;
    private String currentDay;
    private String currentName;
    private int currentExperienceLevel;
    private int currentDuration;
    private int currentExerciseOffsetAmount; // Offset used in exercise list return
    private static final int NUMBER_OF_EXERCISES_IN_WORKOUT = 7;
    private static final int BEGINNER_EXPERIENCE_LEVEL = 1;
    private static final int INTERMEDIATE_EXPERIENCE_LEVEL = 2;
    private static final int ADVANCED_EXPERIENCE_LEVEL = 3;
    private List<Exercise> exerciseList = new ArrayList<Exercise>();
    // Equipment to select from: none [0], Exercise Ball [1], Chin Up Bar [2], both [3]
    private int equipmentAvailable;

    public WorkoutGenerator(String currentWeek, String currentDay, int equipmentAvailable){
        this.currentWeek = currentWeek;
        this.currentDay = currentDay;
        this.equipmentAvailable = equipmentAvailable;
    }

    public WorkoutGenerator(int equipmentAvailable){
        this.equipmentAvailable = equipmentAvailable;
    }

    // Returns 7 exercises for the workout
    public List<Exercise> generateWorkout(String exerciseData){

        if(currentWeek != null && currentDay !=null){
            determineExperienceLevelForCurrentWeek();
        }

        return generateListOfAllExercises(exerciseData).subList(currentExerciseOffsetAmount,
                currentExerciseOffsetAmount + NUMBER_OF_EXERCISES_IN_WORKOUT);
    }

    public List<Exercise> generateListOfAllExercises(String exerciseData){

        String currentEquipment="";
        String videoFileName = "";
        int index = 0;
        int id;
        Scanner scanner = new Scanner(exerciseData);

        Log.i(getClass().getSimpleName(), "generateListOfAllExercises: ");

        while (scanner.hasNextLine()) {
            Log.i(getClass().getSimpleName(), "=================================");

            currentName = scanner.nextLine();
            String[] durationLevels = scanner.nextLine().split(" ");
            if(currentExperienceLevel == 0){
                currentExperienceLevel = 2;
            }

            if(currentExperienceLevel == BEGINNER_EXPERIENCE_LEVEL)
                currentDuration = Integer.parseInt(durationLevels[1]);
            else if(currentExperienceLevel == INTERMEDIATE_EXPERIENCE_LEVEL)
                currentDuration = Integer.parseInt(durationLevels[2]);
            else // else must be advanced experience level
                currentDuration = Integer.parseInt(durationLevels[3]);

            currentEquipment = scanner.nextLine();

            videoFileName = scanner.nextLine();

            id = index+1;

            Log.i(getClass().getSimpleName(), "Equipment:" + currentEquipment);
            Log.i(getClass().getSimpleName(),"Equipment Available key: " + equipmentAvailable);

            switch (equipmentAvailable){
                case 0:
                    if(currentEquipment.equals(" none")){
                        Log.i(getClass().getSimpleName(), "No Equipment Selection...adding exercise...");
                        exerciseList.add(index,new Exercise(id, currentName, currentExperienceLevel,
                                currentDuration, currentEquipment, videoFileName));
                        index++;
                    }
                    break;
                case 1:
                    if(currentEquipment.equals(" Exercise Ball") ||
                            currentEquipment.equals(" none")){
                        Log.i(getClass().getSimpleName(), "Exercise Ball Selection" +
                                "...adding exercise...");
                        exerciseList.add(index,new Exercise(id, currentName, currentExperienceLevel,
                                currentDuration, currentEquipment, videoFileName));
                        index++;
                    }
                    break;
                case 2:
                    if(currentEquipment.equals(" Chin Up Bar") ||
                            currentEquipment.equals(" none")){
                        Log.i(getClass().getSimpleName(), "Chin Up Bar Selection" +
                                "...adding exercise...");
                        exerciseList.add(index,new Exercise(id, currentName, currentExperienceLevel,
                                currentDuration, currentEquipment, videoFileName));
                        index++;
                    }
                    break;
                case 3:
                    Log.i(getClass().getSimpleName(), "All Equipment Selection...adding exercise...");
                    exerciseList.add(index,new Exercise(id, currentName, currentExperienceLevel,
                            currentDuration, currentEquipment, videoFileName));
                    index++;
                    break;
                default:
                    break;
            }
        }
        scanner.close();

        return exerciseList;
    }

    public List<Exercise> createRandomWorkout(String exerciseData){

        exerciseList = generateListOfAllExercises(exerciseData);
        Random randomNumber = new Random();
        List<Exercise> randomWorkout = new ArrayList<Exercise>();
        for(int numberOfExercises = 1;numberOfExercises<=NUMBER_OF_EXERCISES_IN_WORKOUT;
            numberOfExercises++) {
            randomWorkout.add(exerciseList.get(randomNumber.nextInt(exerciseList.size())));
        }

        return randomWorkout;
    }

    private void determineExperienceLevelForCurrentWeek(){
        switch (currentWeek){
            case "Week 1":
                currentExperienceLevel =BEGINNER_EXPERIENCE_LEVEL;
                currentExerciseOffsetAmount = 0;
                break;
            case "Week 2":
                currentExperienceLevel =BEGINNER_EXPERIENCE_LEVEL;
                currentExerciseOffsetAmount = NUMBER_OF_EXERCISES_IN_WORKOUT;

                break;
            case "Week 3":
                currentExperienceLevel =BEGINNER_EXPERIENCE_LEVEL;
                currentExerciseOffsetAmount = 2*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 4":
                currentExperienceLevel =INTERMEDIATE_EXPERIENCE_LEVEL;
                currentExerciseOffsetAmount = 3*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 5":
                currentExperienceLevel =INTERMEDIATE_EXPERIENCE_LEVEL;
                currentExerciseOffsetAmount = 4*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 6":
                currentExperienceLevel =INTERMEDIATE_EXPERIENCE_LEVEL;
                if(equipmentAvailable==0) {
                    currentExerciseOffsetAmount = 4 * NUMBER_OF_EXERCISES_IN_WORKOUT;
                }
                else
                    currentExerciseOffsetAmount = 5*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 7":
                currentExperienceLevel =ADVANCED_EXPERIENCE_LEVEL;
                if(equipmentAvailable==0){
                    currentExerciseOffsetAmount = 5*NUMBER_OF_EXERCISES_IN_WORKOUT;
                    currentExerciseOffsetAmount-=2;
                } else if(equipmentAvailable==1){
                    currentExerciseOffsetAmount = 6*NUMBER_OF_EXERCISES_IN_WORKOUT;
                    currentExerciseOffsetAmount=currentExerciseOffsetAmount-3;
                } else
                    currentExerciseOffsetAmount = 6*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 8":
                currentExperienceLevel =ADVANCED_EXPERIENCE_LEVEL;
                if(equipmentAvailable==0){
                    currentExerciseOffsetAmount = 5*NUMBER_OF_EXERCISES_IN_WORKOUT;
                    currentExerciseOffsetAmount-=2;
                }
                else if(equipmentAvailable==1){
                    currentExerciseOffsetAmount = 6*NUMBER_OF_EXERCISES_IN_WORKOUT;
                    currentExerciseOffsetAmount=currentExerciseOffsetAmount-3;
                }
                else if(equipmentAvailable==2)
                    currentExerciseOffsetAmount = 6*NUMBER_OF_EXERCISES_IN_WORKOUT;
                else if(equipmentAvailable == 3)
                    currentExerciseOffsetAmount = (7*NUMBER_OF_EXERCISES_IN_WORKOUT) -1;
        }
    }

}
