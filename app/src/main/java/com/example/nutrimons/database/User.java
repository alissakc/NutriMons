package com.example.nutrimons.database;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Index;

import com.example.nutrimons.R;

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
    public String nutriCoins;

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
}
