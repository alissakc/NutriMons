package com.example.nutrimons;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;

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
        }

        String currentDay = currentDate.getText().toString();
        if (currentDay.indexOf(2) != '/') {
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

        /*
        final Observer<List<String>> mealObserver = new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (mDb.dateDataDao().findByDate(currentDay) != null) {
                    // gets list data from the database
                    List<String> breakfast = (((List<String>) mDb.dateDataDao().findBreakfastByDate(currentDay).getValue() != null)
                            ? (List<String>) mDb.dateDataDao().findBreakfastByDate(currentDay).getValue() : null);
                    List<String> lunch = (((List<String>) mDb.dateDataDao().findLunchByDate(currentDay).getValue() != null)
                            ? (List<String>) mDb.dateDataDao().findLunchByDate(currentDay).getValue() : null);
                    List<String> dinner = (((List<String>) mDb.dateDataDao().findDinnerByDate(currentDay).getValue() != null)
                            ? (List<String>) mDb.dateDataDao().findDinnerByDate(currentDay).getValue() : null);
                    List<String> snack = (((List<String>) mDb.dateDataDao().findSnackByDate(currentDay).getValue() != null)
                            ? (List<String>) mDb.dateDataDao().findSnackByDate(currentDay).getValue() : null);

                    // combines meals into one array
                    List<String> meals = new ArrayList<>();
                    if (breakfast != null) {
                        meals.addAll(breakfast);
                    }
                    if (lunch != null) {
                        meals.addAll(lunch);
                    }
                    if (dinner != null) {
                        meals.addAll(dinner);
                    }
                    if (snack != null) {
                        meals.addAll(snack);
                    }

                    // Update the UI, in this case, a TextView.
                    MealAdapter mealAdapter = new MealAdapter(meals);
                    mealRecyclerView.setAdapter(mealAdapter);
                }
            }
        };


        final Observer<List<String>> exerciseObserver = new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (mDb.dateDataDao().findByDate(currentDay) != null) {
                    // gets list data from the database
                    List<String> exercise = (((List<String>) mDb.dateDataDao().findExercisesByDate(currentDay).getValue() != null)
                            ? (List<String>) mDb.dateDataDao().findExercisesByDate(currentDay).getValue() : null);

                    // Update the UI, in this case, a TextView.
                    ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercise);
                    exerciseRecyclerView.setAdapter(exerciseAdapter);
                }
            }
        };

        */

        /*
        if (mDb.dateDataDao().findByDate(currentDay) == null) {
            LinearLayout mealLayout = view.findViewById(R.id.mealLinearView);
            LinearLayout exerciseLayout = view.findViewById(R.id.exerciseLinearView);
            mealLayout.setVisibility(View.GONE);
            exerciseLayout.setVisibility(View.GONE);
        }
        else {
            LinearLayout mealLayout = view.findViewById(R.id.mealLinearView);
            LinearLayout exerciseLayout = view.findViewById(R.id.exerciseLinearView);
            mealRecyclerView = view.findViewById(R.id.dailyMealList);
            exerciseRecyclerView = view.findViewById(R.id.dailyExerciseList);
            mealLayout.setVisibility(View.VISIBLE);
            exerciseLayout.setVisibility(View.VISIBLE);

            // gets list data from the database
            List<String> breakfast = (((List<String>) mDb.dateDataDao().findBreakfastByDate(currentDay).getValue() != null)
                    ? (List<String>) mDb.dateDataDao().findBreakfastByDate(currentDay).getValue() : null);
            List<String> lunch = (((List<String>) mDb.dateDataDao().findLunchByDate(currentDay).getValue() != null)
                    ? (List<String>) mDb.dateDataDao().findLunchByDate(currentDay).getValue() : null);
            List<String> dinner = (((List<String>) mDb.dateDataDao().findDinnerByDate(currentDay).getValue() != null)
                    ? (List<String>) mDb.dateDataDao().findDinnerByDate(currentDay).getValue() : null);
            List<String> snack = (((List<String>) mDb.dateDataDao().findSnackByDate(currentDay).getValue() != null)
                    ? (List<String>) mDb.dateDataDao().findSnackByDate(currentDay).getValue() : null);

            // combines meals into one array
            List<String> meals = new ArrayList<>();
            if (breakfast != null) {
                meals.addAll(breakfast);
            }
            if (lunch != null) {
                meals.addAll(lunch);
            }
            if (dinner != null) {
                meals.addAll(dinner);
            }
            if (snack != null) {
                meals.addAll(snack);
            }

            // Update the UI, in this case, a TextView.
            MealAdapter mealAdapter = new MealAdapter(meals);
            mealRecyclerView.setAdapter(mealAdapter);

            exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            exerciseRecyclerView.setHasFixedSize(false);

            // gets list data from the database
            List<String> exercise = (((List<String>) mDb.dateDataDao().findExercisesByDate(currentDay).getValue() != null)
                    ? (List<String>) mDb.dateDataDao().findExercisesByDate(currentDay).getValue() : null);

            ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercise);
            exerciseRecyclerView.setAdapter(exerciseAdapter);
        }
        */

        //try {
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

        List<String> tempBreakfast = (List<String>) mDb.dateDataDao().findBreakfastByDate(currentDay);
        List<String> tempLunch = (List<String>) mDb.dateDataDao().findLunchByDate(currentDay);
        List<String> tempDinner = (List<String>) mDb.dateDataDao().findDinnerByDate(currentDay);
        List<String> tempSnack = (List<String>) mDb.dateDataDao().findSnackByDate(currentDay);

        // gets list data from the database
        List<String> breakfast = (tempBreakfast.isEmpty() ? null : tempBreakfast);
        List<String> lunch = (tempLunch.isEmpty() ? null : tempLunch);
        List<String> dinner = (tempDinner.isEmpty() ? null : tempDinner);
        List<String> snack = (tempSnack.isEmpty() ? null : tempSnack);

        // combines meals into one array
        List<String> meals = new ArrayList<>();
        if (breakfast == null && lunch == null && dinner == null && snack == null) {
            meals.add("No meals were inputted.");
        } else {
            if (breakfast != null) {
                if(!breakfast.isEmpty()){
                    String[] tempB;
                    for (String b : breakfast) {
                        if (!b.equalsIgnoreCase("[null]")) {
                            if(!b.equalsIgnoreCase("[]")){
                                tempB = b.split(Pattern.quote("}"));
                                for(int i = 0; i < tempB.length; i++){
                                    if(!tempB[i].replaceAll("\\W", "").equalsIgnoreCase("null")){
                                        if(!tempB[i].replaceAll("\\W", "").equalsIgnoreCase("")){
                                            int k = tempB[i].indexOf("mealName");
                                            int j = tempB[i].indexOf("monounsaturatedFat");
                                            String ss = tempB[i].substring(k+11,j-3);
                                            meals.add(ss);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (lunch != null) {
                if(!lunch.isEmpty()){
                    String[] tempL;
                    for (String l : lunch) {
                        if (!l.equalsIgnoreCase("[null]")) {
                            if(!l.equalsIgnoreCase("[]")){
                                tempL = l.split(Pattern.quote("}"));
                                for(int i = 0; i < tempL.length; i++){
                                    if(!tempL[i].replaceAll("\\W", "").equalsIgnoreCase("null")){
                                        if(!tempL[i].replaceAll("\\W", "").equalsIgnoreCase("")){
                                            int k = tempL[i].indexOf("mealName");
                                            int j = tempL[i].indexOf("monounsaturatedFat");
                                            String ss = tempL[i].substring(k+11,j-3);
                                            meals.add(ss);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (dinner != null) {
                if(!dinner.isEmpty()){
                    String[] tempD;
                    for (String d : dinner) {
                        if (!d.equalsIgnoreCase("[null]")) {
                            if(!d.equalsIgnoreCase("[]")){
                                tempD = d.split(Pattern.quote("}"));
                                for(int i = 0; i < tempD.length; i++){
                                    if(!tempD[i].replaceAll("\\W", "").equalsIgnoreCase("null")){
                                        if(!tempD[i].replaceAll("\\W", "").equalsIgnoreCase("")){
                                            int k = tempD[i].indexOf("mealName");
                                            int j = tempD[i].indexOf("monounsaturatedFat");
                                            String ss = tempD[i].substring(k+11,j-3);
                                            meals.add(ss);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (snack != null) {
                if(!snack.isEmpty()){
                    String[] tempS;
                    for (String s : snack) {
                        if (!s.equalsIgnoreCase("[null]")) {
                            if(!s.equalsIgnoreCase("[]")){
                                tempS = s.split(Pattern.quote("}"));
                                for(int i = 0; i < tempS.length; i++){
                                    if(!tempS[i].replaceAll("\\W", "").equalsIgnoreCase("null")){
                                        if(!tempS[i].replaceAll("\\W", "").equalsIgnoreCase("")){
                                            int k = tempS[i].indexOf("mealName");
                                            int j = tempS[i].indexOf("monounsaturatedFat");
                                            String ss = tempS[i].substring(k+11,j-3);
                                            meals.add(ss);
                                        }
                                    }
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
                        for(int i = 0; i < tempE.length; i++){
                            e = tempE[i].replaceAll("\\W", "");
                            exercise.add(e);
                        }
                    }
                }
            }
        }

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercise);
        exerciseRecyclerView.setAdapter(exerciseAdapter);


        dailySummaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dailySummaryRecyclerView.setHasFixedSize(false);

        List<String> dailySummary;
        if (temp != null) {
            dailySummaryLayout.setVisibility(View.VISIBLE);
            temp.aggregateNutrients();
            dailySummary = (List<String>) temp.nutrientsToStringList();
        } else {
            dailySummary = new ArrayList<>();
            dailySummary.add("No meals were inputted.");
            dailySummaryLayout.setVisibility(View.GONE);
        }

        DailySummaryAdapter dailySummaryAdapter = new DailySummaryAdapter(dailySummary);
        dailySummaryRecyclerView.setAdapter(dailySummaryAdapter);

        /*
        } catch (NullPointerException e) {
            LinearLayout mealLayout = view.findViewById(R.id.mealLinearView);
            LinearLayout exerciseLayout = view.findViewById(R.id.exerciseLinearView);
            mealLayout.setVisibility(View.GONE);
            exerciseLayout.setVisibility(View.GONE);
            Log.d("i am here", "I am here");
        }
        */

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
                ActionBar actionBar = getActivity().getActionBar();

                if (actionBar != null) {
                    actionBar.setTitle("Dashboard");
                }
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
                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setTitle("Calendar");
                }
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
                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setTitle("Meal Plan");
                }
                transaction.replace(R.id.fragment_daily_info, fragment).addToBackStack(null).commit();
            }
        });

        // button initialization for removing meal
        removeMealButton = view.findViewById(R.id.removeMealFromDailyInfo);
        removeMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setTitle("Exercise");
                }
                transaction.replace(R.id.fragment_daily_info, fragment).addToBackStack(null).commit();
            }
        });

        // button initialization for removing exercise
        removeExerciseButton = view.findViewById(R.id.removeExerciseFromDailyInfo);
        removeExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
