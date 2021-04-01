package com.example.nutrimons;

import android.app.ActionBar;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFromFDC#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFromFDC extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    public static String mealName;
    public static boolean isBranded;

    private final String FDC_API_KEY = "5k1NBU6Op8fHuzi1DBBG3rIAKT7SZuUFLoKpw6Fc";
    private final String QUERY_HEADER = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=";
    //private final String QUERY_MIDDLE = "&query=";
    private RequestQueue queue;

    private class foodRow
    {
        public com.example.nutrimons.database.Meal food;
        public Button selectButton, detailsButton;
        public boolean isDetailsOpen;
        public TextView text;

        public foodRow(com.example.nutrimons.database.Meal food, Button selectButton, Button detailsButton, TextView text)
        {
            this.food = food;
            this.selectButton = selectButton;
            this.selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        AddMeal.food = food;
                        Navigation.findNavController(view).navigate(R.id.action_nav_addFromFDC_to_nav_addMeal);
                    }
                    catch(SQLiteConstraintException e)
                    {
                        Toast.makeText(getContext(), "You already have this in your Meal Plan", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            this.detailsButton = detailsButton;
            this.detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isDetailsOpen){
                        text.setText(food.mealName);
                        isDetailsOpen = false;
                    } else {
                        text.setText(food.toTextViewString());
                        isDetailsOpen = true;
                    }
                }
            });
            this.text = text;
            this.isDetailsOpen = false;
        }
    }

    private ArrayList<foodRow> foodRows = new ArrayList<>();

    public AddFromFDC() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_add_from_FDC.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFromFDC newInstance(String param1, String param2) {
        AddFromFDC fragment = new AddFromFDC();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_from_fdc, container, false);

        queue = Volley.newRequestQueue(getContext());
        queue.cancelAll(mealName);
        TextView tv = v.findViewById(R.id.FDCapiResponse);
        JsonObjectRequest JSONoReq = callFDCapi(mealName, tv, isBranded);
        queue.add(JSONoReq);

        return v;
    }

    //work to get serving size/proportion eaten in
    private JsonObjectRequest callFDCapi(String searchString, TextView tv, boolean isBranded) //ref: https://fdc.nal.usda.gov/api-guide.html
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
        TableLayout table = (TableLayout) v.findViewById(R.id.tableFDC);

        return new JsonObjectRequest(Request.Method.POST, searchURL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            Log.d("res", response.toString()); //ref: https://fdc.nal.usda.gov/api-spec/fdc_api.html#/FDC/getFoodsSearch
                            Toast.makeText(getContext(), "api request sent response", Toast.LENGTH_SHORT).show();

                            JSONArray foodArray = response.getJSONArray(("foods"));
                            //api response is only 50 items per page, need to do a new request to get previous/next pages
                            //Log.d("length", String.valueOf(foodArray.length()));

                            for(int i = 0; i < foodArray.length(); ++i)
                            {
                                String foodName = foodArray.getJSONObject(i).getString("description");

                                TableRow row = new TableRow(getContext());
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);

                                Button button = new Button(getContext());
                                button.setText(String.valueOf(i + 1));
                                row.addView(button);

                                TextView name = new TextView(getContext());
                                name.setText(foodName);
                                name.setEms(12);
                                name.setGravity(Gravity.CENTER_HORIZONTAL);
                                row.addView(name);

                                Button details = new Button(getContext());
                                details.setText("<<");
                                row.addView(details);

                                table.addView(row, i + 1);

                                com.example.nutrimons.database.Meal food = new com.example.nutrimons.database.Meal();
                                food.mealName = foodName;
                                food.servingSize = "100g"; //all units seem to be 100g, no info in json
                                food.servingsEaten = 1;

                                JSONArray foodNutrients = foodArray.getJSONObject(i).getJSONArray("foodNutrients");

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
                                foodRows.add(new foodRow(food, button, details, name));
                            }

                            // catch for the JSON parsing error
                        } catch (Exception e/*JSONException e*/) {
                            //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "error with api response", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "api not responding", Toast.LENGTH_SHORT).show();
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