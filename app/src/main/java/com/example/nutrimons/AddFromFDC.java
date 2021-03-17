package com.example.nutrimons;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private AppDatabase mDb;
    private Bundle bundle;
    public static String mealName;
    public static boolean isBranded;

    private final String FDC_API_KEY = "5k1NBU6Op8fHuzi1DBBG3rIAKT7SZuUFLoKpw6Fc";
    private final String QUERY_HEADER = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=";
    private final String QUERY_MIDDLE = "&query=";
    private RequestQueue queue;

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

        mDb = AppDatabase.getInstance(getContext());

        //bundle = this.getArguments();
        queue = Volley.newRequestQueue(getContext());
        queue.cancelAll(mealName);
        TextView tv = v.findViewById(R.id.FDCapiResponse);
        JsonObjectRequest JSONoReq = callFDCapi(mealName, tv, isBranded);
        queue.add(JSONoReq);

        return v;
    }

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
        Log.d("branded", String.valueOf(isBranded));

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
                            Log.d("number of foods", String.valueOf(foodArray.length()));

                            for(int i = 0; i < foodArray.length(); ++i)
                            {
                                String foodName = foodArray.getJSONObject(i).getString("description");
                                Log.d("food " + i, foodName);

                                JSONArray foodNutrients = foodArray.getJSONObject(i).getJSONArray("foodNutrients");
                                Log.d("Number of food nutrients", String.valueOf(foodNutrients.length()));
                                for(int j = 0; j < foodNutrients.length(); ++j)
                                {
                                    JSONObject foodItem = foodNutrients.getJSONObject(j);
                                    Log.d("food " + i, foodItem.getString("nutrientName") + " " +
                                            foodItem.getInt("value") + foodItem.getString("unitName"));
                                }
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
}