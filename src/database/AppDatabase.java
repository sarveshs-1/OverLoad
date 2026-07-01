package com.example.gymprogressiontracker;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * This class sets up the actual database file on your phone.
 */
@Database(entities = {WorkoutSet.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WorkoutDao workoutDao(); // Link to our commands

    private static AppDatabase instance;

    // This ensures only ONE connection to the database is open at a time
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "gym_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Allows us to keep the code simple
                    .build();
        }
        return instance;
    }
}
