package com.example.nutrimons;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyInfoFragment extends Fragment{

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
        if(bundle == null){
            // gets current date
            long date = System.currentTimeMillis();
            SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
            String dateString = Date.format(date);
            currentDate.setText(dateString);
        }else{
            String date = bundle.getString("key");
            currentDate.setText(date);
        }

        String currentDay = currentDate.getText().toString();
        if(currentDay.indexOf(2) != '/'){
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

            List<String> tempBreakfast = (List<String>) temp.breakfastToListString();
            List<String> tempLunch = (List<String>) temp.lunchToListString();
            List<String> tempDinner = (List<String>) temp.dinnerToListString();
            List<String> tempSnack = (List<String>) temp.snackToListString();

            // gets list data from the database
            List<String> breakfast = (tempBreakfast.isEmpty() ? null : tempBreakfast);
            List<String> lunch = (tempLunch.isEmpty() ? null : tempLunch);
            List<String> dinner = (tempDinner.isEmpty() ? null : tempDinner);
            List<String> snack = (tempSnack.isEmpty() ? null : tempSnack);

            // combines meals into one array
            List<String> meals = new ArrayList<>();
            if (breakfast == null && lunch  == null && dinner  == null && snack  == null) {
                meals.add("No meals were inputted.");
            }
            else {
                if (!breakfast.isEmpty()) {
                    for (String b : breakfast){
                        b = b.replaceAll("\\W", "");
                        meals.add(b);
                    }
                }
                if (!lunch.isEmpty()) {
                    for (String l : lunch){
                        l = l.replaceAll("\\W", "");
                        meals.add(l);
                    }
                }
                if (!dinner.isEmpty()) {
                    for (String d : dinner){
                        d = d.replaceAll("\\W", "");
                        meals.add(d);
                    }
                }
                if (!snack.isEmpty()) {
                    for (String s : snack){
                        s = s.replaceAll("\\W", "");
                        meals.add(s);
                    }
                }
            }

            mealRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mealRecyclerView.setHasFixedSize(false);

            MealAdapter mealAdapter = new MealAdapter(meals);
            mealRecyclerView.setAdapter(mealAdapter);

            exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            exerciseRecyclerView.setHasFixedSize(false);

            List<String> tempExercise = (List<String>) mDb.dateDataDao().findExercisesByDate(currentDay);
            // gets list data from the database
            List<String> exercise = (tempExercise.isEmpty() ? null : tempExercise);
            if(exercise == null){
                exercise = new ArrayList<>();
                exercise.add("No exercises were inputted.");
            }else{
                exercise = new ArrayList<>();
                for (String e : tempExercise){
                    e = e.replaceAll("\\W", "");
                    exercise.add(e);
                }
            }

            ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercise);
            exerciseRecyclerView.setAdapter(exerciseAdapter);


            dailySummaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dailySummaryRecyclerView.setHasFixedSize(false);

            temp.aggregateNutrients();
            List<String> dailySummary = (List<String>) temp.dailySummaryList(); //should never be empty

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

                if(actionBar != null)
                {
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
                if(actionBar != null)
                {
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

        return view;
    }
}
