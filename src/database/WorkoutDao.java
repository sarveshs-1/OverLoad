package com.example.gymprogressiontracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;


 */
@Dao
public interface WorkoutDao {

    @Insert
    void insertWorkout(WorkoutSet workoutSet); // Adds a new log to the database

    @androidx.room.Update
    void updateWorkout(WorkoutSet workoutSet); // Updates an existing log

    @androidx.room.Delete
    void deleteWorkout(WorkoutSet workoutSet); // Removes a log

    @Query("SELECT * FROM workout_sets WHERE dayOfWeek = :day")
    List<WorkoutSet> getWorkoutsForDay(String day); // Gets all logs for a specific day

    @Query("SELECT * FROM workout_sets ORDER BY timestamp DESC")
    List<WorkoutSet> getAllWorkoutsSorted(); // For the History screen
}
