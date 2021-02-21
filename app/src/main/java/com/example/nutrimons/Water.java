package com.example.nutrimons;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
 */
public class Water extends Fragment implements View.OnClickListener{
    // Variables for pie chart
    PieChart waterPieChart;
    private double amountDrank = 100.0;
    private double amountNeeded = 20.0;
    private double inputAmount;

    EditText waterAmountInput;
    Button submitWater;
    Button unitChange;

    // Required empty public constructor
    public Water() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water, container, false);
        waterPieChart = view.findViewById(R.id.waterPieChart_view);

        waterAmountInput = view.findViewById(R.id.waterAmountInputBox);
        submitWater = view.findViewById(R.id.waterSubmitButton);
        submitWater.setOnClickListener(this);
        unitChange = view.findViewById(R.id.unitChangeButton);
        unitChange.setOnClickListener(this);
        initPieChart();
        showPieChart(); // this method has to go after unitChange because it uses unitChange

        return view;
    }

    /**
     * Warning toast to the user when they enter in an invalid option for the input text.
     */
    private void toastWarning(){
        Toast.makeText(getContext(), "Not a valid input", Toast.LENGTH_SHORT).show();
    }
    private void toastConvertedtoOz(){
        Toast.makeText(getContext(), "Converted to oz", Toast.LENGTH_SHORT).show();
    }
    private void toastConvertedtoML(){
        Toast.makeText(getContext(), "Converted to ml", Toast.LENGTH_SHORT).show();
    }
    /**
     * Shows and modifies the pie chart.
     */
    private void showPieChart(){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label;
        if(inOz(unitChange)){
            label = "(in ounces)";
        }
        else{
            label = "(in milliliters)";
        }

        //initializing data
        Map<String, Double> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Consumed",amountDrank);
        if (amountNeeded > 0) {
            typeAmountMap.put("Needed", amountNeeded);
        }

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

    /**
     * Initializes Pie Chart Settings
     */
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
        waterPieChart.setHoleRadius(25f);
        waterPieChart.setTransparentCircleRadius(30f);
    }

    //convert oz to milli
    private double convertOztoMilli(double oz){
        oz *= 29.5735;
        return oz;
    }

    private double convertMillitoOz(double ml){
        ml /= 29.5735;
        return ml;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.waterSubmitButton):
                try {

                    inputAmount = Double.parseDouble(waterAmountInput.getText().toString());
                    amountDrank += inputAmount;
                    amountNeeded = Math.max(0, amountNeeded - inputAmount);
                    initPieChart();
                    showPieChart();

                } catch (NumberFormatException e) {
                    toastWarning();
                }
                break;
            case (R.id.unitChangeButton):
                if (inOz(unitChange)){
                    unitChange.setText("ml");
                    amountDrank = convertOztoMilli(amountDrank);
                    amountNeeded = convertOztoMilli(amountNeeded);
                    toastConvertedtoML();
                }
                else if(!inOz(unitChange)){
                    unitChange.setText("oz");
                    amountDrank = convertMillitoOz(amountDrank);
                    amountNeeded = convertMillitoOz(amountNeeded);
                    toastConvertedtoOz();
                }
                break;

        }
    }

    // checks if a button text is in oz
    private boolean inOz(Button button) {
        if (unitChange.getText().equals("oz")){
            return true;
        }
        else {
            return false;
        }
    }
}