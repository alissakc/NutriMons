package com.example.nutrimons;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Meal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Meal extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    Button gotoScanBarcode, gotoAddMeal, gotoMealPlan;

    public Meal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Meal.
     */
    // TODO: Rename and change types and number of parameters
    public static Meal newInstance(String param1, String param2) {
        Meal fragment = new Meal();
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
        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        // Button initialization
        gotoScanBarcode = view.findViewById(R.id.scanBarcodeButton);
        gotoAddMeal = view.findViewById(R.id.addMealButton);
        gotoMealPlan = view.findViewById(R.id.mealPlanButton);

        // assign listener for buttons
        gotoScanBarcode.setOnClickListener(this);
        gotoAddMeal.setOnClickListener(this);
        gotoMealPlan.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.findViewById(R.id.scanBarcodeButton).equals(view)) {
            Navigation.findNavController(view).navigate(R.id.action_nav_meal_to_nav_scanBarcode);
        } else if (view.findViewById(R.id.addMealButton).equals(view)) {
            Navigation.findNavController(view).navigate(R.id.action_nav_meal_to_nav_addMeal);
        } else if (view.findViewById(R.id.mealPlanButton).equals(view)) {
            Navigation.findNavController(view).navigate(R.id.action_nav_meal_to_nav_mealPlan);
        }
    }

}