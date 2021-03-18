package com.example.nutrimons.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
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
    public int calories;

    @ColumnInfo(name = "water")
    public int water;

    @ColumnInfo(name = "protein")
    public int protein;

    @ColumnInfo(name = "carbohydrate")
    public int carbohydrate;

    @ColumnInfo(name = "sugar")
    public int sugar;

    @ColumnInfo(name = "fiber")
    public int fiber;

    @ColumnInfo(name = "cholesterol")
    public int cholesterol;

    @ColumnInfo(name = "saturatedFat")
    public int saturatedFat;

    @ColumnInfo(name = "monounsaturatedFat")
    public int monounsaturatedFat;

    @ColumnInfo(name = "polyunsaturatedFat")
    public int polyunsaturatedFat;

    @ColumnInfo(name = "transFat")
    public int transFat;

    @ColumnInfo(name = "vitaminA")
    public int vitaminA;

    @ColumnInfo(name = "vitaminC")
    public int vitaminC;

    @ColumnInfo(name = "vitaminD")
    public int vitaminD;

    @ColumnInfo(name = "sodium")
    public int sodium;

    @ColumnInfo(name = "potassium")
    public int potassium;

    @ColumnInfo(name = "calcium")
    public int calcium;

    @ColumnInfo(name = "iron")
    public int iron;

    public Meal(String mealName, int servingSize, int servingWeight, int caloriesPerServing) {
        this.mealName = mealName;
        this.servingSize = servingSize;
        this.servingWeight = servingWeight;
        this.caloriesPerServing = caloriesPerServing;
    }

    public Meal(){}

    public void setFieldFromString(String field, int value)
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

    public String toTextViewString()
    {
        return mealName + "\n" +
                "Calories: " + calories + "kcal\n" +
                "Water: " + water + "g\n" +
                "Protein: " + protein + "g\n" +
                "Carbohydrate: " + carbohydrate + "g\n" +
                "Sugar: " + sugar + "g\n" +
                "Fiber: " + fiber + "g\n" +
                "Cholesterol" + cholesterol + "mg\n" +
                "Saturated Fat: " + saturatedFat + "g\n" +
                "Unsaturated Fat: " + monounsaturatedFat + polyunsaturatedFat + "g\n" +
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
