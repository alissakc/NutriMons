package com.BAMM.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Hashtable;

@Entity//(indices = {@Index(value = {"user_name"}, unique = false)})
public class VitaminDRIs {
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

    public VitaminDRIs() {};

    public VitaminDRIs(ArrayList<String> str) {
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
    }

    public String toString()
    {
        return groupID + " " + age + " " + sex + " " + babyStatus + " " + vitaminA + " " + vitaminC
                +  " " + vitaminD + " " + vitaminE + " " + vitaminK + " " + thiamin + " " + riboflavin
                + " " + niacin + " " + vitaminB6 + " " + folate + " " + vitaminB12 + " " + pantothenicAcid
                + " " + biotin + " " + choline;
    }

    public Hashtable<String, Float> toHashTable()
    {
        Hashtable<String, Float> hash = new Hashtable();
        hash.put("vitaminA", Float.valueOf(vitaminA));
        hash.put("vitaminC", Float.valueOf(vitaminC));
        hash.put("vitaminD", Float.valueOf(vitaminD));
        hash.put("vitaminE", Float.valueOf(vitaminE));
        hash.put("vitaminK", Float.valueOf(vitaminK));
        hash.put("thiamin", Float.valueOf(thiamin));
        hash.put("riboflavin", Float.valueOf(riboflavin));
        hash.put("niacin", Float.valueOf(niacin));
        hash.put("vitaminB6", Float.valueOf(vitaminB6));
        hash.put("folate", Float.valueOf(folate));
        hash.put("vitaminB12", Float.valueOf(vitaminB12));
        hash.put("pantothenicAcid", Float.valueOf(pantothenicAcid));
        hash.put("biotin", Float.valueOf(biotin));
        hash.put("choline", Float.valueOf(choline));
        return hash;
    }
}
