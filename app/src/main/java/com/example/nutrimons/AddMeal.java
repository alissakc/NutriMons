package com.example.nutrimons;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.nutrimons.database.AppDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMeal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMeal extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private Button addButton;
    private String mealName;
    private int servingSize, servingWeight, caloriesPerServing;
    private EditText mealNameText, servingSizeText, servingWeightText, caloriesPerServingText;

    // creates instance of database
    private AppDatabase mDb;

    private final String FDC_API_KEY = "5k1NBU6Op8fHuzi1DBBG3rIAKT7SZuUFLoKpw6Fc";
    private final String QUERY_HEADER = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=";
    private final String QUERY_MIDDLE = "&query=";
    private RequestQueue queue;

    public AddMeal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMeal.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMeal newInstance(String param1, String param2) {
        AddMeal fragment = new AddMeal();
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
        View view = inflater.inflate(R.layout.fragment_add_meal, container, false);

        // database
        mDb = AppDatabase.getInstance(getContext());

        // Button initialization
        addButton = view.findViewById(R.id.submitNewMealButton);

        // new meal
        mealNameText = (EditText) view.findViewById(R.id.editTextFoodName);
        /*servingSizeText = (EditText) view.findViewById(R.id.editTextServingSize);
        servingWeightText = (EditText) view.findViewById(R.id.editTextServingWeight);
        caloriesPerServingText = (EditText) view.findViewById(R.id.editTextCalories);*/

        queue = Volley.newRequestQueue(getContext());

        //assign listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Created entry", Toast.LENGTH_SHORT).show();
                mealName = mealNameText.getText().toString();
                /*servingSize = Integer.parseInt(servingSizeText.getText().toString());
                servingWeight = Integer.parseInt(servingWeightText.getText().toString());
                caloriesPerServing = Integer.parseInt(caloriesPerServingText.getText().toString());*/

                final com.example.nutrimons.database.Meal meal = new com.example.nutrimons.database.Meal(mealName, servingSize, servingWeight, caloriesPerServing);
                //mDb.mealDao().insert(meal);

                queue.cancelAll(mealName);
                TextView tv = view.findViewById(R.id.FDCapiResponse);
                StringRequest strReq = callFDCapi(mealName, tv);
                queue.add(strReq);

                // navigates to meal plan
                //Navigation.findNavController(view).navigate(R.id.action_nav_addMeal_to_nav_mealPlan);
            }
        });

        return view;
    }

    private StringRequest callFDCapi(String searchString, TextView tv) //ref: https://fdc.nal.usda.gov/api-guide.html
    {
        String searchURL = QUERY_HEADER + FDC_API_KEY + QUERY_MIDDLE + searchString;
        //example: https://api.nal.usda.gov/fdc/v1/foods/search?api_key=DEMO_KEY&query=apple
        Log.d("searchURL", searchURL);

        return new StringRequest(Request.Method.GET, searchURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            tv.setText(response); //ref: https://fdc.nal.usda.gov/api-spec/fdc_api.html#/FDC/getFoodsSearch
                            Toast.makeText(getContext(), "api request sent response", Toast.LENGTH_SHORT).show();

                            // catch for the JSON parsing error
                        } catch (Exception e/*JSONException e*/) {
                            //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "error with api response", Toast.LENGTH_SHORT).show();
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