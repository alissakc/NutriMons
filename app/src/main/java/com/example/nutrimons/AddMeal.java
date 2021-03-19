package com.example.nutrimons;

import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

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
    private Button addButton, searchFDCButton, advancedMealButton;
    private String mealName;
    private int servingSize, servingWeight, caloriesPerServing, advCalories, advWater, advProtein,
        advCarbohydrate, advSugar, advFiber, advCholesterol, advSaturatedFat, advMonounsaturatedFat,
        advPolyunsaturatedFat, advTransFat, advVitaminA, advVitaminC, advVitaminD, advSodium,
        advPotassium, advCalcium, advIron;
    private EditText mealNameText, servingSizeText, servingWeightText, caloriesPerServingText,
        advCaloriesText, advWaterText, advProteinText, advCarbohydrateText, advSugarText, advFiberText,
        advCholesterolText, advSaturatedFatText, advMonounsaturatedFatText, advPolyunsaturatedFatText,
        advTransFatText, advVitaminAText, advVitaminCText, advVitaminDText, advSodiumText,
        advPotassiumText, advCalciumText, advIronText;
    private View advancedMealForm;

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
        advancedMealButton = view.findViewById(R.id.advancedMealButton);
        advancedMealForm = view.findViewById(R.id.advancedMealForm);

        // new meal
        mealNameText = (EditText) view.findViewById(R.id.editTextFoodName);
        servingSizeText = (EditText) view.findViewById(R.id.editTextServingSize);
        servingWeightText = (EditText) view.findViewById(R.id.editTextServingWeight);
        caloriesPerServingText = (EditText) view.findViewById(R.id.editTextCalories);

        advCaloriesText = (EditText) view.findViewById(R.id.advancedMealEditCalories);
        advWaterText = (EditText) view.findViewById(R.id.advancedMealEditWater);
        advProteinText = (EditText) view.findViewById(R.id.advancedMealEditProtein);
        advCarbohydrateText = (EditText) view.findViewById(R.id.advancedMealEditCarbohydrate);
        advSugarText = (EditText) view.findViewById(R.id.advancedMealEditSugar);
        advFiberText = (EditText) view.findViewById(R.id.advancedMealEditFiber);
        advCholesterolText = (EditText) view.findViewById(R.id.advancedMealEditCholesterol);
        advSaturatedFatText = (EditText) view.findViewById(R.id.advancedMealEditSaturatedFat);
        advMonounsaturatedFatText = (EditText) view.findViewById(R.id.advancedMealEditMonounsaturatedFat);
        advPolyunsaturatedFatText = (EditText) view.findViewById(R.id.advancedMealEditPolyunsaturatedFat);
        advTransFatText = (EditText) view.findViewById(R.id.advancedMealEditTransFat);
        advVitaminAText = (EditText) view.findViewById(R.id.advancedMealEditVitaminA);
        advVitaminCText = (EditText) view.findViewById(R.id.advancedMealEditVitaminC);
        advVitaminDText = (EditText) view.findViewById(R.id.advancedMealEditVitaminD);
        advSodiumText = (EditText) view.findViewById(R.id.advancedMealEditSodium);
        advPotassiumText = (EditText) view.findViewById(R.id.advancedMealEditPotassium);
        advCalciumText = (EditText) view.findViewById(R.id.advancedMealEditCalcium);
        advIronText = (EditText) view.findViewById(R.id.advancedMealEditIron);

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

        advancedMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(advancedMealForm.getVisibility() == View.GONE)
                    advancedMealForm.setVisibility(View.VISIBLE);
                else
                    advancedMealForm.setVisibility(View.GONE);
            }
        });

        //assign listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Created entry", Toast.LENGTH_SHORT).show();
                try {
                    mealName = mealNameText.getText().toString();
                    servingSize = Integer.parseInt(servingSizeText.getText().toString());
                    servingWeight = Integer.parseInt(servingWeightText.getText().toString());
                    caloriesPerServing = Integer.parseInt(caloriesPerServingText.getText().toString());

                    if(advCaloriesText.getText().toString().isEmpty())
                        advCalories = 0;
                    else
                        advCalories = Integer.parseInt(advCaloriesText.getText().toString());
                    if(advWaterText.getText().toString().isEmpty())
                        advWater = 0;
                    else
                        advWater = Integer.parseInt(advWaterText.getText().toString());
                    if (advProteinText.getText().toString().isEmpty())
                        advProtein = 0;
                    else
                        advProtein = Integer.parseInt(advProteinText.getText().toString());
                    if (advCarbohydrateText.getText().toString().isEmpty())
                        advCarbohydrate = 0;
                    else
                        advCarbohydrate = Integer.parseInt(advCarbohydrateText.getText().toString());
                    if (advSugarText.getText().toString().isEmpty())
                        advSugar = 0;
                    else
                        advSugar = Integer.parseInt(advSugarText.getText().toString());
                    if (advFiberText.getText().toString().isEmpty())
                        advFiber = 0;
                    else
                        advFiber = Integer.parseInt(advFiberText.getText().toString());
                    if (advCholesterolText.getText().toString().isEmpty())
                        advCholesterol = 0;
                    else
                        advCholesterol = Integer.parseInt(advCholesterolText.getText().toString());
                    if (advSaturatedFatText.getText().toString().isEmpty())
                        advSaturatedFat = 0;
                    else
                        advSaturatedFat = Integer.parseInt(advSaturatedFatText.getText().toString());
                    if (advMonounsaturatedFatText.getText().toString().isEmpty())
                        advMonounsaturatedFat = 0;
                    else
                        advMonounsaturatedFat = Integer.parseInt(advMonounsaturatedFatText.getText().toString());
                    if (advPolyunsaturatedFatText.getText().toString().isEmpty())
                        advPolyunsaturatedFat = 0;
                    else
                        advPolyunsaturatedFat = Integer.parseInt(advPolyunsaturatedFatText.getText().toString());
                    if (advTransFatText.getText().toString().isEmpty())
                        advTransFat = 0;
                    else
                        advTransFat = Integer.parseInt(advTransFatText.getText().toString());
                    if (advVitaminAText.getText().toString().isEmpty())
                        advVitaminA = 0;
                    else
                        advVitaminA = Integer.parseInt(advVitaminAText.getText().toString());
                    if (advVitaminCText.getText().toString().isEmpty())
                        advVitaminC = 0;
                    else
                        advVitaminC = Integer.parseInt(advVitaminCText.getText().toString());
                    if (advVitaminDText.getText().toString().isEmpty())
                        advVitaminD = 0;
                    else
                        advVitaminD = Integer.parseInt(advVitaminDText.getText().toString());
                    if (advSodiumText.getText().toString().isEmpty())
                        advSodium = 0;
                    else
                        advSodium = Integer.parseInt(advSodiumText.getText().toString());
                    if (advPotassiumText.getText().toString().isEmpty())
                        advPotassium = 0;
                    else
                        advPotassium = Integer.parseInt(advPotassiumText.getText().toString());
                    if (advCalciumText.getText().toString().isEmpty())
                        advCalcium = 0;
                    else
                        advCalcium = Integer.parseInt(advCalciumText.getText().toString());
                    if (advIronText.getText().toString().isEmpty())
                        advIron = 0;
                    else
                        advIron = Integer.parseInt(advIronText.getText().toString());

                    final com.example.nutrimons.database.Meal meal = new com.example.nutrimons.database.Meal(
                            mealName, servingSize, servingWeight, caloriesPerServing,
                            advCalories, advWater, advProtein, advCarbohydrate, advSugar,
                            advFiber, advCholesterol, advSaturatedFat, advMonounsaturatedFat,
                            advPolyunsaturatedFat, advTransFat, advVitaminA, advVitaminC, advVitaminD,
                            advSodium, advPotassium, advCalcium, advIron);
                    /*meal.setAdvFields(advCalories, advWater, advProtein, advCarbohydrate, advSugar,
                            advFiber, advCholesterol, advSaturatedFat, advMonounsaturatedFat,
                            advPolyunsaturatedFat, advTransFat, advVitaminA, advVitaminC, advVitaminD,
                            advSodium, advPotassium, advCalcium, advIron);*/
                    mDb.mealDao().insert(meal);
                    Log.d("meal", mDb.mealDao().findByName(mealName).toTextViewString());

                    // navigates to meal plan
                    Navigation.findNavController(view).navigate(R.id.action_nav_addMeal_to_nav_mealPlan);
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(getContext(), "Error. Please fill out all of these fields", Toast.LENGTH_LONG).show();
                    mealNameText.setError("");
                    servingSizeText.setError("");
                    servingWeightText.setError("");
                    caloriesPerServingText.setError("");
                    e.printStackTrace();
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getContext(), "Error. Please fill out at all of these fields", Toast.LENGTH_LONG).show();
                    mealNameText.setError("");
                    servingSizeText.setError("");
                    servingWeightText.setError("");
                    caloriesPerServingText.setError("");
                    e.printStackTrace();
                }
                catch(SQLiteConstraintException e) //doesn't currently catch
                {
                    Toast.makeText(getContext(), "Error. This item already exists", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}