package com.example.nutrimons;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinner;

    private Button editProfilePicture, showNutrientRecs;

    private String profileFocus, name, email, password, birthday, financialSource, financialHistory, financialPlan, nutriCoins, age, sex, weight, height, ethnicity, healthHistory, healthGoals, activityLevel;
    private EditText nameText, emailText, passwordText, birthdayText, financialSourceText, financialHistoryText, financialPlanText, nutriCoinsText, ageText, sexText, weightText, heightText, ethnicityText, healthHistoryText, healthGoalsText, activityLevelText;

    private View view;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //set up button
        /*editProfilePicture = view.findViewById(R.id.imageView5);
        editProfilePicture.setOnClickListener(this);*/

        //ask for storage permissions
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE/*,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE*/) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                        // Set up the listener for take photo button
                        //Toast.makeText(getContext(), "Permissions granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Permissions needed for this function", Toast.LENGTH_LONG).show();
                    }
                });

        //spinner stuff, see MealPlan
        spinner = (Spinner) view.findViewById(R.id.profileFocusSpinner);
        spinner.setOnItemSelectedListener(this);

        List<String> profileFocuses = new ArrayList<String>();
        profileFocuses.add("Select Focus");
        profileFocuses.add("Lose Weight");
        profileFocuses.add("Gain Muscle");
        profileFocuses.add("Maintain Weight");
        profileFocuses.add("No Longer Be Malnourished");

        // Creating adapter for spinners
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, profileFocuses);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinners
        spinner.setAdapter(dataAdapter);

        //editProfilePicture = view.findViewById(R.id.imageView5); cannot cast the ImageView as a Button; use an ImageButton object instead
        showNutrientRecs = view.findViewById(R.id.showNutrientRecs);

        //editProfilePicture.setOnClickListener(this);
        showNutrientRecs.setOnClickListener(this);

        nameText = view.findViewById(R.id.profileName);
        emailText = view.findViewById(R.id.email);
        passwordText = view.findViewById(R.id.password);
        birthdayText = view.findViewById(R.id.birthday);
        financialSourceText = view.findViewById(R.id.financialSource);
        financialHistoryText = view.findViewById(R.id.financialHistory);
        financialPlanText = view.findViewById(R.id.financialPlan);
        nutriCoinsText = view.findViewById(R.id.nutriCoins);
        ageText = view.findViewById(R.id.age);
        sexText = view.findViewById(R.id.sex);
        weightText = view.findViewById(R.id.weight);
        heightText = view.findViewById(R.id.height);
        ethnicityText = view.findViewById(R.id.ethnicity);
        healthHistoryText = view.findViewById(R.id.healthHistory);
        healthGoalsText = view.findViewById(R.id.healthGoals);
        activityLevelText = view.findViewById(R.id.activityLevel);

        nameText.addTextChangedListener(new TextChangedListener<EditText>(nameText, name));
        emailText.addTextChangedListener(new TextChangedListener<EditText>(emailText, email));
        passwordText.addTextChangedListener(new TextChangedListener<EditText>(passwordText, password));
        birthdayText.addTextChangedListener(new TextChangedListener<EditText>(birthdayText, birthday));
        financialSourceText.addTextChangedListener(new TextChangedListener<EditText>(financialSourceText, financialSource));
        financialHistoryText.addTextChangedListener(new TextChangedListener<EditText>(financialHistoryText, financialHistory));
        financialPlanText.addTextChangedListener(new TextChangedListener<EditText>(financialPlanText, financialPlan));
        nutriCoinsText.addTextChangedListener(new TextChangedListener<EditText>(nutriCoinsText, nutriCoins));
        ageText.addTextChangedListener(new TextChangedListener<EditText>(ageText, age));
        sexText.addTextChangedListener(new TextChangedListener<EditText>(sexText, sex));
        weightText.addTextChangedListener(new TextChangedListener<EditText>(weightText, weight));
        heightText.addTextChangedListener(new TextChangedListener<EditText>(heightText, height));
        ethnicityText.addTextChangedListener(new TextChangedListener<EditText>(ethnicityText, ethnicity));
        healthHistoryText.addTextChangedListener(new TextChangedListener<EditText>(healthHistoryText, healthHistory));
        healthGoalsText.addTextChangedListener(new TextChangedListener<EditText>(healthGoalsText, healthGoals));
        activityLevelText.addTextChangedListener(new TextChangedListener<EditText>(activityLevelText, activityLevel));

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case (R.id.imageView5): //let user upload photo and swap user profile pic with this
                Toast.makeText(getContext(), "Pencil Clicked", Toast.LENGTH_LONG).show();
                //Navigation.findNavController(view).navigate(R.id.action_nav_meal_to_nav_scanBarcode);
                break;
            case (R.id.showNutrientRecs):
                calculateNutrients();
                Toast.makeText(getContext(), "nutrient button clicked", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        profileFocus = parent.getItemAtPosition(position).toString();

        //***save selected data to database here***

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + profileFocus, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public class TextChangedListener<T> implements TextWatcher { //https://stackoverflow.com/questions/11134144/android-edittext-onchange-listener
        private T target;
        private EditText et;
        private String str;

        public TextChangedListener(T target, String str) {
            this.target = target;
            this.str = str;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}


        @Override
        public void afterTextChanged(Editable s) {
            this.onTextChanged(target, s);
        }

        public /*abstract*/ void onTextChanged(T target, Editable s)
        {
            //target.setText(s); //infinite loop since text now changed
            //also set the view fields database values then replace str with a db insert
            et = (EditText) target;
            str = et.getText().toString();
            Toast.makeText(getContext(), "value is now: " + str, Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateNutrients()
    {
        View nt = view.findViewById(R.id.nutrientsTable);
        TextView rdiCal = view.findViewById(R.id.rdiCalories);
        TextView ulCal = view.findViewById(R.id.ulCalories);
        TextView rdiPro = view.findViewById(R.id.rdiProtein);
        TextView ulPro = view.findViewById(R.id.ulProtein);
        TextView rdiCarbs = view.findViewById(R.id.rdiCarbs);
        TextView ulCarbs = view.findViewById(R.id.ulCarbs);
        TextView rdiFats = view.findViewById(R.id.rdiFats);
        TextView ulFats = view.findViewById(R.id.ulFats);
        int calories, proteins, proteinMax, carbohydrates, carbMax, fats, fatMax;

        getFields();
        calories = calculateCalories();
        proteins = (int)(calories * .2125); //10-35% calorie recommendation
        proteinMax = (int)(calories * .35);
        carbohydrates = (int)(calories * .525); //45-65% calorie recommendation
        carbMax = (int)(calories * .65);
        fats = (int)(calories * .2625); //20-35% calorie recommendation
        fatMax = (int)(calories * .35);

        rdiCal.setText(String.valueOf(calories));
        ulCal.setText(String.valueOf(calories));
        rdiPro.setText(String.valueOf(proteins));
        ulPro.setText(String.valueOf(proteinMax));
        rdiCarbs.setText(String.valueOf(carbohydrates));
        ulCarbs.setText(String.valueOf(carbMax));
        rdiFats.setText(String.valueOf(fats));
        ulFats.setText(String.valueOf(fatMax));

        nt.setVisibility(view.VISIBLE);
    }

    private void getFields()
    {
        name = nameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        birthday = birthdayText.getText().toString();
        financialSource = financialSourceText.getText().toString();
        financialHistory = financialHistoryText.getText().toString();
        financialPlan = financialPlanText.getText().toString();
        nutriCoins = nutriCoinsText.getText().toString();
        age = ageText.getText().toString();
        sex = sexText.getText().toString();
        weight = weightText.getText().toString();
        height = heightText.getText().toString();
        ethnicity = ethnicityText.getText().toString();
        healthHistory = healthHistoryText.getText().toString();
        healthGoals = healthGoalsText.getText().toString();
        activityLevel = activityLevelText.getText().toString();
    }

    private int calculateCalories()
    {
        double al;
        //modify with activity modifier https://www.k-state.edu/paccats/Contents/PA/PDF/Physical%20Activity%20and%20Controlling%20Weight.pdf
        if(activityLevel.equals("Sedentary"))
            al = 1.2;
        else if(activityLevel.equals("Lightly active"))
            al = 1.375;
        else if(activityLevel.equals("Moderately active"))
            al = 1.55;
        else if(activityLevel.equals("Very active"))
            al = 1.725;
        else
            al = 1.9;

        if(sex.equals("Male")) //Mifflin-St Jeor Equation https://en.wikipedia.org/wiki/Basal_metabolic_rate
            return (int)((10 * Integer.parseInt(weight) / 2.2 /*pounds*/ + 6.25 * 2.54 * Integer.parseInt(height) /*inches*/ - 5 * Integer.parseInt(age) + 5) * al);
        else
            return (int)((10 * Integer.parseInt(weight) / 2.2 /*pounds*/ + 6.25 * 2.54 * Integer.parseInt(height) /*inches*/ - 5 * Integer.parseInt(age) - 161) * al);
    }
}