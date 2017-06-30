package com.jdblogs.gymlessabs.models;

/**
 * Created by jonathandeehan on 13/06/2017.
 */

public class Exercise {

    private int exerciseId;
    private String name;
    private int experienceLevel;
    private int duration;
    private String equipment;
    private String videoFileName;

    public Exercise(int exerciseId,String name, int experienceLevel, int duration,
                    String equipment, String videoFileName){
        this.exerciseId = exerciseId;
        this.name = name;
        this.experienceLevel = experienceLevel;
        this.duration = duration;
        this.equipment = equipment;
        this.videoFileName = videoFileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getVideoFileName() {
        return videoFileName;
    }

    public void setVideoFileName(String videoFileName) {
        this.videoFileName = videoFileName;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

}
