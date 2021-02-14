package com.example.nutrimons;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Water#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Water extends Fragment {
    // Variables for pie chart
    PieChart waterPieChart;
    private double amountDrank = 100.0;
    private double amountNeeded = 20.0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Water() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Water.
     */
    // TODO: Rename and change types and number of parameters
    public static Water newInstance(String param1, String param2) {
        Water fragment = new Water();
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
        View view = inflater.inflate(R.layout.fragment_water, container, false);
        waterPieChart = view.findViewById(R.id.waterPieChart_view);
        initPieChart();
        showPieChart();
        return view;
    }
    private void showPieChart(){

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "(in ounces)";

        //initializing data
        Map<String, Double> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Consumed",amountDrank);
        typeAmountMap.put("Needed",amountNeeded);


        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#00c8ff"));
        colors.add(Color.parseColor("#ff002b"));
        /*
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));
         */

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }


        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(30f);
        //setting text size of the entry label (in graph)
        pieDataSet.setValueTextColor(Color.parseColor("#000000"));
        waterPieChart.setEntryLabelColor(Color.parseColor("#000000"));
        waterPieChart.setEntryLabelTextSize(15f);

        waterPieChart.setDrawEntryLabels(false);

        /*
        // value label is set to black and outside the graph. Also,
        pieChart.setExtraLeftOffset(20f);
        pieChart.setExtraRightOffset(20f);
        //pieDataSet.setValueTextColor(Color.BLACK);
        //pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
         */
        //setting legend
        waterPieChart.getLegend().setWordWrapEnabled(true);
        waterPieChart.getLegend().setTextSize(25f);

        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);
        // change the value to percentage
        //pieData.setValueFormatter(new PercentFormatter());
        waterPieChart.setData(pieData);
        waterPieChart.invalidate();
    }
    private void initPieChart(){
        //using percentage as values instead of amount
        waterPieChart.setUsePercentValues(false);

        //remove the description label on the lower left corner, default true if not set
        waterPieChart.getDescription().setEnabled(false);

        // remove legend
        //pieChart.getLegend().setEnabled(false);

        //enabling the user to rotate the chart, default true
        waterPieChart.setRotationEnabled(true);
        //adding friction when rotating the pie chart
        waterPieChart.setDragDecelerationFrictionCoef(0.9f);
        //setting the first entry start from right hand side, default starting from top
        waterPieChart.setRotationAngle(0);

        //highlight the entry when it is tapped, default true if not set
        waterPieChart.setHighlightPerTapEnabled(true);
        //adding animation so the entries pop up from 0 degree
        waterPieChart.animateY(1400, Easing.EaseInOutQuad);
        //setting the color of the hole in the middle, default white
        waterPieChart.setHoleColor(Color.parseColor("#ffffff"));
        waterPieChart.setHoleRadius(40f);
        waterPieChart.setTransparentCircleRadius(0f);
    }
}