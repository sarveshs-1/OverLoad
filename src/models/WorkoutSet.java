package com.example.gymprogressiontracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * WorkoutSet represents a single exercise set in the database.
 * Each instance of this class will be a row in the 'workout_sets' table.
 */
@Entity(tableName = "workout_sets")
public class WorkoutSet {

    @PrimaryKey(autoGenerate = true)
    public int id; // Unique ID for each log entry

    public String dayOfWeek;    // The day this workout belongs to (e.g., "Monday")
    public String exerciseName; // Name of the exercise
    public int weight;         // Weight used in lbs
    public int reps;           // Number of repetitions
    public int sets;           // Number of sets (defaults to 1 in this app)
    
    // NEW: Variable to track if the workout set is finished/checked off
    private boolean isCompleted;

    // Updated Constructor to initialize the data
    public WorkoutSet(String dayOfWeek, String exerciseName, int weight, int reps, int sets) {
        this.dayOfWeek = dayOfWeek;
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.isCompleted = false; // Fresh sets start unchecked!
    }

    // Getter and Setter for isCompleted (needed for the CheckBox logic)
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
