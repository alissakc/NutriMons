package com.BAMM.nutrimons;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.BAMM.nutrimons.database.AppDatabase;

import java.math.BigDecimal;
import java.util.Random;

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
    private Button addButton, searchFDCButton, advancedMealButton, randomButton;
    private String mealName, servingSize;
    private float servingsEaten, advCalories, advProtein,
        advCarbohydrate, advSugar, advFiber, advCholesterol, advSaturatedFat, advMonounsaturatedFat,
        advPolyunsaturatedFat, advTransFat, advVitaminA, advVitaminC, advVitaminD, advSodium,
        advPotassium, advCalcium, advIron;
    private EditText mealNameText, servingSizeText, servingsEatenText,
        advCaloriesText, advProteinText, advCarbohydrateText, advSugarText, advFiberText,
        advCholesterolText, advSaturatedFatText, advMonounsaturatedFatText, advPolyunsaturatedFatText,
        advTransFatText, advVitaminAText, advVitaminCText, advVitaminDText, advSodiumText,
        advPotassiumText, advCalciumText, advIronText;
    private View advancedMealForm;
    public static com.BAMM.nutrimons.database.Meal food;
    private OnFragmentInteractionListener mListener;

    // creates instance of database
    private AppDatabase mDb;

    private View view;

    private View checkboxbrandedButton;
    private boolean isBranded;

    private String[] foodNames;

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
        if (mListener != null) {
            mListener.onFragmentInteraction("Add Meal");
        }

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_meal, container, false);

        // database
        mDb = AppDatabase.getInstance(getContext());

        // Button initialization
        addButton = view.findViewById(R.id.submitNewMealButton);
        searchFDCButton = view.findViewById(R.id.searchFDCButton);
        advancedMealButton = view.findViewById(R.id.advancedMealButton);
        advancedMealForm = view.findViewById(R.id.advancedMealForm);
        randomButton = view.findViewById(R.id.randomMeal);

        // new meal
        mealNameText = (EditText) view.findViewById(R.id.editTextFoodName);
        servingSizeText = (EditText) view.findViewById(R.id.editTextServingSize);
        servingsEatenText = (EditText) view.findViewById(R.id.editTextServingsEaten);
        advCaloriesText = (EditText) view.findViewById(R.id.advancedMealEditCalories);

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

        foodNames = this.getResources().getStringArray(R.array.foodNames);

        initializeFields();

        checkboxbrandedButton = (CompoundButton) view.findViewById(R.id.checkboxBranded);
        checkboxbrandedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
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
                hideKeyboard();
                mealName = mealNameText.getText().toString();
                AddFromFDC fragment = new AddFromFDC();

                fragment.mealName = mealName;
                fragment.isBranded = isBranded;

                Navigation.findNavController(view).navigate(R.id.nav_addFromFDC);
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                getRandomFood();
            }
        });

        advancedMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
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
                hideKeyboard();
                try {
                    mealName = mealNameText.getText().toString();
                    servingSize = servingSizeText.getText().toString();
                    servingsEaten = TextViewToFloat3(servingsEatenText);
                    advCalories = TextViewToFloat3(advCaloriesText);

                    if (advProteinText.getText().toString().isEmpty())
                        advProtein = 0;
                    else
                        advProtein = TextViewToFloat3(advProteinText);
                    if (advCarbohydrateText.getText().toString().isEmpty())
                        advCarbohydrate = 0;
                    else
                        advCarbohydrate = TextViewToFloat3(advCarbohydrateText);
                    if (advSugarText.getText().toString().isEmpty())
                        advSugar = 0;
                    else
                        advSugar = TextViewToFloat3(advSugarText);
                    if (advFiberText.getText().toString().isEmpty())
                        advFiber = 0;
                    else
                        advFiber = TextViewToFloat3(advFiberText);
                    if (advCholesterolText.getText().toString().isEmpty())
                        advCholesterol = 0;
                    else
                        advCholesterol = TextViewToFloat3(advCholesterolText);
                    if (advSaturatedFatText.getText().toString().isEmpty())
                        advSaturatedFat = 0;
                    else
                        advSaturatedFat = TextViewToFloat3(advSaturatedFatText);
                    if (advMonounsaturatedFatText.getText().toString().isEmpty())
                        advMonounsaturatedFat = 0;
                    else
                        advMonounsaturatedFat = TextViewToFloat3(advMonounsaturatedFatText);
                    if (advPolyunsaturatedFatText.getText().toString().isEmpty())
                        advPolyunsaturatedFat = 0;
                    else
                        advPolyunsaturatedFat = TextViewToFloat3(advPolyunsaturatedFatText);
                    if (advTransFatText.getText().toString().isEmpty())
                        advTransFat = 0;
                    else
                        advTransFat = TextViewToFloat3(advTransFatText);
                    if (advVitaminAText.getText().toString().isEmpty())
                        advVitaminA = 0;
                    else
                        advVitaminA = TextViewToFloat3(advVitaminAText);
                    if (advVitaminCText.getText().toString().isEmpty())
                        advVitaminC = 0;
                    else
                        advVitaminC = TextViewToFloat3(advVitaminCText);
                    if (advVitaminDText.getText().toString().isEmpty())
                        advVitaminD = 0;
                    else
                        advVitaminD = TextViewToFloat3(advVitaminDText);
                    if (advSodiumText.getText().toString().isEmpty())
                        advSodium = 0;
                    else
                        advSodium = TextViewToFloat3(advSodiumText) / 1000; //to grams
                    if (advPotassiumText.getText().toString().isEmpty())
                        advPotassium = 0;
                    else
                        advPotassium = TextViewToFloat3(advPotassiumText) / 1000; //to grams
                    if (advCalciumText.getText().toString().isEmpty())
                        advCalcium = 0;
                    else
                        advCalcium = TextViewToFloat3(advCalciumText);
                    if (advIronText.getText().toString().isEmpty())
                        advIron = 0;
                    else
                        advIron = TextViewToFloat3(advIronText);

                    final com.BAMM.nutrimons.database.Meal meal = new com.BAMM.nutrimons.database.Meal(
                            "(" + servingsEaten + ") " + mealName, servingSize, servingsEaten,
                            advCalories, 0, advProtein, advCarbohydrate, advSugar,
                            advFiber, advCholesterol, advSaturatedFat, advMonounsaturatedFat,
                            advPolyunsaturatedFat, advTransFat, advVitaminA, advVitaminC, advVitaminD,
                            advSodium, advPotassium, advCalcium, advIron);
                    mDb.mealDao().insert(meal);

                    Log.d("unsat", String.valueOf(advMonounsaturatedFat + advPolyunsaturatedFat));

                    // navigates to meal plan
                    Navigation.findNavController(view).navigate(R.id.action_nav_addMeal_to_nav_mealPlan);
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(getContext(), "Error. Please fill out all of these fields", Toast.LENGTH_LONG).show();
                    mealNameText.setError("");
                    servingSizeText.setError("");
                    servingsEatenText.setError("");
                    advCaloriesText.setError("");
                    e.printStackTrace();
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getContext(), "Error. Please fill out at all of these fields", Toast.LENGTH_LONG).show();
                    mealNameText.setError("");
                    servingSizeText.setError("");
                    servingsEatenText.setError("");
                    advCaloriesText.setError("");
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

    private void getRandomFood()
    {
        Random r = new Random();
        int random = r.nextInt(foodNames.length);
        mealName = foodNames[random];
        MealRecommender mr = new MealRecommender(getContext(), mealName, true);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {}
        Navigation.findNavController(view).navigate(R.id.nav_addMeal);
    }

    private void initializeFields()
    {
        if(food != null) {
            mealName = food.mealName;
            mealNameText.setText(food.mealName);
            servingSize = food.servingSize;
            servingSizeText.setText(food.servingSize);
            servingsEaten = food.servingsEaten;
            servingsEatenText.setText(String.valueOf(food.servingsEaten));
            advCalories = food.calories;
            advCaloriesText.setText(String.valueOf(food.calories));
            advProtein = food.protein;
            advProteinText.setText(String.valueOf(food.protein));
            advCarbohydrate = food.carbohydrate;
            advCarbohydrateText.setText(String.valueOf(food.carbohydrate));
            advSugar = food.sugar;
            advSugarText.setText(String.valueOf(food.sugar));
            advFiber = food.fiber;
            advFiberText.setText(String.valueOf(food.fiber));
            advCholesterol = food.cholesterol;
            advCholesterolText.setText(String.valueOf(food.cholesterol));
            advSaturatedFat = food.saturatedFat;
            advSaturatedFatText.setText(String.valueOf(food.saturatedFat));
            advMonounsaturatedFat = food.monounsaturatedFat;
            advMonounsaturatedFatText.setText(String.valueOf(food.monounsaturatedFat));
            advPolyunsaturatedFat = food.polyunsaturatedFat;
            advPolyunsaturatedFatText.setText(String.valueOf(food.polyunsaturatedFat));
            advTransFat = food.transFat;
            advTransFatText.setText(String.valueOf(food.transFat));
            advVitaminA = food.vitaminA;
            advVitaminAText.setText(String.valueOf(food.vitaminA));
            advVitaminC = food.vitaminC;
            advVitaminCText.setText(String.valueOf(food.vitaminC));
            advVitaminD = food.vitaminD;
            advVitaminDText.setText(String.valueOf(food.vitaminD));
            advSodium = food.sodium;
            advSodiumText.setText(String.valueOf(food.sodium));
            advPotassium = food.potassium;
            advPotassiumText.setText(String.valueOf(food.potassium));
            advCalcium = food.calcium;
            advCalciumText.setText(String.valueOf(food.calcium));
            advIron = food.iron;
            advIronText.setText(String.valueOf(food.iron));
        }
    }

    private float TextViewToFloat3(TextView tv)
    {
        BigDecimal bd = new BigDecimal(tv.getText().toString());
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private void hideKeyboard()
    {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (AddMeal.OnFragmentInteractionListener) context;
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