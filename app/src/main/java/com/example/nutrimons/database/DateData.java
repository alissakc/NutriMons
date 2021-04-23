package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class DateData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "breakfast")
    public final List<Meal> breakfast;

    @ColumnInfo(name = "lunch")
    public final List<Meal> lunch;

    @ColumnInfo(name = "dinner")
    public final List<Meal> dinner;

    @ColumnInfo(name = "snack")
    public final List<Meal> snack;

    @ColumnInfo(name = "exercise")
    public final List<String> todayExercise;


    @ColumnInfo(name = "calories")
    public float calories;

    @ColumnInfo(name = "water")
    public float water;

    @ColumnInfo(name = "water_unit")
    public String water_unit;

    @ColumnInfo(name = "protein")
    public float protein;

    @ColumnInfo(name = "carbohydrate")
    public float carbohydrate;

    @ColumnInfo(name = "sugar")
    public float sugar;

    @ColumnInfo(name = "fiber")
    public float fiber;

    @ColumnInfo(name = "cholesterol")
    public float cholesterol;

    @ColumnInfo(name = "saturatedFat")
    public float saturatedFat;

    @ColumnInfo(name = "monounsaturatedFat")
    public float monounsaturatedFat;

    @ColumnInfo(name = "polyunsaturatedFat")
    public float polyunsaturatedFat;

    @ColumnInfo(name = "transFat")
    public float transFat;

    @ColumnInfo(name = "vitaminA")
    public float vitaminA;

    @ColumnInfo(name = "vitaminC")
    public float vitaminC;

    @ColumnInfo(name = "vitaminD")
    public float vitaminD;

    @ColumnInfo(name = "sodium")
    public float sodium;

    @ColumnInfo(name = "potassium")
    public float potassium;

    @ColumnInfo(name = "calcium")
    public float calcium;

    @ColumnInfo(name = "iron")
    public float iron;

    @ColumnInfo(name = "coinsLeft")
    public int coinsLeft;

    public DateData(@NonNull String date, List<Meal> breakfast, List<Meal> lunch, List<Meal> dinner, List<Meal> snack, List<String> todayExercise, float water, String water_unit, int coinsLeft) {
        this.date = date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
        this.todayExercise = todayExercise;
        this.water = water;
        this.water_unit = water_unit;
        this.coinsLeft = coinsLeft;
    }

    public void aggregateNutrients() {
        calories = protein = carbohydrate = sugar = fiber = cholesterol = saturatedFat = monounsaturatedFat
                = polyunsaturatedFat = transFat = vitaminA = vitaminC = vitaminD = sodium = potassium
                = calcium = iron = 0f;
        if(!breakfast.isEmpty()) addNutrients(breakfast);
        if(!lunch.isEmpty()) addNutrients(lunch);
        if(!dinner.isEmpty()) addNutrients(dinner);
        if(!snack.isEmpty()) addNutrients(snack);
    }

    private void addNutrients(List<Meal> meals) {
        for (Meal m : meals) {
            if(m != null){
                this.calories += m.servingsEaten * m.calories;
                this.protein += m.servingsEaten * m.protein;
                this.carbohydrate += m.servingsEaten * m.carbohydrate;
                this.sugar += m.servingsEaten * m.sugar;
                this.fiber += m.servingsEaten * m.fiber;
                this.cholesterol += m.servingsEaten * m.cholesterol;
                this.saturatedFat += m.servingsEaten * m.saturatedFat;
                this.monounsaturatedFat += m.servingsEaten * m.monounsaturatedFat;
                this.polyunsaturatedFat += m.servingsEaten * m.polyunsaturatedFat;
                this.transFat += m.servingsEaten * m.transFat;
                this.vitaminA += m.servingsEaten * m.vitaminA;
                this.vitaminC += m.servingsEaten * m.vitaminC;
                this.vitaminD += m.servingsEaten * m.vitaminD;
                this.sodium += m.servingsEaten * m.sodium;
                this.potassium += m.servingsEaten * m.potassium;
                this.calcium += m.servingsEaten * m.calcium;
                this.iron += m.servingsEaten * m.iron;
            }
        }
    }

    @TypeConverters(Converters.class)
    public List<Meal> getBreakfast() {
        return breakfast;
    }

    public List<Meal> getLunch() {
        return lunch;
    }

    public List<Meal> getDinner() {
        return dinner;
    }

    public List<Meal> getSnack() {
        return snack;
    }

    public ArrayList<String> nutrientsToStringList() {
        ArrayList<String> dailyList = new ArrayList<>();

        dailyList.add("Calories: " + calories + "kcal");
        if(water_unit == null){
            water_unit = "oz";
        }
        if(water_unit.equals("ml"))
        {
            dailyList.add("Water: " + water / 1000 + "L");
        }
        else if(water_unit.equals("oz"))
        {
            dailyList.add("Water: " + water / 33.814 + "L");
        }
        dailyList.add("Protein: " + protein + "g");
        dailyList.add("Carbohydrate: " + carbohydrate + "g");
        dailyList.add("Sugar: " + sugar + "g");
        dailyList.add("Fiber: " + fiber + "g");
        dailyList.add("Cholesterol: " + cholesterol + "mg");
        dailyList.add("Saturated Fat: " + saturatedFat + "g");
        dailyList.add("Unsaturated Fat: " + (monounsaturatedFat + polyunsaturatedFat) + "g");
        dailyList.add("Trans Fat: " + transFat + "g");
        dailyList.add("Vitamin A: " + vitaminA + "μg");
        dailyList.add("Vitamin C: " + vitaminC + "mg");
        dailyList.add("Vitamin D: " + vitaminD + "μg");
        dailyList.add("Sodium: " + sodium + "g");
        dailyList.add("Potassium: " + potassium + "g");
        dailyList.add("Calcium: " + calcium + "mg");
        dailyList.add("Iron: " + iron + "mg");

        return dailyList;
    }

    public ArrayList<Float> nutrientsToFloatList() {
        ArrayList<Float> dailyList = new ArrayList<>();

        dailyList.add(calories);
        dailyList.add(water);
        dailyList.add(protein);
        dailyList.add(carbohydrate);
        dailyList.add(sugar);
        dailyList.add(fiber);
        dailyList.add(cholesterol);
        dailyList.add(saturatedFat);
        dailyList.add((monounsaturatedFat + polyunsaturatedFat));
        dailyList.add(transFat);
        dailyList.add(vitaminA);
        dailyList.add(vitaminC);
        dailyList.add(vitaminD);
        dailyList.add(sodium);
        dailyList.add(potassium);
        dailyList.add(calcium);
        dailyList.add(iron);

        return dailyList;
    }


   /* public ArrayList<String> breakfastToListString()
    {
        ArrayList<String> mealList = new ArrayList<String>();

        for(Meal m : breakfast)
        {
            mealList.add(m.mealName);
        }

        return mealList;
    }

    public ArrayList<String> lunchToListString()
    {
        ArrayList<String> mealList = new ArrayList<String>();

        for(Meal m : lunch)
        {
            mealList.add(m.mealName);
        }

        return mealList;
    }

    public ArrayList<String> dinnerToListString()
    {
        ArrayList<String> mealList = new ArrayList<String>();

        for(Meal m : dinner)
        {
            mealList.add(m.mealName);
        }

        return mealList;
    }

    public ArrayList<String> snackToListString()
    {
        ArrayList<String> mealList = new ArrayList<String>();

        for(Meal m : snack)
        {
            mealList.add(m.mealName);
        }

        return mealList;
    }*/
}