package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity
public class DateData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name="breakfast")
    public List<String> breakfast;

    @ColumnInfo(name="lunch")
    public List<String> lunch;

    @ColumnInfo(name="dinner")
    public List<String> dinner;

    @ColumnInfo(name="snack")
    public List<String> snack;

    @ColumnInfo(name="water")
    public double water;

    @ColumnInfo(name="exercise")
    public List<String> todayExercise;

    public DateData(@NonNull String date, List<String> breakfast, List<String> lunch, List<String> dinner, List<String> snack, List<String> todayExercise, double water) {
        this.date = date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
        this.todayExercise = todayExercise;
        this.water = water;
    }
}