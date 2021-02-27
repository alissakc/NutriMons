package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Hashtable;

@Entity//(indices = {@Index(value = {"user_name"}, unique = false)})
public class MacronutrientDRIs {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int groupID;

    @ColumnInfo(name = "age")
    public String age;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "babyStatus")
    public String babyStatus;

    @ColumnInfo(name = "water")
    public String water;

    @ColumnInfo(name = "carbohydrate")
    public String carbohydrate;

    @ColumnInfo(name = "fiber")
    public String fiber;

    @ColumnInfo(name = "fat")
    public String fat;

    @ColumnInfo(name = "linoleicAcid")
    public String linoleicAcid;

    @ColumnInfo(name = "alphaLinoleicAcid")
    public String alphaLinoleicAcid;

    @ColumnInfo(name = "protein")
    public String protein;

    public MacronutrientDRIs() {}

    public MacronutrientDRIs(ArrayList<String> str) {
        this.age = str.get(0);
        this.sex = str.get(1);
        this.babyStatus = str.get(2);
        this.water = str.get(3);
        this.carbohydrate = str.get(4);
        this.fiber = str.get(5);
        this.fat = str.get(6);
        this.linoleicAcid = str.get(7);
        this.alphaLinoleicAcid = str.get(8);
        this.protein = str.get(9);
    }

    public String toString()
    {
        return groupID + " " + age + " " + sex + " " + babyStatus + " " + water + " " + carbohydrate
                + " " + fiber + " " + fat + " " + linoleicAcid + " " + alphaLinoleicAcid + " " + protein;
    }

    public Hashtable<String, Float> toHashTable()
    {
        Hashtable<String, Float> hash = new Hashtable();
        hash.put("water", Float.valueOf(water));
        hash.put("carbohydrate", Float.valueOf(carbohydrate));
        hash.put("fiber", Float.valueOf(fiber));
        hash.put("fat", Float.valueOf(fat));
        hash.put("linoleicAcid", Float.valueOf(linoleicAcid));
        hash.put("alphaLinoleicAcid", Float.valueOf(alphaLinoleicAcid));
        hash.put("protein", Float.valueOf(protein));
        return hash;
    }
}
