package com.jdblogs.gymlessabs.datahandling;


import com.jdblogs.gymlessabs.models.Exercise;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jonathandeehan on 17/06/2017.
 */

public class ShuffleWorkout {

    private static final int NUMBER_OF_EXERCISES_IN_SHUFFLE_WORKOUT = 7;

    public List<Exercise> generateShuffleWorkout(String exerciseData){
        WorkoutGenerator workoutGenerator = new WorkoutGenerator();
        List<Exercise> allExercises = workoutGenerator.generateListOfAllExercises(exerciseData);
        List<Exercise> shuffleWorkout = new ArrayList<Exercise>();
        Random random = new Random(allExercises.size());

        for(int index=0;index<NUMBER_OF_EXERCISES_IN_SHUFFLE_WORKOUT;index++){
            shuffleWorkout.add(allExercises.get(random.nextInt()));
        }

        return shuffleWorkout;

    }

}
