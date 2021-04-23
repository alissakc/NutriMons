package com.example.nutrimons;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private RecyclerView mealRecyclerView, exerciseRecyclerView, dailySummaryRecyclerView;
    private ImageView gotToDashboard, goToPreviousDate, goToNextDate;
    private ImageButton addMealButton, removeMealButton, addExerciseButton, removeExerciseButton;
    private List<String> addedMeals, addedExercises;
    private OnFragmentInteractionListener mListener;

    // vars for calendar
    private TextView currentDate;
    private ImageButton goToCalendar;

    // creates instance of database
    private AppDatabase mDb;

    public DailyInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyInfoFragment newInstance(String param1, String param2) {
        DailyInfoFragment fragment = new DailyInfoFragment();
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

        if (mListener != null) {
            mListener.onFragmentInteraction("Daily Information");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_info, container, false);

        // database
        mDb = AppDatabase.getInstance(getContext());

        // calendar
        currentDate = view.findViewById(R.id.dailyCurrentDateTextView);
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            // gets current date
            long date = System.currentTimeMillis();
            SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
            String dateString = Date.format(date);
            currentDate.setText(dateString);
        } else {
            String date = bundle.getString("key");
            currentDate.setText(date);
            addedMeals = bundle.getStringArrayList("meals");
            addedExercises = bundle.getStringArrayList("exercises");
        }

        String currentDay = currentDate.getText().toString();
        if (currentDay.charAt(2) != '/') {
            currentDay = "0" + currentDay;
        }

        /*
        String currentDateString = "";
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = format.parse(currentDay);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            currentDateString = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */

        mealRecyclerView = view.findViewById(R.id.dailyMealList);
        exerciseRecyclerView = view.findViewById(R.id.dailyExerciseList);
        dailySummaryRecyclerView = view.findViewById(R.id.dailySummary);

        mealRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealRecyclerView.setHasFixedSize(false);

        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseRecyclerView.setHasFixedSize(false);

        dailySummaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dailySummaryRecyclerView.setHasFixedSize(false);

        DateData temp = mDb.dateDataDao().findByDate(currentDay);
        LinearLayout mealLayout = view.findViewById(R.id.mealLinearView);
        LinearLayout exerciseLayout = view.findViewById(R.id.exerciseLinearView);
        LinearLayout dailySummaryLayout = view.findViewById(R.id.dailySummaryLinearView);
        mealRecyclerView = view.findViewById(R.id.dailyMealList);
        exerciseRecyclerView = view.findViewById(R.id.dailyExerciseList);
        dailySummaryRecyclerView = view.findViewById(R.id.dailySummary);
        mealLayout.setVisibility(View.VISIBLE);
        exerciseLayout.setVisibility(View.VISIBLE);
        dailySummaryLayout.setVisibility(View.VISIBLE);

        TextView noMeals = view.findViewById(R.id.meal_no_items);
        TextView noExercises = view.findViewById(R.id.exercise_no_items);
        noMeals.setVisibility(View.GONE);
        noExercises.setVisibility(View.GONE);

        // gets list data from the database
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

        // combines meals into one array
        List<String> meals = new ArrayList<>();
        if (breakfast == null && lunch == null && dinner == null && snack == null) {
            meals.add("No meals were inputted.");
        } else {
            if (breakfast != null) {
                if (!breakfast.isEmpty()) {
                    for (com.example.nutrimons.database.Meal b : breakfast) {
                        if (b != null) {
                            if (!b.mealName.equalsIgnoreCase("null")) {
                                if (!b.mealName.isEmpty()) {
                                    meals.add(b.mealName);
                                }
                            }
                        }
                    }
                }
            }
            if (lunch != null) {
                if (!lunch.isEmpty()) {
                    for (com.example.nutrimons.database.Meal l : lunch) {
                        if (l != null) {
                            if (!l.mealName.equalsIgnoreCase("null")) {
                                if (!l.mealName.isEmpty()) {
                                    meals.add(l.mealName);
                                }
                            }
                        }
                    }
                }
            }
            if (dinner != null) {
                if (!dinner.isEmpty()) {
                    for (com.example.nutrimons.database.Meal d : dinner) {
                        if (d != null) {
                            if (!d.mealName.equalsIgnoreCase("null")) {
                                if (!d.mealName.isEmpty()) {
                                    meals.add(d.mealName);
                                }
                            }
                        }
                    }
                }
            }
            if (snack != null) {
                if (!snack.isEmpty()) {
                    for (com.example.nutrimons.database.Meal s : snack) {
                        if (s != null) {
                            if (!s.mealName.equalsIgnoreCase("null")) {
                                if (!s.mealName.isEmpty()) {
                                    meals.add(s.mealName);
                                }
                            }
                        }
                    }
                }
            }
        }

        mealRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealRecyclerView.setHasFixedSize(false);

        MealAdapter mealAdapter = new MealAdapter(meals);
        mealRecyclerView.setAdapter(mealAdapter);

        if(meals.contains("No meals were inputted.")){
            mealRecyclerView.setVisibility(View.GONE);
            noMeals.setVisibility(View.VISIBLE);
        }else if(meals.isEmpty()){
            mealRecyclerView.setVisibility(View.GONE);
            noMeals.setVisibility(View.VISIBLE);
        }
        /*ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new MealAdapter.SwipeToDeleteMealCallback(mealAdapter));
        itemTouchHelper.attachToRecyclerView(mealRecyclerView);*/

        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseRecyclerView.setHasFixedSize(false);

        List<String> tempExercise = (List<String>) mDb.dateDataDao().findExercisesByDate(currentDay);

        // gets list data from the database
        List<String> exercise = (tempExercise.isEmpty() ? null : tempExercise);
        if (exercise == null) {
            exercise = new ArrayList<>();
            exercise.add("No exercises were inputted.");
        } else {
            exercise = new ArrayList<>();
            String[] tempE;
            for (String e : tempExercise) {
                if (!e.equalsIgnoreCase("[null]")) {
                    if (!e.equalsIgnoreCase("[]")) {
                        tempE = e.split(",");
                        for (int i = 0; i < tempE.length; i++) {
                            e = tempE[i].replaceAll("\\W", "");
                            exercise.add(e);
                        }
                    }else{
                        exercise = new ArrayList<>();
                        exercise.add("No exercises were inputted.");
                    }
                }else{
                    exercise = new ArrayList<>();
                    exercise.add("No exercises were inputted.");
                }
            }
        }

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercise);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        if(exercise.contains("No exercises were inputted.")){
            exerciseRecyclerView.setVisibility(View.GONE);
            noExercises.setVisibility(View.VISIBLE);
        }

        dailySummaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dailySummaryRecyclerView.setHasFixedSize(false);

        List<String> dailySummary;
        if (temp != null) {
            if(temp.getSnack().isEmpty() && temp.getDinner().isEmpty() && temp.getLunch().isEmpty() && temp.getBreakfast().isEmpty()){
                dailySummary = new ArrayList<>();
                dailySummary.add("No meals were inputted.");
                dailySummaryLayout.setVisibility(View.GONE);
            }else{
                dailySummaryLayout.setVisibility(View.VISIBLE);
                temp.aggregateNutrients();
                dailySummary = (List<String>) temp.nutrientsToStringList();
            }
        } else {
            dailySummary = new ArrayList<>();
            dailySummary.add("No meals were inputted.");
            dailySummaryLayout.setVisibility(View.GONE);
        }

        DailySummaryAdapter dailySummaryAdapter = new DailySummaryAdapter(dailySummary);
        dailySummaryRecyclerView.setAdapter(dailySummaryAdapter);

        // go to dashboard
        gotToDashboard = view.findViewById(R.id.dailyToDashboardImageButton);
        gotToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", currentDate.getText().toString());
                Dashboard fragment = new Dashboard();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_dashboard, fragment).addToBackStack(null).commit();
            }
        });

        // button that goes to the calendar view
        goToCalendar = view.findViewById(R.id.dailyCalendarImageButton);
        goToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String date = currentDate.getText().toString();
                bundle.putString("currentDateKey", date);
                CalendarViewFragment fragment = new CalendarViewFragment();
                fragment.setArguments(bundle);
                fragment.setArguments(savedInstanceState);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_daily_info, fragment).addToBackStack(null).commit();
            }
        });

        // button initialization for previous day
        goToPreviousDate = view.findViewById(R.id.dailyPreviousDayImageButton);
        goToPreviousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDay = currentDate.getText().toString();
                try {
                    Date currentDateVal = new SimpleDateFormat("MM/dd/yyyy").parse(currentDay);
                    Date yesterday = new Date(currentDateVal.getTime() - (1000 * 60 * 60 * 24));
                    SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                    String dateString = Date.format(yesterday);
                    currentDate.setText(dateString);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", dateString);
                    DailyInfoFragment fragment = new DailyInfoFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_daily_info, fragment).addToBackStack(null).commit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        // button initialization for next day
        goToNextDate = view.findViewById(R.id.dailyNextDayImageButton);
        goToNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDay = currentDate.getText().toString();
                try {
                    Date currentDateVal = new SimpleDateFormat("MM/dd/yyyy").parse(currentDay);
                    Date tomorrow = new Date(currentDateVal.getTime() + (1000 * 60 * 60 * 24));
                    SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                    String dateString = Date.format(tomorrow);
                    currentDate.setText(dateString);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", dateString);
                    DailyInfoFragment fragment = new DailyInfoFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_daily_info, fragment).addToBackStack(null).commit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // button initialization for adding meal
        addMealButton = view.findViewById(R.id.addMealToDailyInfo);
        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", currentDate.getText().toString());
                MealPlan fragment = new MealPlan();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_daily_info, fragment).addToBackStack(null).commit();
            }
        });

        // button initialization for removing meal
        removeMealButton = view.findViewById(R.id.removeMealFromDailyInfo);
        List<com.example.nutrimons.database.Meal> finalBreakfast = breakfast;
        List<com.example.nutrimons.database.Meal> finalLunch = lunch;
        List<com.example.nutrimons.database.Meal> finalDinner = dinner;
        List<com.example.nutrimons.database.Meal> finalSnack = snack;

        String finalCurrentDay = currentDay;
        removeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray selectedRows = mealAdapter.getSelectedIds();
                int breakfastIndexes = -1;
                int lunchIndexes = -1;
                int dinnerIndexes = -1;
                int snackIndexes = -1;
                if(!finalBreakfast.isEmpty()){
                    breakfastIndexes = finalBreakfast.size();
                }
                if(!finalLunch.isEmpty()){
                    lunchIndexes = ((finalBreakfast.isEmpty() ? 0 : finalBreakfast.size())
                            + finalLunch.size());
                }
                if(!finalDinner.isEmpty()){
                    dinnerIndexes = ((finalBreakfast.isEmpty() ? 0 : finalBreakfast.size())
                            + (finalLunch.isEmpty() ? 0 : finalLunch.size())
                            + finalDinner.size());
                }
                if(!finalSnack.isEmpty()){
                    snackIndexes = ((finalBreakfast.isEmpty() ? 0 : finalBreakfast.size())
                            + (finalLunch.isEmpty() ? 0 : finalLunch.size())
                            + (finalDinner.isEmpty() ? 0 : finalDinner.size())
                            + finalSnack.size());
                }
                if (selectedRows.size() > 0) {
                    for (int i = (selectedRows.size() - 1); i >= 0; i--) {
                        if (selectedRows.valueAt(i)) {
                            meals.remove(selectedRows.keyAt(i));
                            if(breakfastIndexes != -1 && selectedRows.keyAt(i) < breakfastIndexes){
                                finalBreakfast.remove(selectedRows.keyAt(i));
                            }else if(lunchIndexes != -1 && selectedRows.keyAt(i) < lunchIndexes){
                                finalLunch.remove((lunchIndexes-1)-selectedRows.keyAt(i));
                            }else if(dinnerIndexes != -1 && selectedRows.keyAt(i) < dinnerIndexes){
                                finalDinner.remove((dinnerIndexes-1)-selectedRows.keyAt(i));
                            }else if(snackIndexes != -1 && selectedRows.keyAt(i) < snackIndexes){
                                finalSnack.remove((snackIndexes-1)-selectedRows.keyAt(i));
                            }
                        }
                    }
                    final com.example.nutrimons.database.DateData dateData =
                            new com.example.nutrimons.database.DateData(finalCurrentDay,
                            finalBreakfast, finalLunch, finalDinner, finalSnack,
                            mDb.dateDataDao().findByDate(finalCurrentDay).todayExercise,
                            mDb.dateDataDao().findByDate(finalCurrentDay).water,
                            mDb.dateDataDao().findByDate(finalCurrentDay).water_unit,
                            mDb.dateDataDao().findByDate(finalCurrentDay).coinsLeft);
                    mDb.dateDataDao().updateDateData(dateData);
                    mealAdapter.removeSelection();


                    dailySummaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    dailySummaryRecyclerView.setHasFixedSize(false);

                    List<String> dailySummary;
                    if (temp != null) {
                        dailySummaryLayout.setVisibility(View.VISIBLE);
                        dateData.aggregateNutrients();
                        dailySummary = (List<String>) dateData.nutrientsToStringList();
                    } else {
                        dailySummary = new ArrayList<>();
                        dailySummary.add("No meals were inputted.");
                        dailySummaryLayout.setVisibility(View.GONE);
                    }

                    DailySummaryAdapter dailySummaryAdapter = new DailySummaryAdapter(dailySummary);
                    dailySummaryRecyclerView.setAdapter(dailySummaryAdapter);
                }
            }
        });

        // button initialization for adding exercise
        addExerciseButton = view.findViewById(R.id.addExerciseToDailyInfo);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", currentDate.getText().toString());
                Exercise fragment = new Exercise();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_daily_info, fragment).addToBackStack(null).commit();
            }
        });

        // button initialization for removing exercise
        removeExerciseButton = view.findViewById(R.id.removeExerciseFromDailyInfo);
        List<String> finalExercise = exercise;
        removeExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ExerciseAdapter exerciseAdapterTemp = new ExerciseAdapter(finalExercise);
                SparseBooleanArray selectedRows = exerciseAdapter.getSelectedIds();
                if (selectedRows.size() > 0) {
                    for (int i = (selectedRows.size() - 1); i >= 0; i--) {
                        if (selectedRows.valueAt(i)) {
                            finalExercise.remove(selectedRows.keyAt(i));
                        }
                    }
                    final com.example.nutrimons.database.DateData dateData =
                            new com.example.nutrimons.database.DateData(finalCurrentDay,
                                    mDb.dateDataDao().findByDate(finalCurrentDay).breakfast,
                                    mDb.dateDataDao().findByDate(finalCurrentDay).lunch,
                                    mDb.dateDataDao().findByDate(finalCurrentDay).dinner,
                                    mDb.dateDataDao().findByDate(finalCurrentDay).snack,
                                    finalExercise, mDb.dateDataDao().findByDate(finalCurrentDay).water,
                                    mDb.dateDataDao().findByDate(finalCurrentDay).water_unit,
                                    mDb.dateDataDao().findByDate(finalCurrentDay).coinsLeft);
                    mDb.dateDataDao().updateDateData(dateData);
                    exerciseAdapter.removeSelection();
                }
                exerciseRecyclerView.setAdapter(exerciseAdapter);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
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
