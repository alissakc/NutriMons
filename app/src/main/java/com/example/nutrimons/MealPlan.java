package com.example.nutrimons;

import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;
import com.example.nutrimons.database.Exercise;
import com.example.nutrimons.database.Meal;
import com.example.nutrimons.database.User;
import com.google.android.material.navigation.NavigationView;


public class MealPlan extends Fragment implements OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = null;

    // vars
    private Spinner breakfastSpinner, lunchSpinner, dinnerSpinner, snackSpinner;
    private Button save;
    private final List<Meal> selectedBreakfast = new ArrayList<>();
    private final List<Meal> selectedLunch = new ArrayList<>();
    private final List<Meal> selectedDinner = new ArrayList<>();
    private final List<Meal> selectedSnack = new ArrayList<>();
    private ArrayAdapter<String> breakfastDataAdapter, lunchDataAdapter, dinnerDataAdapter, snackDataAdapter;
    private OnFragmentInteractionListener mListener;

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
        if (mListener != null) {
            mListener.onFragmentInteraction("Meal Plan");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);

        // database
        mDb = AppDatabase.getInstance(getContext());

        // checks if the there is a bundle from FragmentTransaction
        String dateString;
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
                selectedBreakfast.add(mDb.mealDao().findByName(breakfastSpinner.getSelectedItem().toString()));
                selectedLunch.add(mDb.mealDao().findByName(lunchSpinner.getSelectedItem().toString()));
                selectedDinner.add(mDb.mealDao().findByName(dinnerSpinner.getSelectedItem().toString()));
                selectedSnack.add(mDb.mealDao().findByName(snackSpinner.getSelectedItem().toString()));

