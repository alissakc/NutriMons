package com.example.nutrimons;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.User;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profile<T> extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinner;

    private ImageView profilePicture;
    private ImageButton editProfilePicture;
    private Button showNutrientRecs, saveProfile;

    private String profileFocus, name, email, password, birthday, financialSource, financialHistory, financialPlan, nutriCoins, age, sex, weight, height, ethnicity, healthHistory, healthGoals, activityLevel;
    private EditText nameText, emailText, passwordText, birthdayText, financialSourceText, financialHistoryText, financialPlanText, nutriCoinsText, ageText, sexText, weightText, heightText, ethnicityText, healthHistoryText, healthGoalsText, activityLevelText;
    private int nameId, emailId, passwordId, birthdayId, financialSourceId, financialHistoryId, financialPlanId, nutriCoinsId, ageId, sexId, weightId, heightId, ethnicityId, healthHistoryId, healthGoalsId, activityLevelId;

    private View view;

    private AppDatabase mDb;
    private NutrientTablesApi nta;

    private int userID;
    Context context;

    ArrayAdapter<String> dataAdapter;

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
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) // ask single or multiple permission once
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
        profileFocuses.add("Maintain Weight");
        profileFocuses.add("Gain Muscle");

        // Creating adapter for spinners
        dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, profileFocuses);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinners
        spinner.setAdapter(dataAdapter);

        profilePicture = view.findViewById(R.id.profilePicture);
        editProfilePicture = view.findViewById(R.id.editProfilePicture);
        showNutrientRecs = view.findViewById(R.id.showNutrientRecs);
        saveProfile = view.findViewById(R.id.saveProfile);

        editProfilePicture.setOnClickListener(this);
        showNutrientRecs.setOnClickListener(this);
        saveProfile.setOnClickListener(this);

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

        nameId = (nameText.getId());
        emailId = (emailText.getId());
        passwordId = (passwordText.getId());
        birthdayId = (birthdayText.getId());
        financialSourceId = (financialSourceText.getId());
        financialHistoryId = (financialHistoryText.getId());
        financialPlanId = (financialPlanText.getId());
        nutriCoinsId = (nutriCoinsText.getId());
        ageId = (ageText.getId());
        sexId = (sexText.getId());
        weightId = (weightText.getId());
        heightId = (heightText.getId());
        ethnicityId = (ethnicityText.getId());
        healthHistoryId = (healthHistoryText.getId());
        healthGoalsId = (healthGoalsText.getId());
        activityLevelId = (activityLevelText.getId());

        mDb = AppDatabase.getInstance(getContext());
        nta = new NutrientTablesApi(mDb);
        userID = mDb.tokenDao().getUserID();
        getFields();

        context = getContext();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userID = mDb.tokenDao().getUserID();
        getFields();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case (R.id.editProfilePicture): //let user upload photo and swap user profile pic with this
                Toast.makeText(getContext(), "Pencil Clicked", Toast.LENGTH_LONG).show();
                //Navigation.findNavController(view).navigate(R.id.action_nav_meal_to_nav_scanBarcode);
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
            case (R.id.showNutrientRecs):
                /*Log.d("age & sex", age + " " + sex);
                nta = new NutrientTablesApi(AppDatabase.getInstance(getContext()));
                nuts = nta.getTablesByGroup(age, sex, "N/A");
                nuts.forEach((key, value)->
                {
                    Log.d("key", key);
                    value.forEach((key2, value2)->Log.d("key & string", key2 + " " + value2 + " "));
                    Log.d("========","===========");
                });*/
                showNutrients();
                //Toast.makeText(getContext(), "nutrient button clicked", Toast.LENGTH_LONG).show();
                break;
            case (R.id.saveProfile):
                saveChanges();
                //Toast.makeText(getContext(), "save profile button clicked", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        profileFocus = parent.getItemAtPosition(position).toString();

        //***save selected data to database here***

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + profileFocus, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) //https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), selectedImage); //get and set image
                Bitmap scaledbmp = scaleBitmap(bitmap, Math.max(profilePicture.getWidth(), profilePicture.getHeight()));
                profilePicture.setImageBitmap(scaledbmp);

                String bmp = BitMapToString(scaledbmp); //add to db
                User u = mDb.userDao().findByUserID(userID);
                u.profilePicture = bmp;
                mDb.userDao().insert(u);
                Log.d("bmp", bmp);
            }
            catch (FileNotFoundException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    private Bitmap scaleBitmap(Bitmap image, int size) //http://www.codeplayon.com/2018/11/android-image-upload-to-server-from-camera-and-gallery/
    {
        int width = image.getWidth(), height = image.getHeight();
        float ratio = (float) width / (float) height;

        if(ratio > 1)
        {
            width = size;
            height = (int) (width / ratio);
        }
        else
        {
            height = size;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String BitMapToString(Bitmap bitmap){ //https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){ //https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void saveChanges()
    {
        User u = mDb.userDao().findByUserID(userID);

        u.profileFocus = profileFocus;
        u.name = name;
        u.email = email;
        u.password = password;
        u.birthday = birthday;

        u.age = age;
        u.sex = sex;
        u.weight = weight;
        u.height = height;
        u.ethnicity = ethnicity;
        u.activityLevel = activityLevel;
        u.healthHistory = healthHistory;

        u.financialSource = financialSource;
        u.financialHistory = financialHistory;
        u.financialPlan = financialPlan;
        u.nutriCoins = nutriCoins;

        u.healthGoals = healthGoals;

        //mDb.userDao().updateUser(u);
        Log.d("debug", "saveChanges() called");
        //Log.d("debug", String.valueOf(u.userID));
        mDb.userDao().delete(u); //allows user to change email
        mDb.userDao().insert(u);

        Log.d("User ", u.toString());
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
            if(((EditText) target).getId() == nameId)
                name = str;
            else if(((EditText) target).getId() == emailId)
                email = str;
            else if(((EditText) target).getId() == passwordId)
                password = str;
            else if(((EditText) target).getId() == birthdayId)
                birthday = str;
            else if(((EditText) target).getId() == financialSourceId)
                financialSource = str;
            else if(((EditText) target).getId() == financialHistoryId)
                financialHistory = str;
            else if(((EditText) target).getId() == financialPlanId)
                financialPlan = str;
            else if(((EditText) target).getId() == nutriCoinsId)
                nutriCoins = str;
            else if(((EditText) target).getId() == ageId)
                age = str;
            else if(((EditText) target).getId() == sexId)
                sex = str;
            else if(((EditText) target).getId() == weightId)
                weight = str;
            else if(((EditText) target).getId() == heightId)
                height = str;
            else if(((EditText) target).getId() == ethnicityId)
                ethnicity = str;
            else if(((EditText) target).getId() == healthHistoryId)
                healthHistory = str;
            else if(((EditText) target).getId() == healthGoalsId)
                healthGoals = str;
            else if(((EditText) target).getId() == activityLevelId)
                activityLevel = str;
            //Log.d("debug", String.valueOf(((EditText) target).getId()));
            //Toast.makeText(getContext(), "value is now: " + str, Toast.LENGTH_SHORT).show();
        }
    }

    private void showNutrients() //from https://www.nal.usda.gov/sites/default/files/fnic_uploads/recommended_intakes_individuals.pdf
    {
        nta.updateUserNutrients(userID);
        User u = mDb.userDao().findByUserID(userID);

        View nt = view.findViewById(R.id.nutrientsTable);
        TextView driCalories = view.findViewById(R.id.driCalories);
        TextView ulCalories = view.findViewById(R.id.ulCalories);
        TextView driWater = view.findViewById(R.id.driWater);
        TextView ulWater = view.findViewById(R.id.ulWater);
        TextView driProtein = view.findViewById(R.id.driProtein);
        TextView ulProtein = view.findViewById(R.id.ulProtein);
        TextView driCarbs = view.findViewById(R.id.driCarbs);
        TextView ulCarbs = view.findViewById(R.id.ulCarbs);
        TextView driSugar = view.findViewById(R.id.driSugar);
        TextView ulSugar = view.findViewById(R.id.ulSugar);
        TextView driFiber = view.findViewById(R.id.driFiber);
        TextView ulFiber = view.findViewById(R.id.ulFiber);
        TextView driFats = view.findViewById(R.id.driFats);
        TextView ulFats = view.findViewById(R.id.ulFats);
        TextView driCholesterol = view.findViewById(R.id.driCholesterol);
        TextView ulCholesterol = view.findViewById(R.id.ulCholesterol);
        TextView driSaturatedFats = view.findViewById(R.id.driSaturatedFats);
        TextView ulSaturatedFats = view.findViewById(R.id.ulSaturatedFats);
        TextView driUnsaturatedFats = view.findViewById(R.id.driUnsaturatedFats);
        TextView ulUnsaturatedFats = view.findViewById(R.id.ulUnsaturatedFats);
        TextView driTransFats = view.findViewById(R.id.driTransFats);
        TextView ulTransFats = view.findViewById(R.id.ulTransFats);

        TextView driVitaminA = view.findViewById(R.id.driVitaminA);
        TextView ulVitaminA = view.findViewById(R.id.ulVitaminA);
        TextView driVitaminC = view.findViewById(R.id.driVitaminC);
        TextView ulVitaminC = view.findViewById(R.id.ulVitaminC);
        TextView driVitaminD = view.findViewById(R.id.driVitaminD);
        TextView ulVitaminD = view.findViewById(R.id.ulVitaminD);

        TextView driSodium = view.findViewById(R.id.driSodium);
        TextView ulSodium = view.findViewById(R.id.ulSodium);
        TextView driPotassium = view.findViewById(R.id.driPotassium);
        TextView ulPotassium = view.findViewById(R.id.ulPotassium);
        TextView driCalcium = view.findViewById(R.id.driCalcium);
        TextView ulCalcium = view.findViewById(R.id.ulCalcium);
        TextView driIron = view.findViewById(R.id.driIron);
        TextView ulIron = view.findViewById(R.id.ulIron);

        driCalories.setText(u.calories + " cal");
        ulCalories.setText(u.calories + " cal");
        driWater.setText(u.water + "L");
        //ulWater.setText(u.water + "L");
        driProtein.setText(u.proteinDRI + "g"); //grams returned, convert to calories
        ulProtein.setText(u.proteinUL + "g"); //percentage returned
        driCarbs.setText(u.carbsDRI + "g");
        ulCarbs.setText(u.carbsUL + "g");
        driSugar.setText(u.sugarDRI + "g");
        ulSugar.setText(u.sugarUL + "g");
        driFiber.setText(u.fiberDRI + "g");
        //ulFiber.setText(u.fiberDRI + "g");
        driFats.setText(u.fatsDRI + "g");
        ulFats.setText(u.fatsUL + "g");
        driCholesterol.setText(u.cholesterolDRI + "g");
        ulCholesterol.setText(u.cholesterolUL + "g");
        driSaturatedFats.setText(u.saturatedFatsDRI + "g");
        ulSaturatedFats.setText(u.saturatedFatsUL + "g");
        driUnsaturatedFats.setText(u.unsaturatedFatsDRI + "g");
        ulUnsaturatedFats.setText(u.unsaturatedFatsUL + "g");
        driTransFats.setText(u.transFatsDRI + "g");
        ulTransFats.setText(u.transFatsUL + "g");

        driVitaminA.setText(u.vitaminADRI + "µg");
        ulVitaminA.setText(u.vitaminAUL + "µg");
        driVitaminC.setText(u.vitaminCDRI + "mg");
        ulVitaminC.setText(u.vitaminCUL + "mg");
        driVitaminD.setText(u.vitaminDDRI + "µg");
        ulVitaminD.setText(u.vitaminDUL + "µg");

        driSodium.setText(u.sodiumDRI + "g");
        ulSodium.setText(u.sodiumUL + "g");
        driPotassium.setText(u.potassiumDRI + "g");
        //ulPotassium.setText(u.potassiumDRI + "g"); //no limit
        driCalcium.setText(u.calciumDRI + "mg");
        ulCalcium.setText(u.calciumUL + "mg");
        driIron.setText(u.ironDRI + "mg");
        ulIron.setText(u.ironUL + "mg");

        nt.setVisibility(view.VISIBLE);
    }

    private void getFields()
    {
        User u = mDb.userDao().findByUserID(userID);
        String pfp = u.profilePicture;
        try {
            if(!pfp.equals(""))
                profilePicture.setImageBitmap(StringToBitMap(pfp));
        }
        catch(NullPointerException e) { }

        switch(u.profileFocus)
        {
            case "Lose Weight":
                profileFocus = "Lose Weight";
                spinner.setSelection(1); //needs refactoring to find the text from the arraylist
                break;
            case "Maintain Weight":
                profileFocus = "Maintain Weight";
                spinner.setSelection(2);
                break;
            case "Gain Muscle":
                profileFocus = "Gain Muscle";
                spinner.setSelection(3);
                break;
            default:

        }

        name = u.name;
        email = u.email;
        password = u.password;
        birthday = u.birthday;

        age = u.age;
        sex = u.sex;
        weight = u.weight;
        height = u.height;
        ethnicity = u.ethnicity;
        activityLevel = u.activityLevel;
        healthHistory = u.healthHistory;

        financialSource = u.financialSource;
        financialHistory = u.financialHistory;
        financialPlan = u.financialPlan;
        nutriCoins = u.nutriCoins;

        healthGoals = u.healthGoals;

        nameText.setText(name);
        emailText.setText(email);
        passwordText.setText(password);
        birthdayText.setText(birthday);

        ageText.setText(age);
        sexText.setText(sex);
        weightText.setText(weight);
        heightText.setText(height);
        ethnicityText.setText(ethnicity);
        activityLevelText.setText(activityLevel);
        healthHistoryText.setText(healthHistory);

        financialSourceText.setText(financialSource);
        financialHistoryText.setText(financialHistory);
        financialPlanText.setText(financialPlan);
        nutriCoinsText.setText(nutriCoins);

        healthGoalsText.setText(healthGoals);
    }
}