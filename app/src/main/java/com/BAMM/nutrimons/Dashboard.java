package com.BAMM.nutrimons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.BAMM.nutrimons.database.AppDatabase;
import com.BAMM.nutrimons.database.DateData;
import com.BAMM.nutrimons.database.User;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    private OnFragmentInteractionListener mListener;

    PieChart caloriesPieChart;
    private BarChart macrosChart;
    private LineChart caloriesOverTimeChart;

    // vars for horizontal bar chart
    private static int MAX_X_VALUE;
    private static final String SET_LABEL = "Nutrient Overview";
    private static final String[] MACRO_NUTRIENTS = { "PROTEINS %", "CARBOHYDRATES %", "FATS %"};
    private List<Float> NUTRIENT_VALUES = new ArrayList<>(), NUTRIENTS_DRI = new ArrayList<>();

    private List<String> allDates;
    private List<Float> allCalories;
    private ArrayList<Entry> XaxisF;

    private String dateString;

    Button goToMeal, goToWater, goToExercise, goToTamagotchi;
    ImageView gotToProfile, goToPreviousDate, goToNextDate;
    int currentIndex;

    private TextView factTextView;
    int tenSec = 10000;
    Handler handler = new Handler();
    Runnable runnable;


    // vars for calendar
    private TextView currentDate;
    private ImageButton goToCalendar;

    //Resources res = this.getResources();
    //private String[] factBank = res.getStringArray(R.array.factBank);
    private String[] factBank;
    /*private String[] factBank = new String[]{
            "Milk is 87% water. The nutrients, like protein, carbohydrate, vitamins and minerals are all found in the other 13%.",
            "Fluid needs vary depending on your age and gender. Teens and adults need anywhere between 8 and 13 cups of fluid each day. Water is great, but milk, juice, soup and anything else you drink also count as fluid.",
            "Most supermarket wasabi is actually horseradish.",
            "One fast food burger can have meat from 100 different cows.",
            "White chocolate isn’t chocolate.",
            "The red food dye for Skittles is made from boiled beetles.",
            "Chocolate has been used as a currency in Ancient civilizations of Mexico and South America.",
            "Froot Loops are all the same flavor.",
            "In Japan, chefs have to train for over two years in order to qualify to serve pufferfish.",
            "Thomas Jefferson made pasta popular in the U.S.",
            "Bad eggs will float.",
            "Popsicles were invented by accident.",
            "Bird saliva is a delicacy in China.",
            "Coffee beans can help eliminate bad breath.",
            "Green, yellow, and red bell peppers are not actually the same vegetable.",
            "Many shredded cheeses and cereals contain cellulose (wood pulp).",
            "Ketchup was once believed to have medicinal qualities that could cure, among other ailments, diarrhea.",
            "A typical ear of corn has an even number of rows.",
            "One burger patty can contain hundreds of different cows.",
            "Scientists can turn peanut butter into diamonds.",
            "Fruit snacks and cars are coated in the same type of wax.",
            "Ripe cranberries will bounce like rubber balls.",
            "An 11-year-old invented the Popsicle by accident.",
            "Farm-raised salmon is naturally white and then dyed pink.",
            "Apple pie is not American.",
            "Potatoes can absorb and reflect Wi-fi signals.",
            "Raw oysters are still alive when you eat them.",
            "Every banana you eat is a clone.",
            "In fact, bananas are technically berries — and strawberries are not.",
            "Before being domesticated, chickens only produced about a dozen eggs a year. Now they can produce hundreds.",
            "Honey will never ever go bad.",
            "Carrots were originally purple.",
            "Most wasabi is actually just dyed horseradish.",
            "People once thought tomatoes were poisonous.",
            "Grapes will explode if you put them in the microwave.",
            "Crackers will give you cavities faster than candy.",
            "Eating too much nutmeg has the effect of a hallucinogenic drug.",
            "Chicken wings were considered throwaway parts before Buffalo wings were invented.",
            "Processed cheese was invented in Switzerland, not America.",
            "Chili peppers contain a chemical that tricks your mouth into \"thinking\" it's being burned — that's why spicy food hurts so much.",
    };*/
    private AppDatabase mDb;

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
        factBank = this.getResources().getStringArray(R.array.factBank);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Dashboard");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mDb = AppDatabase.getInstance(getContext());

        // Button and image initialization
