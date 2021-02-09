package com.example.nutrimons;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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

        return view;
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