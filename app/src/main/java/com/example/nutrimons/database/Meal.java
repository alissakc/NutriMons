package com.example.nutrimons.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Meal {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    public int mealID;

    @ColumnInfo(name = "meal_name")
    public String mealName;

    @ColumnInfo(name = "serving_size")
    public int servingSize;

    @ColumnInfo(name = "serving_weight")
    public int servingWeight;

    @ColumnInfo(name = "calories_per_serving")
    public int caloriesPerServing;

    public Meal(String mealName, int servingSize, int servingWeight, int caloriesPerServing) {
        this.mealName = mealName;
        this.servingSize = servingSize;
        this.servingWeight = servingWeight;
        this.caloriesPerServing = caloriesPerServing;
    }
}
