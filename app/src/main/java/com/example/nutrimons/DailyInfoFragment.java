package com.example.nutrimons;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrimons.database.AppDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private RecyclerView mealList, exerciseList;
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

        mealList = view.findViewById(R.id.dailyMealList);
        exerciseList = view.findViewById(R.id.dailyExerciseList);

        String currentDay = currentDate.getText().toString();
        if (mDb.dateDataDao().findByDate(currentDay) == null) {
            LinearLayout mealLayout = view.findViewById(R.id.mealLinearView);
            LinearLayout exerciseLayout = view.findViewById(R.id.exerciseLinearView);
            mealLayout.setVisibility(View.GONE);
            exerciseLayout.setVisibility(View.GONE);
        } else {
            LinearLayout mealLayout = view.findViewById(R.id.mealLinearView);
            LinearLayout exerciseLayout = view.findViewById(R.id.exerciseLinearView);
            mealList = view.findViewById(R.id.dailyMealList);
            exerciseList = view.findViewById(R.id.dailyExerciseList);
            mealLayout.setVisibility(View.VISIBLE);
            exerciseLayout.setVisibility(View.VISIBLE);

            List<String> breakfast = (((List<String>) mDb.dateDataDao().findBreakfastByDate(currentDay) != null)
                    ? (List<String>) mDb.dateDataDao().findBreakfastByDate(currentDay) : null);
            List<String> lunch = (((List<String>) mDb.dateDataDao().findLunchByDate(currentDay) != null)
                    ? (List<String>) mDb.dateDataDao().findLunchByDate(currentDay) : null);
            List<String> dinner = (((List<String>) mDb.dateDataDao().findDinnerByDate(currentDay) != null)
                    ? (List<String>) mDb.dateDataDao().findDinnerByDate(currentDay) : null);
            List<String> snack = (((List<String>) mDb.dateDataDao().findSnackByDate(currentDay) != null)
                    ? (List<String>) mDb.dateDataDao().findSnackByDate(currentDay) : null);

            List<String> exercise = (((List<String>) mDb.dateDataDao().findExercisesByDate(currentDay) != null)
                    ? (List<String>) mDb.dateDataDao().findExercisesByDate(currentDay) : null);



        }

        // go to dashboard
        gotToDashboard = view.findViewById(R.id.dailyToDashboardImageButton);
        gotToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
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