/*                ArrayList<String> finalMeals = new ArrayList<>();
                ArrayList<String> finalExercises = new ArrayList<>();*/
                if (mDb.dateDataDao().findByDate(finalDateString) == null) {
                    final com.example.nutrimons.database.DateData dateData = new com.example.nutrimons.database.DateData(finalDateString, selectedBreakfast, selectedLunch, selectedDinner, selectedSnack, new ArrayList<>());
                    dateData.aggregateNutrients();
                    mDb.dateDataDao().insert(dateData);
                } else {
                    DateData temp = mDb.dateDataDao().findByDate(finalDateString);
                    // checks for existing meals
                    List<com.example.nutrimons.database.Meal> breakfast;
                    try {
                        breakfast = temp.getBreakfast();
                    } catch (NullPointerException e) {
                        breakfast = null;
                    }
                    List<com.example.nutrimons.database.Meal> lunch;
                    try {
                        lunch = temp.getLunch();
                    } catch (NullPointerException e) {
                        lunch = null;
                    }
                    List<com.example.nutrimons.database.Meal> dinner;
                    try {
                        dinner = temp.getDinner();
                    } catch (NullPointerException e) {
                        dinner = null;
                    }
                    List<com.example.nutrimons.database.Meal> snack;
                    try {
                        snack = temp.getSnack();
                    } catch (NullPointerException e) {
                        snack = null;
                    }
                    // checks to see if nothing was selected
                    if (breakfastSpinner.getSelectedItem().toString().equalsIgnoreCase("Select an item")) {
                        selectedBreakfast.clear();
                    } else {
                        if (breakfast != null) {
                            if (!breakfast.isEmpty()) {
                                for (com.example.nutrimons.database.Meal b : breakfast) {
                                    if (b != null) {
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
                            if (!lunch.isEmpty()) {
                                for (com.example.nutrimons.database.Meal l : lunch) {
                                    if (l != null) {
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
                            if (!dinner.isEmpty()) {
                                for (com.example.nutrimons.database.Meal d : dinner) {
                                    if (d != null) {
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
                            if (!snack.isEmpty()) {
                                for (com.example.nutrimons.database.Meal s : snack) {
                                    if (s != null) {
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

    /*                if (!selectedBreakfast.isEmpty()) {
                        for (Meal b : selectedBreakfast) {
                            if (b != null) {
                                if (!b.mealName.equalsIgnoreCase("null")) {
                                    if (!b.mealName.isEmpty()) {
                                        finalMeals.add(b.mealName);
                                    }
                                }
                            }
                        }
                    }
                    if (!selectedLunch.isEmpty()) {
                        for (Meal l : selectedLunch) {
                            if (l != null) {
                                if (!l.mealName.equalsIgnoreCase("null")) {
                                    if (!l.mealName.isEmpty()) {
                                        finalMeals.add(l.mealName);
                                    }
                                }
                            }
                        }
                    }
                    if (!selectedDinner.isEmpty()) {
                        for (Meal d : selectedDinner) {
                            if (d != null) {
                                if (!d.mealName.equalsIgnoreCase("null")) {
                                    if (!d.mealName.isEmpty()) {
                                        finalMeals.add(d.mealName);
                                    }
                                }
                            }
                        }
                    }
                    if (!selectedSnack.isEmpty()) {
                        for (Meal s : selectedSnack) {
                            if (s != null) {
                                if (!s.mealName.equalsIgnoreCase("null")) {
                                    if (!s.mealName.isEmpty()) {
                                        finalMeals.add(s.mealName);
                                    }
                                }
                            }
                        }
                    }
*/
                    /*// gets list data from the database
                    List<com.example.nutrimons.database.Meal> breakfastTemp;
                    try {
                        breakfastTemp = (temp.getBreakfast() == null ?
                                (temp.getBreakfast().isEmpty() ? null : temp.getBreakfast()) : temp.getBreakfast());
                    } catch (NullPointerException e) {
                        breakfastTemp = null;
                    }
                    List<com.example.nutrimons.database.Meal> lunchTemp;
                    try {
                        lunchTemp = (temp.getLunch() == null ?
                                (temp.getLunch().isEmpty() ? null : temp.getLunch()) : temp.getLunch());
                    } catch (NullPointerException e) {
                        lunchTemp = null;
                    }
                    List<com.example.nutrimons.database.Meal> dinnerTemp;
                    try {
                        dinnerTemp = (temp.getDinner() == null ?
                                (temp.getDinner().isEmpty() ? null : temp.getDinner()) : temp.getDinner());
                    } catch (NullPointerException e) {
                        dinnerTemp = null;
                    }
                    List<com.example.nutrimons.database.Meal> snackTemp;
                    try {
                        snackTemp = (temp.getSnack() == null ?
                                (temp.getSnack().isEmpty() ? null : temp.getSnack()) : temp.getSnack());
                    } catch (NullPointerException e) {
                        snackTemp = null;
                    }
                    if (selectedBreakfast.isEmpty() && selectedLunch.isEmpty() && selectedDinner.isEmpty() && selectedSnack.isEmpty()
                            && breakfastTemp == null && lunchTemp == null && dinnerTemp == null && snackTemp == null) {
                        finalMeals.add("No meals were inputted.");
                    } else {
                        // combines meals into one array
                        if (breakfastTemp != null) {
                            if (!breakfastTemp.isEmpty()) {
                                for (com.example.nutrimons.database.Meal b : breakfastTemp) {
                                    if (b != null) {
                                        if (!b.mealName.equalsIgnoreCase("null")) {
                                            if (!b.mealName.isEmpty()) {
                                                finalMeals.add(b.mealName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (lunchTemp != null) {
                            if (!lunchTemp.isEmpty()) {
                                for (com.example.nutrimons.database.Meal l : lunchTemp) {
                                    if (l != null) {
                                        if (!l.mealName.equalsIgnoreCase("null")) {
                                            if (!l.mealName.isEmpty()) {
                                                finalMeals.add(l.mealName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (dinnerTemp != null) {
                            if (!dinnerTemp.isEmpty()) {
                                for (com.example.nutrimons.database.Meal d : dinnerTemp) {
                                    if (d != null) {
                                        if (!d.mealName.equalsIgnoreCase("null")) {
                                            if (!d.mealName.isEmpty()) {
                                                finalMeals.add(d.mealName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (snackTemp != null) {
                            if (!snackTemp.isEmpty()) {
                                for (com.example.nutrimons.database.Meal s : snackTemp) {
                                    if (s != null) {
                                        if (!s.mealName.equalsIgnoreCase("null")) {
                                            if (!s.mealName.isEmpty()) {
                                                finalMeals.add(s.mealName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (!selectedBreakfast.isEmpty()) {
                            for (Meal b : selectedBreakfast) {
                                if (b != null) {
                                    if (!b.mealName.equalsIgnoreCase("null")) {
                                        if (!b.mealName.isEmpty()) {
                                            finalMeals.add(b.mealName);
                                        }
                                    }
                                }
                            }
                        }
                        if (!selectedLunch.isEmpty()) {
                            for (Meal l : selectedLunch) {
                                if (l != null) {
                                    if (!l.mealName.equalsIgnoreCase("null")) {
                                        if (!l.mealName.isEmpty()) {
                                            finalMeals.add(l.mealName);
                                        }
                                    }
                                }
                            }
                        }
                        if (!selectedDinner.isEmpty()) {
                            for (Meal d : selectedDinner) {
                                if (d != null) {
                                    if (!d.mealName.equalsIgnoreCase("null")) {
                                        if (!d.mealName.isEmpty()) {
                                            finalMeals.add(d.mealName);
                                        }
                                    }
                                }
                            }
                        }
                        if (!selectedSnack.isEmpty()) {
                            for (Meal s : selectedSnack) {
                                if (s != null) {
                                    if (!s.mealName.equalsIgnoreCase("null")) {
                                        if (!s.mealName.isEmpty()) {
                                            finalMeals.add(s.mealName);
                                        }
                                    }
                                }
                            }
                        }
                    }*/
                   /* if (mDb.dateDataDao().findByDate(finalDateString).todayExercise != null) {
                        if (!mDb.dateDataDao().findByDate(finalDateString).todayExercise.isEmpty()) {
                            for (String s : mDb.dateDataDao().findByDate(finalDateString).todayExercise) {
                                if (!s.equalsIgnoreCase("null")) {
                                    finalExercises.add(s);
                                }
                            }
                        }
                    }else{
                        finalExercises.add("No exercises were inputted.");
                    }*/
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

                //reward user
                DateData dd = mDb.dateDataDao().findByDate(finalDateString);
                User u = mDb.userDao().findByUserID(mDb.tokenDao().getUserID());
                for(int i = 0; i < dd.nutrientsToFloatList().size(); ++i)
                {
                    if (dd.nutrientsToFloatList().get(i) / u.DRIToFloatList().get(i) >= 1)
                    {
                            u.nutriCoins += 1;
                    }
                }
                mDb.userDao().insert(u);

                dd.aggregateNutrients();
                List<Float> nuts = dd.nutrientsToFloatList();
                List<String> nutStrs = dd.nutrientsToStringList();
                List<Float> nutrientDRIs = u.DRIToFloatList();
                List<Float> nutrientULs = u.ULToFloatList();
                //getContext().startService(new Intent(getContext(), NotificationService.class));

                for(int i = 0; i < nuts.size(); ++i)
                {

                    if(nuts.get(i) / nutrientULs.get(i) >= 1)
                    {
                        getContext().startService( new Intent( getContext(), NotificationService.class )) ;
                        Toast.makeText(getContext(), "DANGER: You have consumed a critical amount of '"
                                + nutStrs.get(i) + "'. Eating any more can be a long-term detriment to your health. " +
                                "See Nutrient Overview for more details", Toast.LENGTH_LONG).show();
                        //ns.notifyLimit("UL");
                    }
                    else if(nuts.get(i) / nutrientDRIs.get(i) >= 1)
                    {
                            Toast.makeText(getContext(), "Notification: You have reached your daily limit of '"
                                    + nutStrs.get(i) + "'. See Nutrient Overview for more details", Toast.LENGTH_SHORT).show();
                            //ns.notifyLimit("DRI");
                    }

                }

                long date = System.currentTimeMillis();
                SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                String currentDate = Date.format(date);
                if (!finalDateString.equalsIgnoreCase(currentDate)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", finalDateString);
/*                    if (!finalMeals.contains("No meals were inputted.")) {
                        bundle.putStringArrayList("meals", finalMeals);
                    }
                    if (!finalExercises.contains("No exercises were inputted.")) {
                        bundle.putStringArrayList("exercises", finalExercises);
                    }*/
                    DailyInfoFragment fragment = new DailyInfoFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_meal_plan, fragment).addToBackStack(null).commit();
                } else {
                    // navigates to the dashboard
                    //Navigation.findNavController(view).navigate(R.id.action_nav_mealPlan_to_nav_home);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", finalDateString);
                    Dashboard fragment = new Dashboard();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
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
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (MealPlan.OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String title);
    }
}