//        gotToProfile = view.findViewById(R.id.imageProfile);
        goToMeal = view.findViewById(R.id.dashboardAddMeal);
        goToWater = view.findViewById(R.id.dashboardAddWater);
        goToExercise = view.findViewById(R.id.dashboardAddExercise);
        goToTamagotchi = view.findViewById(R.id.dashboardTamagotchi);
        factTextView = view.findViewById(R.id.textViewFunFactText);

        DateData dateData = BAMM.getDateData();
        dateData.aggregateNutrients();
        mDb.dateDataDao().updateDateData(dateData);

        List<Float> nutValuesTemp = dateData.nutrientsToFloatList();
        NUTRIENT_VALUES.add(nutValuesTemp.get(2)); //protein
        NUTRIENT_VALUES.add(nutValuesTemp.get(3)); //carbs
        NUTRIENT_VALUES.add(nutValuesTemp.get(7) + nutValuesTemp.get(8) + nutValuesTemp.get(9)); //fats
        NUTRIENT_VALUES.add(nutValuesTemp.get(0)); //calories

        MAX_X_VALUE = MACRO_NUTRIENTS.length;

        User u = mDb.userDao().findByUserID(mDb.tokenDao().getUserID());
        List<Float> nutrientDRIsTemp = u.DRIToFloatList();
        NUTRIENTS_DRI.add(nutrientDRIsTemp.get(2));
        NUTRIENTS_DRI.add(nutrientDRIsTemp.get(3));
        NUTRIENTS_DRI.add(nutrientDRIsTemp.get(7) + nutrientDRIsTemp.get(8) + nutrientDRIsTemp.get(9));
        NUTRIENTS_DRI.add(nutrientDRIsTemp.get(0));

        //create and show the piechart for calories
        caloriesPieChart = view.findViewById(R.id.caloriesPieChart_view);
        initPieChart();
        showPieChart();

        //create and show the horizontal bar chart for macros
        macrosChart = view.findViewById(R.id.macrosChart_view);
        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);

        allDates = mDb.dateDataDao().getAllDates();
        allCalories = mDb.dateDataDao().getAllCalories();
        caloriesOverTimeChart = view.findViewById(R.id.overTimeChart_view);
        for(int i = 0; i < allDates.size(); ++i)
        {
            Log.d("data: " + i, allDates.get(i) + " " + allCalories.get(i));
        }
        if(allDates.size() > 1)
            renderData();
        else
            caloriesOverTimeChart.setNoDataText("Use the app for more than one day to see this chart");

        // assign listener for buttons
        goToMeal.setOnClickListener(this);
        goToWater.setOnClickListener(this);
        goToExercise.setOnClickListener(this);
        goToTamagotchi.setOnClickListener(this);

        updateFact();

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, tenSec);
                updateFact();
            }
        }, tenSec);

        // calendar
        currentDate = view.findViewById(R.id.currentDateTextView);
        Bundle bundle = this.getArguments();
        if(bundle == null){
            currentDate.setText(dateString);
        }else{
            String date = bundle.getString("key");
            currentDate.setText(date);
        }

        // button that goes to the calendar view
        goToCalendar = view.findViewById(R.id.calendarImageButton);
        goToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_calendar);
                Bundle bundle = new Bundle();
                String date = currentDate.getText().toString();
                bundle.putString("currentDateKey", date);
                CalendarViewFragment fragment = new CalendarViewFragment();
                fragment.setArguments(bundle);
                fragment.setArguments(savedInstanceState);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_dashboard, fragment).addToBackStack(null).commit();
            }
        });

        // button initialization for previous day
        goToPreviousDate= view.findViewById(R.id.previousDayImageButton);
        goToPreviousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDay = currentDate.getText().toString();
                try{
                    Date currentDateVal =new SimpleDateFormat("MM/dd/yyyy").parse(currentDay);
                    Date yesterday = new Date(currentDateVal.getTime() - (1000 * 60 * 60 * 24));
                    SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                    String dateString = Date.format(yesterday);
                    currentDate.setText(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // button initialization for next day
        goToNextDate= view.findViewById(R.id.nextDayImageButton);
        goToNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDay = currentDate.getText().toString();
                try{
                    Date currentDateVal =new SimpleDateFormat("MM/dd/yyyy").parse(currentDay);
                    Date tomorrow = new Date(currentDateVal.getTime() + (1000 * 60 * 60 * 24));
                    SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
                    String dateString = Date.format(tomorrow);
                    currentDate.setText(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    /*@Override
    public void onResume()
    {

        super.onResume();
        View v = getView().findViewById(R.id.fragment_dashboard);

        try {
            if(mDb.tokenDao().getUserID() == 0) //no user logged in
            {
                Log.d("user not found", "id: " + mDb.tokenDao().getUserID());
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_login);
            }
            else //user logged in
            {
                Log.d("user found", "id: " + mDb.tokenDao().getUserID());
                //Navigation.findNavController(view).navigate(R.id.action_nav_login_to_nav_dashboard);
            }
        }
        catch(NullPointerException e) //fresh database
        {
            mDb.tokenDao().insert(new Token(0));
            Log.d("new token", "id: " + mDb.tokenDao().getUserID());
            Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_registration);
        }
    }*/

    @Override
      public void onClick(View v) {
        switch (v.getId()) {
            /*case (R.id.imageProfile):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_profile);
                break;*/
            case (R.id.dashboardAddMeal):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_meal);
                break;
            case (R.id.dashboardAddWater):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_water);
                break;
            case (R.id.dashboardAddExercise):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_exercise);
                break;
                case (R.id.dashboardTamagotchi):
                Navigation.findNavController(v).navigate(R.id.action_nav_dashboard_to_nav_tamagotchi);
                break;
        }
    }

    private void updateFact() {
        Random rand = new Random();
        currentIndex = rand.nextInt(factBank.length);
        String factTextResId = factBank[currentIndex];
        factTextView.setText(factTextResId);
    }

    private void showPieChart(){

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "(Calories)";

        //initializing data
        Map<String, Double> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Consumed", (double) NUTRIENT_VALUES.get(3));
        if(NUTRIENTS_DRI.get(3) - NUTRIENT_VALUES.get(3) > 0)
            typeAmountMap.put("Remaining", (double) NUTRIENTS_DRI.get(3) - NUTRIENT_VALUES.get(3));
        else
            typeAmountMap.put("Remaining", 0d);

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#bf6bff"));
        colors.add(Color.parseColor("#ff4d85"));
        //colors.add(Color.parseColor("#304567"));
        /*
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
        pieDataSet.setValueTextSize(20f);
        //setting text size of the entry label (in graph)
        pieDataSet.setValueTextColor(Color.parseColor("#000000"));
        caloriesPieChart.setEntryLabelColor(Color.parseColor("#000000"));
        caloriesPieChart.setEntryLabelTextSize(15f);

        caloriesPieChart.setDrawEntryLabels(false);

        /*
        // value label is set to black and outside the graph. Also,
        pieChart.setExtraLeftOffset(20f);
        pieChart.setExtraRightOffset(20f);
        //pieDataSet.setValueTextColor(Color.BLACK);
        //pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
         */
        //setting legend
        caloriesPieChart.getLegend().setWordWrapEnabled(true);
        caloriesPieChart.getLegend().setTextSize(20f);

        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);
        // change the value to percentage
        //pieData.setValueFormatter(new PercentFormatter());
        caloriesPieChart.setData(pieData);
        caloriesPieChart.invalidate();
    }

    private void initPieChart(){
        //using percentage as values instead of amount
        caloriesPieChart.setUsePercentValues(false);

        //remove the description label on the lower left corner, default true if not set
        caloriesPieChart.getDescription().setEnabled(false);

        // remove legend
        //pieChart.getLegend().setEnabled(false);

        //enabling the user to rotate the chart, default true
        caloriesPieChart.setRotationEnabled(true);
        //adding friction when rotating the pie chart
        caloriesPieChart.setDragDecelerationFrictionCoef(0.9f);
        //setting the first entry start from right hand side, default starting from top
        caloriesPieChart.setRotationAngle(0);

        //highlight the entry when it is tapped, default true if not set
        caloriesPieChart.setHighlightPerTapEnabled(true);
        //adding animation so the entries pop up from 0 degree
        caloriesPieChart.animateY(1400, Easing.EaseInOutQuad);
        //setting the color of the hole in the middle, default white
        caloriesPieChart.setHoleColor(Color.parseColor("#ffffff"));
        caloriesPieChart.setHoleRadius(40f);
        caloriesPieChart.setTransparentCircleRadius(0f);
    }

    private void configureChartAppearance() {
        macrosChart.getDescription().setEnabled(false);

        XAxis xAxis = macrosChart.getXAxis();
        xAxis.setLabelCount(MAX_X_VALUE);   // sets the number of labels for the y-axis
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return MACRO_NUTRIENTS[(int) value];
            }
        });
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            float x = i;
            float y = NUTRIENT_VALUES.get(i) / NUTRIENTS_DRI.get(i) * 100;
            Log.d("nuts", NUTRIENT_VALUES.get(i) + " " + NUTRIENTS_DRI.get(i) );
            values.add(new BarEntry(x, y));
        }
        BarDataSet set1 = new BarDataSet(values, SET_LABEL);
        set1.setColors(Color.parseColor("#de882c"), Color.parseColor("#19a852"), Color.parseColor("#3c8da3"));

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        macrosChart.setData(data);
        macrosChart.invalidate();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (Dashboard.OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

    private void renderData() {
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = caloriesOverTimeChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setLabelCount(allDates.size());   // sets the number of labels for the y-axis
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd yyyy");
        String s0 = BAMM.getDateString();
        s0 = s0.substring(s0.lastIndexOf('/') + 1) + '/' + s0.substring(0, s0.lastIndexOf('/'));
        s0 = s0.replace('/', '-');
        LocalDate d0 = LocalDate.parse(s0);
        Log.d("local date0", d0.toString());
        String s00 = allDates.get(0);
        s00 = s00.substring(s00.lastIndexOf('/') + 1) + '/' + s00.substring(0, s00.lastIndexOf('/'));
        s00 = s00.replace('/', '-');
        LocalDate d00 = LocalDate.parse(s00);
        long maxX = d0.toEpochDay() - d00.toEpochDay() + 1;
        XaxisF = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        for(int i = 0, j = 0; i < maxX; ++i)
        {
            String s = allDates.get(j);
            s = s.substring(s.lastIndexOf('/') + 1) + '/' + s.substring(0, s.lastIndexOf('/'));
            s = s.replace('/', '-');
            LocalDate d = LocalDate.parse(s);
            s = s.substring(s.indexOf('-') + 1);
            if(d00.toEpochDay() + i == d.toEpochDay())
            {
                XaxisF.add(new Entry(i, allCalories.get(j++)));
                dates.add(s);
            }
            else
            {
                XaxisF.add(new Entry(i, 0));
                dates.add("");
            }
        }
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Log.d("value", "" + value);
                return dates.get((int) value);
            }
        });
        xAxis.setDrawLimitLinesBehindData(true);

        float cals = Float.parseFloat(mDb.userDao().findByUserID(mDb.tokenDao().getUserID()).calories);
        LimitLine ll1 = new LimitLine(cals, "Daily Calories Needed");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        YAxis leftAxis = caloriesOverTimeChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        caloriesOverTimeChart.getAxisRight().setEnabled(false);
        setData();
    }

    private void setData() {

        LineDataSet set1;
        if (caloriesOverTimeChart.getData() != null &&
                caloriesOverTimeChart.getData().getDataSetCount() > 0) {
            caloriesOverTimeChart.getDescription().setText("Calories Over Time");
            caloriesOverTimeChart.getDescription().setTextSize(16);
            set1 = (LineDataSet) caloriesOverTimeChart.getData().getDataSetByIndex(0);
            set1.setValues(XaxisF);
            caloriesOverTimeChart.getData().notifyDataChanged();
            caloriesOverTimeChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(XaxisF, "Daily Calories");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                set1.setFillColor(Color.BLUE);
            } else {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            caloriesOverTimeChart.setData(data);
        }
    }
}