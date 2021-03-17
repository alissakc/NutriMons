package com.example.nutrimons;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.nutrimons.database.AppDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMeal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMeal extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private Button addButton, searchFDCButton;
    private String mealName;
    private int servingSize, servingWeight, caloriesPerServing;
    private EditText mealNameText, servingSizeText, servingWeightText, caloriesPerServingText;

    // creates instance of database
    private AppDatabase mDb;

    private View checkboxbrandedButton;
    private boolean isBranded;

    public AddMeal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMeal.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMeal newInstance(String param1, String param2) {
        AddMeal fragment = new AddMeal();
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
        View view = inflater.inflate(R.layout.fragment_add_meal, container, false);

        // database
        mDb = AppDatabase.getInstance(getContext());

        // Button initialization
        addButton = view.findViewById(R.id.submitNewMealButton);
        searchFDCButton = view.findViewById(R.id.searchFDCButton);

        // new meal
        mealNameText = (EditText) view.findViewById(R.id.editTextFoodName);
        servingSizeText = (EditText) view.findViewById(R.id.editTextServingSize);
        servingWeightText = (EditText) view.findViewById(R.id.editTextServingWeight);
        caloriesPerServingText = (EditText) view.findViewById(R.id.editTextCalories);

        checkboxbrandedButton = (CompoundButton) view.findViewById(R.id.checkboxBranded);
        checkboxbrandedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    isBranded = true;
                } else {
                    isBranded = false;
                }
            }
        });

        searchFDCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealName = mealNameText.getText().toString();
                Log.d("mealname", mealName);

                /*//FragmentManager fm = getFragmentManager();
                AddFromFDC fragment = new AddFromFDC();
                //Fragment addFromFDC = new Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("mealName", mealName);
                bundle.putBoolean("isBranded", isBranded);
                //addFromFDC.setArguments(bundle);
                fragment.setArguments(bundle);
                /*fm.beginTransaction()
                        .replace(placeholder, fragment, tabId)
                        .commit();*/
                AddFromFDC fragment = new AddFromFDC();
                fragment.mealName = mealName;
                fragment.isBranded = isBranded;
                Navigation.findNavController(view).navigate(R.id.nav_addFromFDC);
            }
        });

        //assign listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Created entry", Toast.LENGTH_SHORT).show();
                mealName = mealNameText.getText().toString();
                servingSize = Integer.parseInt(servingSizeText.getText().toString());
                servingWeight = Integer.parseInt(servingWeightText.getText().toString());
                caloriesPerServing = Integer.parseInt(caloriesPerServingText.getText().toString());

                final com.example.nutrimons.database.Meal meal = new com.example.nutrimons.database.Meal(mealName, servingSize, servingWeight, caloriesPerServing);
                mDb.mealDao().insert(meal);

                // navigates to meal plan
                Navigation.findNavController(view).navigate(R.id.action_nav_addMeal_to_nav_mealPlan);
            }
        });

        return view;
    }
}