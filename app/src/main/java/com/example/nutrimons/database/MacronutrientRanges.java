package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity//(indices = {@Index(value = {"user_name"}, unique = false)})
public class MacronutrientRanges {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int groupID;

    @ColumnInfo(name = "age")
    public String age;

    @ColumnInfo(name = "fat")
    public String fat;

    @ColumnInfo(name = "linoleicAcid")
    public String linoleicAcid;

    @ColumnInfo(name = "alphaLinoleicAcid")
    public String alphaLinoleicAcid;

    @ColumnInfo(name = "carbohydrate")
    public String carbohydrate;

    @ColumnInfo(name = "protein")
    public String protein;

    public MacronutrientRanges() {}

    public MacronutrientRanges(ArrayList<String> str) {
        this.age = str.get(0);
        this.fat = str.get(1);
        this.linoleicAcid = str.get(2);
        this.alphaLinoleicAcid = str.get(3);
        this.carbohydrate = str.get(4);
        this.protein = str.get(5);
    }

    public String toString()
    {
        return groupID + " " + age + " " + fat + " " + linoleicAcid + " " + alphaLinoleicAcid + " "
                + carbohydrate + " " + protein;
    }
}
