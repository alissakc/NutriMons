package com.BAMM.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Hashtable;

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

    public Hashtable<String, Float> toHashTable()
    {
        Hashtable<String, Float> hash = new Hashtable();
        hash.put("fatMin", Float.valueOf(fat.split("-")[0]));
        hash.put("fatMax", Float.valueOf(fat.split("-")[1]));
        hash.put("linoleicAcidMin", Float.valueOf(linoleicAcid.split("-")[0]));
        hash.put("linoleicAcidMax", Float.valueOf(linoleicAcid.split("-")[1]));
        hash.put("alphaLinoleicAcidMin", Float.valueOf(alphaLinoleicAcid.split("-")[0]));
        hash.put("alphaLinoleicAcidMax", Float.valueOf(alphaLinoleicAcid.split("-")[1]));
        hash.put("carbohydrateMin", Float.valueOf(carbohydrate.split("-")[0]));
        hash.put("carbohydrateMax", Float.valueOf(carbohydrate.split("-")[1]));
        hash.put("proteinMin", Float.valueOf(protein.split("-")[0]));
        hash.put("proteinMax", Float.valueOf(protein.split("-")[1]));
        return hash;
    }
}
