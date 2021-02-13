package com.example.nutrimons;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button goToMeal, goToWater, goToExercise;
    ImageView gotToProfile;

    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Button and image initialization
        gotToProfile = view.findViewById(R.id.imageProfile);
        goToMeal = view.findViewById(R.id.dashboardAddMeal);
        goToWater = view.findViewById(R.id.dashboardAddWater);
        goToExercise = view.findViewById(R.id.dashboardAddExercise);


        // assign listener for buttons
        gotToProfile.setOnClickListener(this);
        goToMeal.setOnClickListener(this);
        goToWater.setOnClickListener(this);
        goToExercise.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.imageProfile):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_profile);
                break;
            case (R.id.dashboardAddMeal):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_meal);
                break;
            case (R.id.dashboardAddWater):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_water);
                break;
            case (R.id.dashboardAddExercise):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_exercise);
                break;
        }
    }
}