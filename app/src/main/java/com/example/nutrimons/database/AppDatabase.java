package com.example.nutrimons.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Meal and Exercise are classes annotated with @Entity
@Database(entities = {Meal.class, Exercise.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // database instance
    private static AppDatabase instance;
    // database name
    private static final String DATABASE_NAME = "main";

    public static synchronized AppDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    // MealDao is a class annotated with @Dao
    public abstract MealDao mealDao();
    // ExerciseDao is a class annotated with @Dao
    public abstract ExerciseDao exerciseDao();

}
