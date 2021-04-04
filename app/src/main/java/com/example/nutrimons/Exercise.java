package com.example.nutrimons;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private Spinner exerciseSpinner;
    private Button addButton, save;
    private String exerciseName, unitName;
    private int caloriesPerUnit;
    private float duration;
    private EditText exerciseNameText, unitNameText, caloriesPerUnitText, durationText;
    private final List<String> exerciseList = new ArrayList<>();

    // creates instance of database
    private AppDatabase mDb;

    public Exercise() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Exercise.
     */
    // TODO: Rename and change types and number of parameters
    public static Exercise newInstance(String param1, String param2) {
        Exercise fragment = new Exercise();
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
        View view =  inflater.inflate(R.layout.fragment_exercise, container, false);

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

        List<String> exercises = (List<String>) mDb.exerciseDao().getAllNames();
        exercises.add(0, "Select an item");

        // Spinner element
        exerciseSpinner = (Spinner) view.findViewById(R.id.breakfastMultipleItemSelectionSpinner);

        // Spinner click listener
        exerciseSpinner.setOnItemSelectedListener(this);

        /*
        // Spinner Drop down elements
        List<String> exercises = new ArrayList<String>();
        exercises.add("Select an item");
        exercises.add("Aerobic");
        exercises.add("Basketball");
        exercises.add("Badminton");
        exercises.add("Baseball");
        exercises.add("Bench Press");
        exercises.add("Bicycling");
        exercises.add("Boxing");
        exercises.add("Soccer");
        exercises.add("Running");
        exercises.add("Volleyball");
        exercises.add("Walking");
        */

        // Creating adapter for spinners
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, exercises);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinners
        exerciseSpinner.setAdapter(dataAdapter);

        // Button initialization
        addButton = view.findViewById(R.id.submitNewExercise);

        // new exercise
        exerciseNameText = (EditText) view.findViewById(R.id.editTextExerciseName);
        caloriesPerUnitText = (EditText) view.findViewById(R.id.editTextCaloriesPerUnit);
        unitNameText = (EditText) view.findViewById(R.id.editTextUnitName);
        durationText = (EditText) view.findViewById(R.id.editTextDuration);

        String finalDateString = dateString;
        //assign listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseName = exerciseNameText.getText().toString();
                caloriesPerUnit = Integer.parseInt(caloriesPerUnitText.getText().toString());
                unitName = unitNameText.getText().toString();
                duration = Float.parseFloat(durationText.getText().toString());
                final com.example.nutrimons.database.Exercise exercise = new com.example.nutrimons.database.Exercise(exerciseName, caloriesPerUnit, unitName, duration);
                mDb.exerciseDao().insert(exercise);
                Toast.makeText(getContext(), "Created entry", Toast.LENGTH_SHORT).show();
                // refreshes exercise page
                Bundle bundle = new Bundle();
                bundle.putString("key", finalDateString);
                Exercise fragment = new Exercise();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_exercise, fragment).addToBackStack(null).commit();
            }
        });

        save = view.findViewById(R.id.saveExerciseButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.add(exerciseSpinner.getSelectedItem().toString());
                if(mDb.dateDataDao().findByDate(finalDateString) == null){
                    final DateData dateData = new DateData(finalDateString, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), exerciseList);
                    mDb.dateDataDao().insert(dateData);
                }
                else{
                    DateData dd = mDb.dateDataDao().findByDate(finalDateString);
                    if(mDb.dateDataDao().findExercisesByDate(finalDateString) != null){
                        if(!mDb.dateDataDao().findExercisesByDate(finalDateString).isEmpty()){
                            for (String s : mDb.dateDataDao().findExercisesByDate(finalDateString)) {
                                if (!s.equalsIgnoreCase("[null]")) {
                                    if (!s.equalsIgnoreCase("[]")) {
                                        s = s.replaceAll("\\W", "");
                                        exerciseList.add(s);
                                    }
                                }
                            }
                        }
                    }
                    final com.example.nutrimons.database.DateData dateData =
                            new com.example.nutrimons.database.DateData(finalDateString,
                                    dd.breakfast, dd.lunch, dd.dinner, dd.snack, exerciseList);
                    mDb.dateDataDao().updateDateData(dateData);
                    //mDb.dateDataDao().updateExercise(exerciseList, dateString);
                }

                long date = System.currentTimeMillis();
                SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                String currentDate = Date.format(date);
                if (!finalDateString .equalsIgnoreCase(currentDate)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", finalDateString);
                    DailyInfoFragment fragment = new DailyInfoFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    ActionBar actionBar = getActivity().getActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle("Daily Information");
                    }
                    transaction.replace(R.id.fragment_exercise, fragment).addToBackStack(null).commit();
                } else {
                    // refreshes exercise page
                    Bundle bundle = new Bundle();
                    bundle.putString("key", finalDateString);
                    Exercise fragment = new Exercise();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_exercise, fragment).addToBackStack(null).commit();
                }
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }


}