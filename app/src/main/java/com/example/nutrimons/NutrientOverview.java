package com.example.nutrimons;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;
import com.example.nutrimons.database.User;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NutrientOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NutrientOverview extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private static int MAX_X_VALUE;
    private static final String SET_LABEL = "Percent of Daily Needs";
    private static String[] nutrients = new String[] {};
    private static List<Float> nutrientValues, nutrientDRIs, nutrientULs;

    private HorizontalBarChart chart;

    private Button toNutrientInfo;

    private AppDatabase mDb;

    public NutrientOverview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NutrientOverview.
     */
    // TODO: Rename and change types and number of parameters
    public static NutrientOverview newInstance(String param1, String param2) {
        NutrientOverview fragment = new NutrientOverview();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nutrient_overview, container, false);

        mDb = AppDatabase.getInstance(getContext());
        DateData dateData = BAMM.getDateData();
        dateData.aggregateNutrients();

        List<String> nuts = dateData.nutrientsToStringList();
        nutrients = nuts.toArray(nutrients);

        nutrientValues = dateData.nutrientsToFloatList();

        MAX_X_VALUE = nutrients.length;

        User u = BAMM.getCurrentUser();
        nutrientDRIs = u.DRIToFloatList();
        nutrientULs = u.ULToFloatList();

        if(dateData.water_unit != null)
        {
            if(dateData.water_unit.equals("ml"))
            {
                nutrientValues.set(1, nutrientValues.get(1) / 1000f);
                nutrients[1] = "Water: " + nutrientValues.get(1) + "L";
            }
            else if(dateData.water_unit.equals("oz"))
            {
                nutrientValues.set(1, nutrientValues.get(1) / 33.814f);
                nutrients[1] = "Water: " + nutrientValues.get(1) + "L";
            }
        }

        chart = view.findViewById(R.id.horizontalBarChart);

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);

        toNutrientInfo = new Button(getContext());
        toNutrientInfo.setBackgroundColor(00000000);
        toNutrientInfo.setOnClickListener(this);
        chart.addView(toNutrientInfo);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        //Log.d("button", "clicked");
        Navigation.findNavController(view).navigate(R.id.action_nav_nutrientOverview_to_nav_nutrientInformation);
    }

    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(MAX_X_VALUE);   // sets the number of labels for the y-axis
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (nutrients[(int) value]);
            }
        });
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        int [] barColorArray = new int[MAX_X_VALUE];
        for (int i = 0; i < MAX_X_VALUE; i++) {
            float x = i;
            float y;
            if(nutrientValues.get(i) == 0) {
                y = 0;
            }else if (nutrientDRIs.get(i) == 0){
                y = 100;
            }else if (nutrientValues.get(i) / nutrientDRIs.get(i) == 1){
                y = 100;
            }else {
                y = (nutrientValues.get(i) / nutrientDRIs.get(i)) * 100;
            }
            Log.d(String.valueOf(i), nutrientValues.get(i) + " " + nutrientDRIs.get(i));

            values.add(new BarEntry(x, y));
            if(y > 100){
                barColorArray[i] = Color.RED;
            }else if(y == 100){
                barColorArray[i] = Color.GREEN;
            }else{
                barColorArray[i] = Color.YELLOW;
            }
        }

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        //set1.setColors(barColorArray);
        set1.setColors(new int[] {Color.RED, Color.GREEN, Color.YELLOW});

        BarData data = new BarData(dataSets);

        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(11f);
        chart.setData(data);
        chart.invalidate();
    }
}