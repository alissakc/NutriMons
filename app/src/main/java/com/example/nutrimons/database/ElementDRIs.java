package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity//(indices = {@Index(value = {"user_name"}, unique = false)})
public class ElementDRIs {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int groupID;

    @ColumnInfo(name = "age")
    public String age;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "babyStatus")
    public String babyStatus;

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

    @ColumnInfo(name = "phosphorous")
    public String phosphorous;

    @ColumnInfo(name = "selenium")
    public String selenium;

    @ColumnInfo(name = "zinc")
    public String zinc;

    @ColumnInfo(name = "potassium")
    public String potassium;

    @ColumnInfo(name = "sodium")
    public String sodium;

    @ColumnInfo(name = "chloride")
    public String chloride;

    public ElementDRIs() {}

    public ElementDRIs(ArrayList<String> str) {
        this.age = str.get(0);
        this.sex = str.get(1);
        this.babyStatus = str.get(2);
        this.calcium = str.get(3);
        this.chromium = str.get(4);
        this.copper = str.get(5);
        this.fluoride = str.get(6);
        this.iodine = str.get(7);
        this.iron = str.get(8);
        this.magnesium = str.get(9);
        this.manganese = str.get(10);
        this.molybdenum = str.get(11);
        this.phosphorous = str.get(12);
        this.selenium = str.get(13);
        this.zinc = str.get(14);
        this.potassium = str.get(15);
        this.sodium = str.get(16);
        this.chloride = str.get(17);
    }

    public String toString()
    {
        return groupID + " " + age + " " + sex + " " + babyStatus + " " + calcium + " " + chromium
                + " " + copper + " " + fluoride + " " + iodine + " " + iron + " " + magnesium
                + " " + manganese + " " + molybdenum + " " + phosphorous + " " + selenium + " "
                + zinc + " " + potassium + " " + sodium + " " + chloride;
    }
}
