package com.jdblogs.gymlessabs.datahandling;

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
    private List<Exercise> exerciseList = new ArrayList<Exercise>();

    public WorkoutGenerator(String currentWeek, String currentDay){
        this.currentWeek = currentWeek;
        this.currentDay = currentDay;
    }

    public WorkoutGenerator(){}

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

        while (scanner.hasNextLine()) {
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

            // uncomment when you add all the video file names to the strings.xml
            videoFileName = scanner.nextLine();

            id = index+1;
            exerciseList.add(index,new Exercise(id, currentName, currentExperienceLevel,
                    currentDuration, currentEquipment, videoFileName));
            index++;
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
                currentExperienceLevel =1;
                currentExerciseOffsetAmount = 0;
                break;
            case "Week 2":
                currentExperienceLevel =1;
                currentExerciseOffsetAmount = NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 3":
                currentExperienceLevel =1;
                currentExerciseOffsetAmount = 2*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 4":
                currentExperienceLevel =2;
                currentExerciseOffsetAmount = 3* NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 5":
                currentExperienceLevel =2;
                currentExerciseOffsetAmount = 4*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 6":
                currentExperienceLevel =2;
                currentExerciseOffsetAmount = 5*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 7":
                currentExperienceLevel =3;
                currentExerciseOffsetAmount = 6*NUMBER_OF_EXERCISES_IN_WORKOUT;
                break;
            case "Week 8":
                currentExperienceLevel =3;
                currentExerciseOffsetAmount = 6*NUMBER_OF_EXERCISES_IN_WORKOUT;
        }
    }

}
