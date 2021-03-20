package com.example.nutrimons.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/* prevents the table from having two rows that contain the same set of values for the mealName,
servingSize, servingWeight, and caloriesPerServing columns  */
@Entity(indices = {@Index(value = {"meal_name", "serving_size", "serving_weight", "calories_per_serving"},
        unique = true)})
public class Meal {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    public Integer mealID;

    @ColumnInfo(name = "meal_name")
    public String mealName;

    @ColumnInfo(name = "serving_size")
    public int servingSize;

    @ColumnInfo(name = "serving_weight")
    public int servingWeight;

    @ColumnInfo(name = "calories_per_serving")
    public int caloriesPerServing;

    @ColumnInfo(name = "calories")
    public float calories;

    @ColumnInfo(name = "water")
    public float water;

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

    public Meal(String mealName, int servingSize, int servingWeight, int caloriesPerServing) {
        this.mealName = mealName;
        this.servingSize = servingSize;
        this.servingWeight = servingWeight;
        this.caloriesPerServing = caloriesPerServing;
    }

    @Ignore
    public Meal(String mealName, int servingSize, int servingWeight, int caloriesPerServing,
                float calories, float water, float protein, float carbohydrate, float sugar, float fiber,
                float cholesterol, float saturatedFat, float monounsaturatedFat, float polyunsaturatedFat,
                float transFat, float vitaminA, float vitaminC, float vitaminD, float sodium, float potassium,
                float calcium, float iron)
    {
        this.mealName = mealName;
        this.servingSize = servingSize;
        this.servingWeight = servingWeight;
        this.caloriesPerServing = caloriesPerServing;
        this.calories = calories;
        this.water = water;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.sugar = sugar;
        this.fiber = fiber;
        this.cholesterol = cholesterol;
        this.saturatedFat = saturatedFat;
        this.monounsaturatedFat = monounsaturatedFat;
        this.polyunsaturatedFat = polyunsaturatedFat;
        this.transFat = transFat;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.vitaminD = vitaminD;
        this.sodium = sodium;
        this.potassium = potassium;
        this.calcium = calcium;
        this.iron = iron;
    }

    public void setFieldFromString(String field, float value)
    {
        switch(field)
        {
            case "calories":
                calories = value;
                break;
            case "water":
                water = value;
                break;
            case "protein":
                protein = value;
                break;
            case "carbohydrate":
                carbohydrate = value;
                break;
            case "sugar":
                sugar = value;
                break;
            case "fiber":
                fiber = value;
                break;
            case "cholesterol":
                cholesterol = value;
                break;
            case "saturatedFat":
                saturatedFat = value;
                break;
            case "monounsaturatedFat":
                monounsaturatedFat = value;
                break;
            case "polyunsaturatedFat":
                polyunsaturatedFat = value;
                break;
            case "transFat":
                transFat = value;
                break;
            case "vitaminA":
                vitaminA = value;
                break;
            case "vitaminC":
                vitaminC = value;
                break;
            case "vitaminD":
                vitaminD = value;
                break;
            case "sodium":
                sodium = value;
                break;
            case "potassium":
                potassium = value;
                break;
            case "calcium":
                calcium = value;
                break;
            case "iron":
                iron = value;
                break;
            default:
                Log.e("no attribute", "no such field exists");
                break;
        }
    }

    /*public void setAdvFields(float calories, float water, float protein, float carbohydrate, float sugar,
                             float fiber, float cholesterol, float saturatedFat, float monounsaturatedFat,
                             float polyunsaturatedFat, float transFat, float vitaminA, float vitaminC,
                             float vitaminD, float sodium, float potassium, float calcium, float iron)
    {
        this.calories = calories;
        this.water = water;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.sugar = sugar;
        this.fiber = fiber;
        this.cholesterol = cholesterol;
        this.saturatedFat = saturatedFat;
        this.monounsaturatedFat = monounsaturatedFat;
        this.polyunsaturatedFat = polyunsaturatedFat;
        this.transFat = transFat;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.vitaminD = vitaminD;
        this.sodium = sodium;
        this.potassium = potassium;
        this.calcium = calcium;
        this.iron = iron;
    }*/

    public String toTextViewString()
    {
        return mealName + "\n" +
                "Serving Size:" + servingSize + "\n" +
                "Calories: " + calories + "kcal\n" +
                "Water: " + water + "g\n" +
                "Protein: " + protein + "g\n" +
                "Carbohydrate: " + carbohydrate + "g\n" +
                "Sugar: " + sugar + "g\n" +
                "Fiber: " + fiber + "g\n" +
                "Cholesterol: " + cholesterol + "mg\n" +
                "Saturated Fat: " + saturatedFat + "g\n" +
                "Unsaturated Fat: " + (monounsaturatedFat + polyunsaturatedFat) + "g\n" +
                "Trans Fat: " + transFat + "g\n" +
                "Vitamin A: " + vitaminA + "μg\n" +
                "Vitamin C: " + vitaminC + "mg\n" +
                "Vitamin D: " + vitaminD + "μg\n" +
                "Sodium: " + sodium + "g\n" +
                "Potassium: " + potassium + "g\n" +
                "Calcium: " + calcium + "mg\n" +
                "Iron: " + iron + "mg\n";
    }
}
