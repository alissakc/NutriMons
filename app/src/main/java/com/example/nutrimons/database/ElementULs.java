package com.example.nutrimons.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Hashtable;

@Entity//(indices = {@Index(value = {"user_name"}, unique = false)})
public class ElementULs {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int groupID;

    @ColumnInfo(name = "age")
    public String age;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "babyStatus")
    public String babyStatus;

    @ColumnInfo(name = "arsenic")
    public String arsenic;

    @ColumnInfo(name = "boron")
    public String boron;

    @ColumnInfo(name = "calcium")
    public String calcium;

    @ColumnInfo(name = "chromium")
    public String chromium;

    @ColumnInfo(name = "copper")
    public String copper;

    @ColumnInfo(name = "fluoride")
    public String fluoride;

    @ColumnInfo(name = "iodine")
    public String iodine;

    @ColumnInfo(name = "iron")
    public String iron;

    @ColumnInfo(name = "magnesium")
    public String magnesium;

    @ColumnInfo(name = "manganese")
    public String manganese;

    @ColumnInfo(name = "molybdenum")
    public String molybdenum;

    @ColumnInfo(name = "nickel")
    public String nickel;

    @ColumnInfo(name = "phosphorous")
    public String phosphorous;

    @ColumnInfo(name = "selenium")
    public String selenium;

    @ColumnInfo(name = "silicon")
    public String silicon;

    @ColumnInfo(name = "vanadium")
    public String vanadium;

    @ColumnInfo(name = "zinc")
    public String zinc;

    @ColumnInfo(name = "sodium")
    public String sodium;

    @ColumnInfo(name = "chloride")
    public String chloride;

    public ElementULs() {}

    public ElementULs(ArrayList<String> str) {
        this.age = str.get(0);
        this.sex = str.get(1);
        this.babyStatus = str.get(2);
        this.arsenic = str.get(3);
        this.boron = str.get(4);
        this.calcium = str.get(5);
        this.chromium = str.get(6);
        this.copper = str.get(7);
        this.fluoride = str.get(8);
        this.iodine = str.get(9);
        this.iron = str.get(10);
        this.magnesium = str.get(11);
        this.manganese = str.get(12);
        this.molybdenum = str.get(13);
        this.nickel = str.get(14);
        this.phosphorous = str.get(15);
        this.selenium = str.get(16);
        this.silicon = str.get(17);
        this.vanadium = str.get(18);
        this.zinc = str.get(19);
        this.sodium = str.get(20);
        this.chloride = str.get(21);
    }

    public String toString()
    {
        return groupID + " " + age + " " + sex + " " + babyStatus + " " + arsenic + " " + boron
                +  " " + calcium + " " + chromium + " " + copper + " " + fluoride + " " + iodine
                + " " + iron + " " + magnesium + " " + manganese + " " + molybdenum + " " + nickel
                + " " + phosphorous + " " + selenium + " " + silicon + " " + vanadium + " "
                + zinc + " " + sodium + " " + chloride;
    }

    public Hashtable<String, Float> toHashTable()
    {
        Hashtable<String, Float> hash = new Hashtable();
        hash.put("arsenic", Float.valueOf(arsenic));
        hash.put("boron", Float.valueOf(boron));
        hash.put("calcium", Float.valueOf(calcium));
        hash.put("chromium", Float.valueOf(chromium));
        hash.put("copper", Float.valueOf(copper));
        hash.put("fluoride", Float.valueOf(fluoride));
        hash.put("iodine", Float.valueOf(iodine));
        hash.put("iron", Float.valueOf(iron));
        hash.put("magnesium", Float.valueOf(magnesium));
        hash.put("manganese", Float.valueOf(manganese));
        hash.put("molybdenum", Float.valueOf(molybdenum));
        hash.put("nickel", Float.valueOf(nickel));
        hash.put("phosphorous", Float.valueOf(phosphorous));
        hash.put("selenium", Float.valueOf(selenium));
        hash.put("silicon", Float.valueOf(silicon));
        hash.put("vanadium", Float.valueOf(vanadium));
        hash.put("zinc", Float.valueOf(zinc));
        hash.put("sodium", Float.valueOf(sodium));
        hash.put("chloride", Float.valueOf(chloride));
        return hash;
    }
}
