package com.example.nutrimons.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

// Meal, Exercise, and User are classes annotated with @Entity
@Database(entities = {Meal.class, Exercise.class, User.class, DateData.class, ElementDRIs.class, ElementULs.class, MacronutrientDRIs.class,
  MacronutrientRanges.class, VitaminDRIs.class, VitaminULs.class}, version = 1)
@TypeConverters({Converters.class})
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
    // UserDao is a class annotated with @Dao
    public abstract UserDao userDao();
    // DataDataDao is a class annotated with @Dao
    public abstract DateDataDao dateDataDao();

    public abstract ElementDRIsDAO elementDRIsDAO();
    public abstract ElementULsDAO elementULsDAO();
    public abstract MacronutrientDRIsDAO macronutrientDRIsDAO();
    public abstract MacronutrientRangesDAO macronutrientRangesDAO();
    public abstract VitaminDRIsDAO vitaminDRIsDAO();
    public abstract VitaminULsDAO vitaminULsDAO();
}
