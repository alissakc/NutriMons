package com.example.nutrimons;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.List;

class Profile extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE/*,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE*/) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                        // Set up the listener for take photo button
                        Toast.makeText(getContext(), "Working", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Permissions needed for this function", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigate(R.id.action_nav_scanBarcode_to_nav_home);
                    }
                });

        //spinner stuff, see MealPlan
        spinner = (Spinner) view.findViewById(R.id.profileFocusSpinner);
        spinner.setOnItemSelectedListener(this);

        List<String> profileFocuses = new ArrayList<String>();
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

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onClick(View view) {
        //let user upload photo and swap user profile pic with this
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        //***save selected data to database here***

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}