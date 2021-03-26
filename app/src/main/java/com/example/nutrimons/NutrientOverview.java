package com.example.nutrimons;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;
import com.example.nutrimons.database.Meal;
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
public class NutrientOverview extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private static /*final*/ int MAX_X_VALUE;// = 18;
    //private static final int MAX_Y_VALUE = 120;
    //private static final int MIN_Y_VALUE = 0;
    private static final String SET_LABEL = "Percent of Daily Needs";
    private static /*final*/ String[] NUTRIENTS = new String[] {};/*= { "CARBOHYDRATES", "PROTEINS", "FATS",  "VITAMIN B1", "VITAMIN B2", "VITAMIN B6", "VITAMIN B12",
            "VITAMIN C", "FOLIC ACID", "VITAMIN A", "VITAMIN D", "VITAMIN E", "VITAMIN K", "CALCIUM", "POTASSIUM", "SODIUM", "IRON", "ZINC" };*/
    private static List<Float> NUTRIENT_VALUES, NUTRIENTS_DRI, NUTRIENTS_UL;

    private HorizontalBarChart chart;

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

        long date = System.currentTimeMillis();
        SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = Date.format(date);

        mDb = AppDatabase.getInstance(getContext());
        DateData dateData = mDb.dateDataDao().findByDate(dateString);
        dateData.aggregateNutrients();

        List<String> nuts = dateData.nutrientsToStringList();
        NUTRIENTS = nuts.toArray(NUTRIENTS);

        NUTRIENT_VALUES = dateData.nutrientsToFloatList();

        MAX_X_VALUE = NUTRIENTS.length;

        User u = mDb.userDao().findByUserID(mDb.tokenDao().getUserID());
        NUTRIENTS_DRI = u.DRIToFloatList();
        NUTRIENTS_UL = u.ULToFloatList();

        chart = view.findViewById(R.id.horizontalBarChart);

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);

        return view;
    }

    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(MAX_X_VALUE);   // sets the number of labels for the y-axis
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (NUTRIENTS[(int) value]);
            }
        });
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            float x = i;
            float y;
            if(NUTRIENT_VALUES.get(i) == 0)
                y = 0;
            else if(NUTRIENTS_DRI.get(i) == 0)
                y = 100;
            else if(NUTRIENT_VALUES.get(i) / NUTRIENTS_DRI.get(i) > 1)
                y = 100;
            else
                y = NUTRIENT_VALUES.get(i) / NUTRIENTS_DRI.get(i) * 100;
            values.add(new BarEntry(x, y));
        }
        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(11f);
        chart.setData(data);
        chart.invalidate();
    }
}