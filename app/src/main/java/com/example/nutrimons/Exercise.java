package com.example.nutrimons;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;
import com.example.nutrimons.database.Meal;
import com.example.nutrimons.database.User;


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
    private OnFragmentInteractionListener mListener;

    // creates instance of database
    private AppDatabase mDb;

    private View view;

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
        if (mListener != null) {
            mListener.onFragmentInteraction("Exercise");
        }
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exercise, container, false);

        // database
        mDb = AppDatabase.getInstance(getContext());

        // checks if the there is a bundle from FragmentTransaction
        String dateString;
        Bundle bundle = this.getArguments();
        if (bundle == null) {
            // gets current date
            dateString = BAMM.getDateString();
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
                hideKeyboard();
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
                hideKeyboard();

                String exercise = exerciseSpinner.getSelectedItem().toString();
                if(exercise.equals("Select an item"))
                    return;
                exerciseList.add(exercise);
                Toast.makeText(getContext(), exercise + " added", Toast.LENGTH_SHORT).show();

                ArrayList<String> finalMeals = new ArrayList<>();
                ArrayList<String> finalExercises = new ArrayList<>();
                if (BAMM.getCurrentDateData() == null) {
                    final DateData dateData = new DateData(finalDateString, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), exerciseList);
                    mDb.dateDataDao().insert(dateData);
                } else {
                    DateData dd = BAMM.getCurrentDateData();
                    if (mDb.dateDataDao().findExercisesByDate(finalDateString) != null) {
                        if (!mDb.dateDataDao().findExercisesByDate(finalDateString).isEmpty()) {
                            for (String s : mDb.dateDataDao().findExercisesByDate(finalDateString)) {
                                if (!s.equalsIgnoreCase("[null]")) {
                                    if (!s.equalsIgnoreCase("[]")) {
                                        String[] temp = s.split("(?=\\p{Lu})");
                                        for(String t : temp){
                                            t = t.replaceAll("\\W", "");
                                            if(!t.equals("")){
                                                exerciseList.add(t);
                                                finalExercises.add(t);
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                    final DateData dateData =
                            new DateData(finalDateString,
                                    dd.breakfast, dd.lunch, dd.dinner, dd.snack, exerciseList);
                    mDb.dateDataDao().updateDateData(dateData);

                    // gets list data from the database
                    List<Meal> breakfast;
                    try {
                        breakfast = (dd.getBreakfast() == null ?
                                (dd.getBreakfast().isEmpty() ? null : dd.getBreakfast()) : dd.getBreakfast());
                    } catch (NullPointerException e) {
                        breakfast = null;
                    }
                    List<Meal> lunch;
                    try {
                        lunch = (dd.getLunch() == null ?
                                (dd.getLunch().isEmpty() ? null : dd.getLunch()) : dd.getLunch());
                    } catch (NullPointerException e) {
                        lunch = null;
                    }
                    List<Meal> dinner;
                    try {
                        dinner = (dd.getDinner() == null ?
                                (dd.getDinner().isEmpty() ? null : dd.getDinner()) : dd.getDinner());
                    } catch (NullPointerException e) {
                        dinner = null;
                    }
                    List<Meal> snack;
                    try {
                        snack = (dd.getSnack() == null ?
                                (dd.getSnack().isEmpty() ? null : dd.getSnack()) : dd.getSnack());
                    } catch (NullPointerException e) {
                        snack = null;
                    }

                    // combines meals into one array
                    if (breakfast == null && lunch == null && dinner == null && snack == null) {
                        finalMeals.add("No meals were inputted.");
                    } else {
                        if (breakfast != null) {
                            if (!breakfast.isEmpty()) {
                                for (Meal b : breakfast) {
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
                        if (lunch != null) {
                            if (!lunch.isEmpty()) {
                                for (Meal l : lunch) {
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
                        if (dinner != null) {
                            if (!dinner.isEmpty()) {
                                for (Meal d : dinner) {
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
                        if (snack != null) {
                            if (!snack.isEmpty()) {
                                for (Meal s : snack) {
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
                    }

                    List<String> tempExercise = (List<String>) mDb.dateDataDao().findExercisesByDate(finalDateString);
                    if (tempExercise.isEmpty() && exerciseList.isEmpty()) {
                        finalExercises.add("No exercises were inputted.");
                    } else {
                        String[] tempE;
                        for (String e : tempExercise) {
                            if (!e.equalsIgnoreCase("[null]")) {
                                if (!e.equalsIgnoreCase("[]")) {
                                    tempE = e.split(",");
                                    for (int i = 0; i < tempE.length; i++) {
                                        e = tempE[i].replaceAll("\\W", "");
                                        finalExercises.add(e);
                                    }
                                }
                            }
                        }
                    }
                    //mDb.dateDataDao().updateExercise(exerciseList, dateString);
                }

                String currentDate = BAMM.getDateString();
                if (!finalDateString.equalsIgnoreCase(currentDate)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", finalDateString);
                    /*if (!finalMeals.contains("No meals were inputted.")) {
                        bundle.putStringArrayList("meals", finalMeals);
                    }
                    if (!finalExercises.contains("No exercises were inputted.")) {
                        bundle.putStringArrayList("exercises", finalExercises);
                    }*/
                    DailyInfoFragment fragment = new DailyInfoFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
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

                //reward user
                User u = BAMM.getCurrentUser();
                u.nutriCoins += 1;
                mDb.userDao().insert(u);
            }
        });
        return view;
    }

    private void hideKeyboard()
    {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (Exercise.OnFragmentInteractionListener) context;
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