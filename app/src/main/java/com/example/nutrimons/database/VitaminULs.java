package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity//(indices = {@Index(value = {"user_name"}, unique = false)})
public class VitaminULs {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int groupID;

    @ColumnInfo(name = "age")
    public String age;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "babyStatus")
    public String babyStatus;

    @ColumnInfo(name = "vitaminA")
    public String vitaminA;

    @ColumnInfo(name = "vitaminC")
    public String vitaminC;

    @ColumnInfo(name = "vitaminD")
    public String vitaminD;

    @ColumnInfo(name = "vitaminE")
    public String vitaminE;

    @ColumnInfo(name = "vitaminK")
    public String vitaminK;

    @ColumnInfo(name = "thiamin")
    public String thiamin;

    @ColumnInfo(name = "riboflavin")
    public String riboflavin;

    @ColumnInfo(name = "niacin")
    public String niacin;

    @ColumnInfo(name = "vitaminB6")
    public String vitaminB6;

    @ColumnInfo(name = "folate")
    public String folate;

    @ColumnInfo(name = "vitaminB12")
    public String vitaminB12;

    @ColumnInfo(name = "pantothenicAcid")
    public String pantothenicAcid;

    @ColumnInfo(name = "biotin")
    public String biotin;

    @ColumnInfo(name = "choline")
    public String choline;

    @ColumnInfo(name = "carotenoids")
    public String carotenoids;

    public VitaminULs() {}

    public VitaminULs(ArrayList<String> str) {
        this.age = str.get(0);
        this.sex = str.get(1);
        this.babyStatus = str.get(2);
        this.vitaminA = str.get(3);
        this.vitaminC = str.get(4);
        this.vitaminD = str.get(5);
        this.vitaminE = str.get(6);
        this.vitaminK = str.get(7);
        this.thiamin = str.get(8);
        this.riboflavin = str.get(9);
        this.niacin = str.get(10);
        this.vitaminB6 = str.get(11);
        this.folate = str.get(12);
        this.vitaminB12 = str.get(13);
        this.pantothenicAcid = str.get(14);
        this.biotin = str.get(15);
        this.choline = str.get(16);
        this.carotenoids = str.get(17);
    }

    public String toString()
    {
        return groupID + " " + age + " " + sex + " " + babyStatus + " " + vitaminA + " " + vitaminC
                +  " " + vitaminD + " " + vitaminE + " " + vitaminK + " " + thiamin + " " + riboflavin
                + " " + niacin + " " + vitaminB6 + " " + folate + " " + vitaminB12 + " " + pantothenicAcid
                + " " + biotin + " " + choline + " " + carotenoids;
    }
}
