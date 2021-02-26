package com.example.nutrimons.database;

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

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Ignore
    public User(String email, String password, String name, String birthday, String financialSource, String financialHistory, String financialPlan, String nutriCoins, String age, String sex, String weight, String height, String ethnicity, String healthHistory, String healthGoals, String profileFocus, String activityLevel) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.financialSource = financialSource;
        this.financialHistory = financialHistory;
        this.financialPlan = financialPlan;
        this.nutriCoins = nutriCoins;
        this.age = age;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.ethnicity = ethnicity;
        this.healthHistory = healthHistory;
        this.healthGoals = healthGoals;
        this.profileFocus = profileFocus;
        this.activityLevel = activityLevel;
    }

    public String toString()
    {
        return userID + name + email + password + birthday + financialSource + financialHistory
                + financialPlan + nutriCoins + age + sex + weight + height + ethnicity
                + healthHistory + healthGoals + profileFocus + activityLevel;
    }
}
