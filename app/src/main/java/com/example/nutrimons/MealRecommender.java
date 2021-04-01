package com.example.nutrimons;

import android.app.ActionBar;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.util.TableInfo;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrimons.database.AppDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.camera.core.CameraX.getContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFromFDC#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealRecommender {

    public static String mealName;
    public static boolean isBranded;

    private final String FDC_API_KEY = "5k1NBU6Op8fHuzi1DBBG3rIAKT7SZuUFLoKpw6Fc";
    private final String QUERY_HEADER = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=";
    private RequestQueue queue;

    private Context context;

    public MealRecommender(Context context) {
        // Required empty public constructor

        this.context = context;

        queue = Volley.newRequestQueue(context);
        queue.cancelAll(mealName);
        JsonObjectRequest JSONoReq = callFDCapi(mealName, isBranded);
        queue.add(JSONoReq);
    }

    //work to get serving size/proportion eaten in
    private JsonObjectRequest callFDCapi(String searchString, boolean isBranded) //ref: https://fdc.nal.usda.gov/api-guide.html
    {
        String searchURL = QUERY_HEADER + FDC_API_KEY /*+ QUERY_MIDDLE + searchString*/;
        //example: https://api.nal.usda.gov/fdc/v1/foods/search?api_key=DEMO_KEY&query=applesauce
        Log.d("searchURL", searchURL);
        JSONObject jsonBody = new JSONObject();
        try{ //per FoodListCriteria https://fdc.nal.usda.gov/api-spec/fdc_api.html#/
            jsonBody.put("query", searchString);
            if(isBranded)
                jsonBody.put("dataType", new JSONArray(new Object[]{"Foundation", "Survey", "Branded", "SR Legacy"}));
            else
                jsonBody.put("dataType", new JSONArray(new Object[]{"Foundation", "Survey", "SR Legacy"}));
            jsonBody.put("requireAllWords", true);
        }
        catch(JSONException e) { e.printStackTrace(); }

        HashMap<String, String> nutrientsOfInterest = generateNutrientsOfInterest();

        return new JsonObjectRequest(Request.Method.POST, searchURL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            Log.d("res", response.toString()); //ref: https://fdc.nal.usda.gov/api-spec/fdc_api.html#/FDC/getFoodsSearch
                            Toast.makeText(context, "api request sent response", Toast.LENGTH_SHORT).show();

                            JSONArray foodArray = response.getJSONArray(("foods"));
                            //api response is only 50 items per page, need to do a new request to get previous/next pages
                            //Log.d("length", String.valueOf(foodArray.length()));

                            JSONObject meal = foodArray.getJSONObject(0);

                            String foodName = meal.getString("description");

                                com.example.nutrimons.database.Meal food = new com.example.nutrimons.database.Meal();
                                food.mealName = foodName;
                                food.servingSize = "100g"; //all units seem to be 100g, no info in json
                                food.servingsEaten = 1;

                                JSONArray foodNutrients = meal.getJSONArray("foodNutrients");

                                for(int j = 0; j < foodNutrients.length(); ++j)
                                {
                                    JSONObject foodItem = foodNutrients.getJSONObject(j);
                                    if(nutrientsOfInterest.containsKey(foodItem.getString("nutrientName")))
                                    {
                                        if(foodItem.getString("value") != "kJ")
                                        {
                                            food.setFieldFromString(nutrientsOfInterest.get(foodItem.getString("nutrientName")), ((Double)foodItem.getDouble("value")).floatValue());
                                        }
                                    }
                                }

                            // catch for the JSON parsing error
                        } catch (Exception e/*JSONException e*/) {
                            //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "error with api response", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(context, "api not responding", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                })
        {
            @Override //change http header per OFF api READ operations request
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("UserAgent: NutriMons - Android - Version 0.0 - https://github.com/alissakc/NutriMons", "CSULB CECS 491: BAMM");

                return params;
            }
        };
    }

    private HashMap<String, String> generateNutrientsOfInterest()
    {
        HashMap<String, String> nutrientsOfInterest = new HashMap<>();
        nutrientsOfInterest.put("Energy", "calories");
        //nutrientsOfInterest.put("Water", "water");
        nutrientsOfInterest.put("Protein", "protein");
        nutrientsOfInterest.put("Carbohydrate, by difference", "carbohydrate");
        nutrientsOfInterest.put("Sugars, total including NLEA", "sugar");
        nutrientsOfInterest.put("Fiber, total dietary", "fiber");
        nutrientsOfInterest.put("Cholesterol", "cholesterol");
        nutrientsOfInterest.put("Fatty acids, total saturated", "saturatedFat");
        nutrientsOfInterest.put("Fatty acids, total monounsaturated", "monounsaturatedFat");
        nutrientsOfInterest.put("Fatty acids, total polyunsaturated", "polyunsaturatedFat");
        nutrientsOfInterest.put("Fatty acids, total trans", "transFat");

        nutrientsOfInterest.put("Vitamin A, RAE", "vitaminA");
        nutrientsOfInterest.put("Vitamin C, total ascorbic acid", "vitaminC");
        nutrientsOfInterest.put("Vitamin D (D2 + D3)", "vitaminD");
        //nutrientsOfInterest.put("Vitamin E, added", "vitaminE");
        //nutrientsOfInterest.put("Vitamin K (phylloquinone)", "vitaminK");
        //nutrientsOfInterest.put("Thiamin", "thiamin");
        //nutrientsOfInterest.put("Riboflavin", "riboflavin");
        //nutrientsOfInterest.put("Niacin", "niacin");
        //nutrientsOfInterest.put("Vitamin B-6", "vitaminB6");
        //nutrientsOfInterest.put("Folate, total", "folate");
        //nutrientsOfInterest.put("Vitamin B-12", "vitaminB12");
        //nutrientsOfInterest.put("Pantothenic acid", "pantothenicAcid");
        //nutrientsOfInterest.put("Biotin", "biotin");
        //nutrientsOfInterest.put("Choline, total", "choline");
        //nutrientsOfInterest.put("Carotenoids", "carotenoids");

        nutrientsOfInterest.put("Sodium, Na", "sodium");
        nutrientsOfInterest.put("Potassium, K", "potassium");
        nutrientsOfInterest.put("Calcium, Ca", "calcium");
        nutrientsOfInterest.put("Iron, Fe", "iron");
        //nutrientsOfInterest.put("Chromium, Cr", "chromium");
        //nutrientsOfInterest.put("Copper, Cu", "copper");
        //nutrientsOfInterest.put("Fluoride, F", "fluoride");
        //nutrientsOfInterest.put("Iodine, I", "iodine");
        //nutrientsOfInterest.put("Magnesium, Mg", "magnesium");
        //nutrientsOfInterest.put("Manganese, Mn", "manganese");
        //nutrientsOfInterest.put("Molybdenum, Mo", "molybdenum");
        //nutrientsOfInterest.put("Phosphorous, P", "phosphorous");
        //nutrientsOfInterest.put("Selenium, Se", "selenium");
        //nutrientsOfInterest.put("Zinc, Zn", "zinc");
        //nutrientsOfInterest.put("Chloride, Cl", "chloride");
        //nutrientsOfInterest.put("Arsenic, As", "arsenic");
        //nutrientsOfInterest.put("Boron, B", "boron");
        //nutrientsOfInterest.put("Nickel, Ni", "nickel");
        //nutrientsOfInterest.put("Silicon, Si", "silicon");
        //nutrientsOfInterest.put("Vanadium, V", "vanadium");

        return nutrientsOfInterest;
    }
}