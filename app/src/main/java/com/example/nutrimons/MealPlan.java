package com.example.nutrimons;

import android.app.ActionBar;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;
import com.example.nutrimons.database.Exercise;
import com.example.nutrimons.database.Meal;
import com.google.android.material.navigation.NavigationView;


public class MealPlan extends Fragment implements OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = null;

    // TODO: Rename and change types of parameters
    private String date;

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

    public MealPlan(String date) {
        newInstance(date);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date Parameter 1.
     * @return A new instance of fragment MealPlan.
     */
    // TODO: Rename and change types and number of parameters
    public static MealPlan newInstance(String date) {
        MealPlan fragment = new MealPlan();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
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

        // checks if the there is a bundle from FragmentTransaction
        String dateString = "";
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            // gets current date
            long date = System.currentTimeMillis();
            SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
            dateString = Date.format(date);
        } else {
            String date = bundle.getString("key");
            if (date.charAt(2) != '/') {
                dateString = "0" + date;
            } else {
                dateString = date;
            }
        }

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
        String finalDateString = dateString;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                selectedBreakfast.add(breakfastSpinner.getSelectedItem().toString());
                selectedLunch.add(lunchSpinner.getSelectedItem().toString());
                selectedDinner.add(dinnerSpinner.getSelectedItem().toString());
                selectedSnack.add(snackSpinner.getSelectedItem().toString());*/
                selectedBreakfast.add(mDb.mealDao().findByName(breakfastSpinner.getSelectedItem().toString()));
                selectedLunch.add(mDb.mealDao().findByName(lunchSpinner.getSelectedItem().toString()));
                selectedDinner.add(mDb.mealDao().findByName(dinnerSpinner.getSelectedItem().toString()));
                selectedSnack.add(mDb.mealDao().findByName(snackSpinner.getSelectedItem().toString()));

                if (mDb.dateDataDao().findByDate(finalDateString) == null) {
                    final com.example.nutrimons.database.DateData dateData = new com.example.nutrimons.database.DateData(finalDateString, selectedBreakfast, selectedLunch, selectedDinner, selectedSnack, new ArrayList<>());
                    dateData.aggregateNutrients();
                    mDb.dateDataDao().insert(dateData);
                } else {
                    DateData temp = mDb.dateDataDao().findByDate(finalDateString);
                    // checks for existing meals
                    List<com.example.nutrimons.database.Meal> breakfast = new ArrayList<>();
                    try{
                        breakfast = (temp.getBreakfast() == null ?
                                (temp.getBreakfast().isEmpty() ? null : temp.getBreakfast()) : temp.getBreakfast());
                    }catch(NullPointerException e){
                        breakfast = null;
                    }
                    List<com.example.nutrimons.database.Meal> lunch = new ArrayList<>();
                    try{
                        lunch = (temp.getLunch() == null ?
                                (temp.getLunch().isEmpty() ? null : temp.getLunch()) : temp.getLunch());
                    }catch(NullPointerException e){
                        lunch = null;
                    }
                    List<com.example.nutrimons.database.Meal> dinner = new ArrayList<>();
                    try{
                        dinner = (temp.getDinner() == null ?
                                (temp.getDinner().isEmpty() ? null : temp.getDinner()) : temp.getDinner());
                    }catch(NullPointerException e){
                        dinner = null;
                    }
                    List<com.example.nutrimons.database.Meal> snack = new ArrayList<>();
                    try{
                        snack = (temp.getSnack() == null ?
                                (temp.getSnack().isEmpty() ? null : temp.getSnack()) : temp.getSnack());
                    }catch(NullPointerException e){
                        snack = null;
                    }
                    // checks to see if nothing was selected
                    if (breakfastSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedBreakfast.clear();
                    } else {
                        if (breakfast != null) {
                            if(!breakfast.isEmpty()){
                                for (com.example.nutrimons.database.Meal b : breakfast) {
                                    if(b != null){
                                        if (!b.mealName.equalsIgnoreCase("null")) {
                                            if (!b.mealName.isEmpty()) {
                                                selectedBreakfast.add(b);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (lunchSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedLunch.clear();
                    } else {
                        // checks for existing meals
                        if (lunch != null) {
                            if(!lunch.isEmpty()){
                                for (com.example.nutrimons.database.Meal l : lunch) {
                                    if(l != null){
                                        if (!l.mealName.equalsIgnoreCase("null")) {
                                            if (!l.mealName.isEmpty()) {
                                                selectedLunch.add(l);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (dinnerSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedDinner.clear();
                    } else {
                        // checks for existing meals
                        if (dinner != null) {
                            if(!dinner.isEmpty()){
                                for (com.example.nutrimons.database.Meal d : dinner) {
                                    if(d != null){
                                        if (!d.mealName.equalsIgnoreCase("null")) {
                                            if (!d.mealName.isEmpty()) {
                                                selectedDinner.add(d);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (snackSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedSnack.clear();
                    } else {
                        // checks for existing meals
                        if (snack != null) {
                            if(!snack.isEmpty()){
                                for (com.example.nutrimons.database.Meal s : snack) {
                                    if(s != null){
                                        if (!s.mealName.equalsIgnoreCase("null")) {
                                            if (!s.mealName.isEmpty()) {
                                                selectedSnack.add(s);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    final com.example.nutrimons.database.DateData dateData = new com.example.nutrimons.database.DateData(finalDateString,
                            (selectedBreakfast.isEmpty()) ? mDb.dateDataDao().findByDate(finalDateString).breakfast : selectedBreakfast,
                            (selectedLunch.isEmpty()) ? mDb.dateDataDao().findByDate(finalDateString).lunch : selectedLunch,
                            (selectedDinner.isEmpty()) ? mDb.dateDataDao().findByDate(finalDateString).dinner : selectedDinner,
                            (selectedSnack.isEmpty()) ? mDb.dateDataDao().findByDate(finalDateString).snack : selectedSnack,
                            mDb.dateDataDao().findByDate(finalDateString).todayExercise);
                    dateData.aggregateNutrients();
                    mDb.dateDataDao().updateDateData(dateData);
                    //mDb.dateDataDao().updateMealPlan(selectedBreakfast, selectedLunch, selectedDinner, selectedSnack, dateString);
                }

                /*if (mDb.dateDataDao().findByDate(finalDateString) == null) {
                    final com.example.nutrimons.database.DateData dateData = new com.example.nutrimons.database.DateData(finalDateString, selectedBreakfast, selectedLunch, selectedDinner, selectedSnack, new ArrayList<>());
                    dateData.aggregateNutrients();
                    mDb.dateDataDao().insert(dateData);
                } else {
                    // checks to see if nothing was selected
                    if (breakfastSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedBreakfast.clear();
                    } else {
                        // checks for existing meals
                        if (!mDb.dateDataDao().findBreakfastByDate(finalDateString).isEmpty()) {
                            for (String s : mDb.dateDataDao().findBreakfastByDate(finalDateString)) {
                                if (!s.equalsIgnoreCase("[null]")) {
                                    if (!s.equalsIgnoreCase("[]")) {
                                        s = s.replaceAll("\\W", "");
                                        selectedBreakfast.add(mDb.mealDao().findByName(s));
                                    }
                                }
//                                selectedBreakfast.add(mDb.mealDao().findByName(s));
//                                selectedBreakfast.add(s);
                            }
                        }
                    }
                    if (lunchSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedLunch.clear();
                    } else {
                        // checks for existing meals
                        if (!mDb.dateDataDao().findLunchByDate(finalDateString).isEmpty()) {
                            for (String s : mDb.dateDataDao().findLunchByDate(finalDateString)) {
                                if (!s.equalsIgnoreCase("[null]")) {
                                    if (!s.equalsIgnoreCase("[]")) {
                                        s = s.replaceAll("\\W", "");
                                        selectedLunch.add(mDb.mealDao().findByName(s));
                                    }
                                }
//                                selectedLunch.add(s);
//                                selectedLunch.add(mDb.mealDao().findByName(s));
                            }
                        }
                    }
                    if (dinnerSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedDinner.clear();
                    } else {
                        // checks for existing meals
                        if (!mDb.dateDataDao().findDinnerByDate(finalDateString).isEmpty()) {
                            for (String s : mDb.dateDataDao().findDinnerByDate(finalDateString)) {
                                if (!s.equalsIgnoreCase("[null]")) {
                                    if (!s.equalsIgnoreCase("[]")) {
                                        s = s.replaceAll("\\W", "");
                                        selectedDinner.add(mDb.mealDao().findByName(s));
                                    }
                                }
//                                selectedDinner.add(s);
//                                selectedDinner.add(mDb.mealDao().findByName(s));
                            }
                        }
                    }
                    if (snackSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedSnack.clear();
                    } else {
                        // checks for existing meals
                        if (!mDb.dateDataDao().findSnackByDate(finalDateString).isEmpty()) {
                            for (String s : mDb.dateDataDao().findSnackByDate(finalDateString)) {
                                if (!s.equalsIgnoreCase("[null]")) {
                                    if (!s.equalsIgnoreCase("[]")) {
                                        s = s.replaceAll("\\W", "");
                                        selectedSnack.add(mDb.mealDao().findByName(s));
                                    }
                                }
//                                selectedSnack.add(s);
//                                selectedSnack.add(mDb.mealDao().findByName(s));
                            }
                        }
                    }

                    final com.example.nutrimons.database.DateData dateData = new com.example.nutrimons.database.DateData(finalDateString,
                            (selectedBreakfast.isEmpty()) ? mDb.dateDataDao().findByDate(finalDateString).breakfast : selectedBreakfast,
                            (selectedLunch.isEmpty()) ? mDb.dateDataDao().findByDate(finalDateString).lunch : selectedLunch,
                            (selectedDinner.isEmpty()) ? mDb.dateDataDao().findByDate(finalDateString).dinner : selectedDinner,
                            (selectedSnack.isEmpty()) ? mDb.dateDataDao().findByDate(finalDateString).snack : selectedSnack,
                            mDb.dateDataDao().findByDate(finalDateString).todayExercise);
                    dateData.aggregateNutrients();
                    mDb.dateDataDao().updateDateData(dateData);
                    //mDb.dateDataDao().updateMealPlan(selectedBreakfast, selectedLunch, selectedDinner, selectedSnack, dateString);
                }*/

                long date = System.currentTimeMillis();
                SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                String currentDate = Date.format(date);
                if (!finalDateString.equalsIgnoreCase(currentDate)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", finalDateString);
                    DailyInfoFragment fragment = new DailyInfoFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    ActionBar actionBar = getActivity().getActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle("Daily Information");
                    }
                    transaction.replace(R.id.fragment_meal_plan, fragment).addToBackStack(null).commit();
                } else {
                    // navigates to the dashboard
                    //Navigation.findNavController(view).navigate(R.id.action_nav_mealPlan_to_nav_home);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", finalDateString);
                    Dashboard fragment = new Dashboard();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    ActionBar actionBar = getActivity().getActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle("Dashboard");
                    }
                    transaction.replace(R.id.fragment_meal_plan, fragment).addToBackStack(null).commit();
                }
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