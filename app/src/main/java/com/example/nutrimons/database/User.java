package com.example.nutrimons.database;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Index;

import com.example.nutrimons.R;

import java.util.ArrayList;
import java.util.List;

@Entity//(indices = {@Index(value = {"user_name"}, unique = false)})
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int userID;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "birthday")
    public String birthday;

    @ColumnInfo(name = "financialSource")
    public String financialSource;

    @ColumnInfo(name = "financialHistory")
    public String financialHistory;

    @ColumnInfo(name = "financialPlan")
    public String financialPlan;

    @ColumnInfo(name = "nutriCoins")
    public int nutriCoins;

    @ColumnInfo(name = "age")
    public String age;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "weight")
    public String weight;

    @ColumnInfo(name = "height")
    public String height;

    @ColumnInfo(name = "ethnicity")
    public String ethnicity;

    @ColumnInfo(name = "healthHistory")
    public String healthHistory;

    @ColumnInfo(name = "healthGoals")
    public String healthGoals;

    @ColumnInfo(name = "profileFocus")
    public String profileFocus;

    @ColumnInfo(name = "activityLevel")
    public String activityLevel;

    @ColumnInfo(name = "calories")
    public String calories;

    @ColumnInfo(name = "water")
    public String water;

    @ColumnInfo(name = "proteinDRI")
    public String proteinDRI;

    @ColumnInfo(name = "proteinUL")
    public String proteinUL;

    @ColumnInfo(name = "carbsDRI")
    public String carbsDRI;

    @ColumnInfo(name = "carbsUL")
    public String carbsUL;

    @ColumnInfo(name = "sugarDRI")
    public String sugarDRI;

    @ColumnInfo(name = "sugarUL")
    public String sugarUL;

    @ColumnInfo(name = "fiberDRI")
    public String fiberDRI;

    @ColumnInfo(name = "fatsDRI")
    public String fatsDRI;

    @ColumnInfo(name = "fatsUL")
    public String fatsUL;

    @ColumnInfo(name = "cholesterolDRI")
    public String cholesterolDRI;

    @ColumnInfo(name = "cholesterolUL")
    public String cholesterolUL;

    @ColumnInfo(name = "saturatedFatsDRI")
    public String saturatedFatsDRI;

    @ColumnInfo(name = "saturatedFatsUL")
    public String saturatedFatsUL;

    @ColumnInfo(name = "unsaturatedFatsDRI")
    public String unsaturatedFatsDRI;

    @ColumnInfo(name = "unsaturatedFatsUL")
    public String unsaturatedFatsUL;

    @ColumnInfo(name = "transFatsDRI")
    public String transFatsDRI;

    @ColumnInfo(name = "transFatsUL")
    public String transFatsUL;

    @ColumnInfo(name = "vitaminADRI")
    public String vitaminADRI;

    @ColumnInfo(name = "vitaminAUL")
    public String vitaminAUL;

    @ColumnInfo(name = "vitaminCDRI")
    public String vitaminCDRI;

    @ColumnInfo(name = "vitaminCUL")
    public String vitaminCUL;

    @ColumnInfo(name = "vitaminDDRI")
    public String vitaminDDRI;

    @ColumnInfo(name = "vitaminDUL")
    public String vitaminDUL;

    @ColumnInfo(name = "sodiumDRI")
    public String sodiumDRI;

    @ColumnInfo(name = "sodiumUL")
    public String sodiumUL;

    @ColumnInfo(name = "potassiumDRI")
    public String potassiumDRI;

    @ColumnInfo(name = "calciumDRI")
    public String calciumDRI;

    @ColumnInfo(name = "calciumUL")
    public String calciumUL;

    @ColumnInfo(name = "ironDRI")
    public String ironDRI;

    @ColumnInfo(name = "ironUL")
    public String ironUL;

    @ColumnInfo(name = "profilePicture")
    public String profilePicture;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.nutriCoins = 0;
    }

    public String toString()
    {
        return userID + " " + name + " " + email + " " + password + " " + birthday + " " + financialSource
                + " " + financialHistory + " " + financialPlan + " " + nutriCoins + " " + age + " "
                + sex + " " + weight + " " + height + " " + ethnicity + " " + healthHistory + " "
                + healthGoals + " " + profileFocus + " " + activityLevel + " " + calories + " " + water
                + " " + proteinDRI + " " + proteinUL + " " + carbsDRI + " " + carbsUL + " " + sugarDRI
                + " " + sugarUL + " " + fiberDRI + " " + fatsDRI + " " + fatsUL + " " + cholesterolDRI
                + " " + cholesterolUL + " " + saturatedFatsDRI + " " + saturatedFatsUL + " "
                + unsaturatedFatsDRI + " " + unsaturatedFatsUL + " " + transFatsDRI + " " + transFatsUL
                + " " + vitaminADRI + " " + vitaminAUL + " " + vitaminCDRI + " " + vitaminCUL + " "
                + " " + vitaminDDRI + " " + vitaminDUL + " " + sodiumDRI + " " + sodiumUL + " "
                + potassiumDRI + " " + calciumDRI + " " + calciumUL + " " + ironDRI + " " + ironUL;
    }
    
    public List<Float> DRIToFloatList()
    {
        ArrayList<Float> dailyList = new ArrayList<>();

        dailyList.add(Float.parseFloat(calories));
        dailyList.add(Float.parseFloat(water));
        dailyList.add(Float.parseFloat(proteinDRI));
        dailyList.add(Float.parseFloat(carbsDRI));
        dailyList.add(Float.parseFloat(sugarDRI));
        dailyList.add(Float.parseFloat(fiberDRI));
        dailyList.add(Float.parseFloat(cholesterolDRI));
        dailyList.add(Float.parseFloat(saturatedFatsDRI));
        dailyList.add(Float.parseFloat(unsaturatedFatsDRI));
        dailyList.add(Float.parseFloat(transFatsDRI));
        dailyList.add(Float.parseFloat(vitaminADRI));
        dailyList.add(Float.parseFloat(vitaminCDRI));
        dailyList.add(Float.parseFloat(vitaminDDRI));
        dailyList.add(Float.parseFloat(sodiumDRI));
        dailyList.add(Float.parseFloat(potassiumDRI));
        dailyList.add(Float.parseFloat(calciumDRI));
        dailyList.add(Float.parseFloat(ironDRI));

        return dailyList;
    }

    public List<Float> ULToFloatList()
    {
        ArrayList<Float> dailyList = new ArrayList<>();

        dailyList.add(Float.parseFloat(calories));
        dailyList.add(Float.MAX_VALUE);//null
        dailyList.add(Float.parseFloat(proteinUL));
        dailyList.add(Float.parseFloat(carbsUL));
        dailyList.add(Float.parseFloat(sugarUL));
        dailyList.add(Float.MAX_VALUE);//null
        dailyList.add(Float.parseFloat(cholesterolUL));
        dailyList.add(Float.parseFloat(saturatedFatsUL));
        dailyList.add(Float.parseFloat(unsaturatedFatsUL));
        dailyList.add(Float.parseFloat(transFatsUL));
        dailyList.add(Float.parseFloat(vitaminAUL));
        dailyList.add(Float.parseFloat(vitaminCUL));
        dailyList.add(Float.parseFloat(vitaminDUL));
        dailyList.add(Float.parseFloat(sodiumUL));
        dailyList.add(Float.MAX_VALUE);//null
        dailyList.add(Float.parseFloat(calciumUL));
        dailyList.add(Float.parseFloat(ironUL));

        return dailyList;
    }
}
