package com.BAMM.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int exerciseID;

    @ColumnInfo(name = "exercise_name")
    public String exerciseName;

    @ColumnInfo(name = "calories_per_unit")
    public int caloriesPerUnit;

    @ColumnInfo(name = "unit_name")
    public String unitName;

    @ColumnInfo(name="duration")
    public float duration;

    @Ignore
    public Exercise(String exerciseName, int caloriesPerUnit, String unitName) {
        this.exerciseName = exerciseName;
        this.caloriesPerUnit = caloriesPerUnit;
        this.unitName = unitName;
    }

    public Exercise(String exerciseName, int caloriesPerUnit, String unitName, float duration) {
        this.exerciseName = exerciseName;
        this.caloriesPerUnit = caloriesPerUnit;
        this.unitName = unitName;
        this.duration = duration;
    }
}