package com.example.nutrimons;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.Meal;
import com.example.nutrimons.database.User;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Water extends Fragment implements View.OnClickListener{
    // Variables for pie chart
    PieChart waterPieChart;
    private float amountDrank = 0.0f;
    private float amountNeeded; //Liters
    private float inputAmount;
    private String unit;

    EditText waterAmountInput;
    Button submitWater;
    Button unitChange;


    SimpleDateFormat Date;
    String dateString;
    long date;
    // creates instance of database
    private AppDatabase mDb;

    private View view;

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
        view = inflater.inflate(R.layout.fragment_water, container, false);
        waterPieChart = view.findViewById(R.id.waterPieChart_view);
        submitWater = view.findViewById(R.id.waterSubmitButton);
        submitWater.setOnClickListener(this);
        waterAmountInput = view.findViewById(R.id.waterAmountInputBox);
        unitChange = view.findViewById(R.id.unitChangeButton);
        unitChange.setOnClickListener(this);
        //Toast.makeText(getContext(), "dateString.getClass().getName()", Toast.LENGTH_SHORT).show();

//        // database
        mDb = AppDatabase.getInstance(getContext());

        long date = System.currentTimeMillis();
        SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = Date.format(date);
        Log.d("WaterDateString", dateString);
        Log.d("WaterDateString", String.valueOf(dateString.equals("03/13/2021")));

        amountNeeded = Float.parseFloat(mDb.userDao().findByUserID(mDb.tokenDao().getUserID()).water) * 33.81402f; //convert to fl oz

//        com.example.nutrimons.database.DateData dateTest = new com.example.nutrimons.database.DateData(dateString, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), 1.1);
//        com.example.nutrimons.database.DateData nullDateTest = null;
//        com.example.nutrimons.database.DateData obj1 = new com.example.nutrimons.database.DateData(dateString, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), 1.1);
//        com.example.nutrimons.database.DateData obj2 = new com.example.nutrimons.database.DateData(dateString, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), 1.1);
//        //com.example.nutrimons.database.DateData findByObj = mDb.dateDataDao().findByDate(dateString);
//        if (dateTest == null){
//            Log.d("WaterDateString", "dateObject: NULL");
//        } else if (dateTest != null){
//            Log.d("WaterDateString", "dateObject: NOT NULL");
//        }
//
//        if (nullDateTest == null){
//            Log.d("WaterDateString", "dateObjectNull: NULL");
//        } else if (nullDateTest != null){
//            Log.d("WaterDateString", "dateObjectNullNOT NULL");
//        }
//
//
//        if (obj1 == obj2){
//            Log.d("WaterDateString", "Obj: Equal");
//        } else if (obj1 != obj2){
//            Log.d("WaterDateString", "Obj: NOT EQUAL");
//        }

        // FIND BY
//        if(mDb.dateDataDao().findByDate(dateString) == null){
//            Log.d("WaterDateString", "FindBy: NULL");
//        } else{
//            Log.d("WaterDateString", "FindBy: NOT NULL");
//        }

        if(mDb.dateDataDao().findByDate(dateString) == null){
            amountDrank = 0.0f;
            amountNeeded = Float.parseFloat(mDb.userDao().findByUserID(mDb.tokenDao().getUserID()).water) * 33.81402f; //convert to fl oz
        }else {
            List<Float> temp = (List<Float>)mDb.dateDataDao().findWaterByDate(dateString);
            amountDrank = ((Float)temp.get(0) == null) ? 0.0f : (Float)temp.get(0);
            unit = ((String)mDb.dateDataDao().findByDate(dateString).water_unit == null ? "oz" : (mDb.dateDataDao().findByDate(dateString).water_unit.equals("oz") ? "oz" : "ml"));
            amountNeeded -= amountDrank;
        }
//        try{
//            List<Double> temp = (List<Double>)mDb.dateDataDao().findWaterByDate(dateString);
//            amountDrank = ((Double)temp.get(0) == null) ? 0.0 : (Double)temp.get(0);
//            amountNeeded -= amountDrank;
//        } catch(NullPointerException e) {
//            amountDrank = 0.0;
//            amountNeeded = 120.0;
//        }
        initPieChart();
        showPieChart(); // this method has to go after unitChange because it uses unitChange



        return view;
    }
//    @Override
//    public void onResume() {
//        // database
//        date = System.currentTimeMillis();
//        Date = new SimpleDateFormat("MM/dd/yyyy");
//        dateString = Date.format(date);
//        Toast.makeText(getContext(), dateString.getClass().getName(), Toast.LENGTH_SHORT).show();
//
//        if(mDb.dateDataDao().findByDate(dateString) == null){
//            amountDrank = 0.0;
//            amountNeeded = 120.0;
//        }else {
//            List<Double> temp = (List<Double>)mDb.dateDataDao().findWaterByDate(dateString);
//            amountDrank = ((Double)temp.get(0) == null) ? 0.0 : (Double)temp.get(0);
//            amountNeeded -= amountDrank;
//        }
//        super.onResume();
//
//    }
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
        if(unit.equals("oz")){//inOz(unitChange)){
            label = "(in ounces)";
            unitChange.setText("oz");
        }
        else{
            label = "(in milliliters)";
            unitChange.setText("ml");
        }

        //initializing data
        Map<String, Float> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Consumed", amountDrank);
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
    private float convertOztoMilli(float oz){
        oz *= 29.5735;
        return oz;
    }

    private float convertMillitoOz(float ml){
        ml /= 29.5735;
        return ml;
    }
    @Override
    public void onClick(View v) {
        hideKeyboard();
        switch (v.getId()) {
            case (R.id.waterSubmitButton):
                try {
                    inputAmount = Float.parseFloat(waterAmountInput.getText().toString());
                    amountDrank += inputAmount;
                    long date = System.currentTimeMillis();
                    SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                    String dateString = Date.format(date);
                    if(mDb.dateDataDao().findByDate(dateString) == null){
                        com.example.nutrimons.database.DateData dateData = new com.example.nutrimons.database.DateData(dateString, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0f,"ml");
                        dateData.water = amountDrank;
                        dateData.water_unit = unit;
                        for(String s:  dateData.nutrientsToStringList())
                            Log.d("nutrient", s);
                        mDb.dateDataDao().insert(dateData);
                    }
                    else{
                        com.example.nutrimons.database.DateData dateData = mDb.dateDataDao().findByDate(dateString);
                        dateData.water = amountDrank;
                        dateData.water_unit = unit;
                        mDb.dateDataDao().updateDateData(dateData);
                        //mDb.dateDataDao().updateMealPlan(selectedBreakfast, selectedLunch, selectedDinner, selectedSnack, dateString);
                    }
                    amountNeeded = Math.max(0, amountNeeded - inputAmount);
                    initPieChart();
                    showPieChart();

                    //reward user
                    User u = mDb.userDao().findByUserID(mDb.tokenDao().getUserID());
                    u.nutriCoins += 1;
                    mDb.userDao().insert(u);

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
                    unit = "ml";
                }
                else if(!inOz(unitChange)){
                    unitChange.setText("oz");
                    amountDrank = convertMillitoOz(amountDrank);
                    amountNeeded = convertMillitoOz(amountNeeded);
                    toastConvertedtoOz();
                    unit = "oz";
                }
                break;

        }
    }

    private void hideKeyboard()
    {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
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