package com.example.nutrimons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.Exercise;
import com.example.nutrimons.database.Meal;
import com.google.android.material.navigation.NavigationView;


public class MealPlan extends Fragment implements OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private Spinner breakfastSpinner, lunchSpinner, dinnerSpinner, snackSpinner;
    private Button save;
    private final List<Meal> selectedBreakfast = new ArrayList<>();
    private final List<Meal> selectedLunch = new ArrayList<>();
    private final List<Meal> selectedDinner = new ArrayList<>();
    private final List<Meal> selectedSnack = new ArrayList<>();
    private ArrayAdapter<String> breakfastDataAdapter, lunchDataAdapter, dinnerDataAdapter, snackDataAdapter;

    // creates instance of database
    private AppDatabase mDb;

    public MealPlan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealPlan.
     */
    // TODO: Rename and change types and number of parameters
    public static MealPlan newInstance(String param1, String param2) {
        MealPlan fragment = new MealPlan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);

        // database
        mDb = AppDatabase.getInstance(getContext());

        // Spinner element
        breakfastSpinner = (Spinner) view.findViewById(R.id.breakfastMultipleItemSelectionSpinner);
        lunchSpinner = (Spinner) view.findViewById(R.id.lunchMultipleItemSelectionSpinner);
        dinnerSpinner = (Spinner) view.findViewById(R.id.dinnerMultipleItemSelectionSpinner);
        snackSpinner = (Spinner) view.findViewById(R.id.snackMultipleItemSelectionSpinner);

        // Spinner click listener
        breakfastSpinner.setOnItemSelectedListener(this);
        lunchSpinner.setOnItemSelectedListener(this);
        dinnerSpinner.setOnItemSelectedListener(this);
        snackSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> food = (List<String>) mDb.mealDao().getAllNames();
        food.add(0, "Select an item");

        /*
        // Spinner Drop down elements
        List<String> food = new ArrayList<String>();
        food.add("Select an item");
        food.add("Apple Pie");
        food.add("Banana");
        food.add("Chicken Noodle Soup");
        food.add("Eggs");
        food.add("Greek Yogurt");
        food.add("Tomato Sauce Pasta");
        food.add("White Rice");
         */

        // Creating adapter for spinners
        breakfastDataAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, food);
        lunchDataAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, food);
        dinnerDataAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, food);
        snackDataAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, food);

        // Drop down layout style - list view with radio button
        breakfastDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lunchDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snackDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinners
        breakfastSpinner.setAdapter(breakfastDataAdapter);
        lunchSpinner.setAdapter(lunchDataAdapter);
        dinnerSpinner.setAdapter(dinnerDataAdapter);
        snackSpinner.setAdapter(snackDataAdapter);

        save = view.findViewById(R.id.saveMealPlanButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedBreakfast.add(mDb.mealDao().findByName(breakfastSpinner.getSelectedItem().toString()));
                selectedLunch.add(mDb.mealDao().findByName(lunchSpinner.getSelectedItem().toString()));
                selectedDinner.add(mDb.mealDao().findByName(dinnerSpinner.getSelectedItem().toString()));
                selectedSnack.add(mDb.mealDao().findByName(snackSpinner.getSelectedItem().toString()));
                long date = System.currentTimeMillis();
                SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                String dateString = Date.format(date);
                if(mDb.dateDataDao().findByDate(dateString) == null){
                    final com.example.nutrimons.database.DateData dateData = new com.example.nutrimons.database.DateData(dateString, selectedBreakfast, selectedLunch, selectedDinner, selectedSnack, new ArrayList<String>());
                    dateData.aggregateNutrients();
                    mDb.dateDataDao().insert(dateData);
                }
                else{
                    if(breakfastSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")){
                        selectedBreakfast.clear();
                    }
                    if(lunchSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")){
                        selectedLunch.clear();
                    }
                    if(dinnerSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")){
                        selectedDinner.clear();
                    }
                    if(snackSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")){
                        selectedSnack.clear();
                    }
                    final com.example.nutrimons.database.DateData dateData = new com.example.nutrimons.database.DateData(dateString,
                            (selectedBreakfast.isEmpty()) ? mDb.dateDataDao().findByDate(dateString).breakfast : selectedBreakfast,
                            (selectedLunch.isEmpty()) ? mDb.dateDataDao().findByDate(dateString).lunch : selectedLunch,
                            (selectedDinner.isEmpty()) ? mDb.dateDataDao().findByDate(dateString).dinner : selectedDinner,
                            (selectedSnack.isEmpty()) ? mDb.dateDataDao().findByDate(dateString).snack : selectedSnack,
                            mDb.dateDataDao().findByDate(dateString).todayExercise);
                    dateData.aggregateNutrients();
                    mDb.dateDataDao().updateDateData(dateData);
                    //mDb.dateDataDao().updateMealPlan(selectedBreakfast, selectedLunch, selectedDinner, selectedSnack, dateString);
                }
                // navigates to the dashboard
                Navigation.findNavController(view).navigate(R.id.action_nav_mealPlan_to_nav_home);
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        parent.getId();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}