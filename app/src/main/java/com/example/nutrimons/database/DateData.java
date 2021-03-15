package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Entity
public class DateData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name="breakfast")
    public final  List<String> breakfast;

    @ColumnInfo(name="lunch")
    public final  List<String> lunch;

    @ColumnInfo(name="dinner")
    public final  List<String> dinner;

    @ColumnInfo(name="snack")
    public final  List<String> snack;

    @ColumnInfo(name="water")
    public Double water;

    @ColumnInfo(name="exercise")
    public final  List<String> todayExercise;

    public DateData(@NonNull String date, List<String> breakfast, List<String> lunch, List<String> dinner, List<String> snack, List<String> todayExercise, Double water) {
        this.date = date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
        this.todayExercise = todayExercise;
        this.water = water;
    }
}