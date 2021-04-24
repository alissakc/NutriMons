package com.example.nutrimons;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NutrientInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sources extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MealPlan.OnFragmentInteractionListener mListener;


    ImageButton funFacts1B, funFacts2B, funFacts3B, calorieCalculator1B, calorieCalculator2B, calorieCalculator3B,
            nutrientRecommendationsB, addMealSearchB, barcodeSearchB, macronutrientInfoB,
            vitaminInfoB, mineralInfoB;
    WebView funFacts1WV, funFacts2WV, funFacts3WV, calorieCalculator1WV, calorieCalculator2WV, calorieCalculator3WV, nutrientRecommendationsWV, addMealSearchWV,
            barcodeSearchWV, unsaturatedFats2WV, macronutrientInfoWV, vitaminInfoWV, mineralInfoWV;
    final String funFacts1_URL = "https://bcdairy.ca/nutritioneducation/articles/fun-nutrition-facts",
            funFacts2_URL = "https://facts.net/general/nutrition-facts/",
            funFacts3_URL = "https://www.healthline.com/nutrition/20-nutrition-facts-that-should-be-common-sense#TOC_TITLE_HDR_2",
            calorieCalculator1_URL = "https://www.k-state.edu/paccats/Contents/PA/PDF/Physical%20Activity%20and%20Controlling%20Weight.pdf",
            calorieCalculator2_URL = "https://en.wikipedia.org/wiki/Basal_metabolic_rate",
            calorieCalculator3_URL = "https://physiqonomics.com/fat-loss/",
            nutrientRecommendations_URL = "https://www.nal.usda.gov/sites/default/files/fnic_uploads/recommended_intakes_individuals.pdf",
            addMealSearch_URL = "https://fdc.nal.usda.gov/index.html",
            barcodeSearch_URL = "https://wiki.openfoodfacts.org/",
            macronutrientInfo_URL = "https://medlineplus.gov/definitions/nutritiondefinitions.html",
            vitaminInfo_URL = "https://medlineplus.gov/definitions/vitaminsdefinitions.html",
            mineralInfo_URL = "https://medlineplus.gov/definitions/mineralsdefinitions.html";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Sources() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sources.
     */
    // TODO: Rename and change types and number of parameters
    public static Sources newInstance(String param1, String param2) {
        Sources fragment = new Sources();
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
        if (mListener != null) {
            mListener.onFragmentInteraction("Sources");
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sources, container, false);

        funFacts1B = v.findViewById(R.id.funFacts1B);
        funFacts2B = v.findViewById(R.id.funFacts2B);
        funFacts3B = v.findViewById(R.id.funFacts3B);
        calorieCalculator1B = v.findViewById(R.id.calorieCalculator1B);
        calorieCalculator2B = v.findViewById(R.id.calorieCalculator2B);
        calorieCalculator3B = v.findViewById(R.id.calorieCalculator3B);
        nutrientRecommendationsB = v.findViewById(R.id.nutrientRecommendationsB);
        addMealSearchB = v.findViewById(R.id.addMealSearchB);
        barcodeSearchB = v.findViewById(R.id.barcodeSearchB);
        macronutrientInfoB = v.findViewById(R.id.macronutrientInfoB);
        vitaminInfoB = v.findViewById(R.id.vitaminInfoB);
        mineralInfoB = v.findViewById(R.id.mineralInfoB);


        funFacts1B.setOnClickListener(this);
        funFacts2B.setOnClickListener(this);
        funFacts3B.setOnClickListener(this);
        calorieCalculator1B.setOnClickListener(this);
        calorieCalculator2B.setOnClickListener(this);
        calorieCalculator3B.setOnClickListener(this);
        nutrientRecommendationsB.setOnClickListener(this);
        addMealSearchB.setOnClickListener(this);
        barcodeSearchB.setOnClickListener(this);
        macronutrientInfoB.setOnClickListener(this);
        vitaminInfoB.setOnClickListener(this);
        mineralInfoB.setOnClickListener(this);


        funFacts1WV = v.findViewById(R.id.funFacts1WV);
        funFacts2WV = v.findViewById(R.id.funFacts2WV);
        funFacts3WV = v.findViewById(R.id.funFacts3WV);
        calorieCalculator1WV = v.findViewById(R.id.calorieCalculator1WV);
        calorieCalculator2WV = v.findViewById(R.id.calorieCalculator2WV);
        calorieCalculator3WV = v.findViewById(R.id.calorieCalculator3WV);
        nutrientRecommendationsWV = v.findViewById(R.id.nutrientRecommendationsWV);
        addMealSearchWV = v.findViewById(R.id.addMealSearchWV);
        barcodeSearchWV = v.findViewById(R.id.barcodeSearchWV);
        macronutrientInfoWV = v.findViewById(R.id.macronutrientInfoWV);
        vitaminInfoWV = v.findViewById(R.id.vitaminInfoWV);
        mineralInfoWV = v.findViewById(R.id.mineralInfoWV);


        return v;
    }

    private void setWebView(WebView wv, String url, String keptText)
    {
        if(wv.getVisibility() == View.GONE)
        {
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    wv.setVisibility(View.VISIBLE);
                }
            });
            wv.loadUrl(url);
        }
        else
            wv.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId()){
            case(R.id.funFacts1B):
                setWebView((WebView) funFacts1WV, funFacts1_URL, "Calories");
                break;
            case(R.id.funFacts2B):
                setWebView((WebView) funFacts2WV, funFacts2_URL, "Protein");
                break;
            case(R.id.funFacts3B):
                setWebView((WebView) funFacts3WV, funFacts3_URL, "Carbohydrates");
                break;
            case(R.id.calorieCalculator1B):
                setWebView((WebView) calorieCalculator1WV, calorieCalculator1_URL, "Sugar");
                break;
            case(R.id.calorieCalculator2B):
                setWebView((WebView) calorieCalculator2WV, calorieCalculator2_URL, "Fiber");
                break;
            case(R.id.calorieCalculator3B):
                setWebView((WebView) calorieCalculator3WV, calorieCalculator3_URL, "Total Fat");
                break;
            case(R.id.nutrientRecommendationsB):
                setWebView((WebView) nutrientRecommendationsWV, nutrientRecommendations_URL, "Cholesterol");
                break;
            case(R.id.addMealSearchB):
                setWebView((WebView) addMealSearchWV, addMealSearch_URL, "Saturated Fat");
                break;
            case(R.id.barcodeSearchB):
                setWebView((WebView) barcodeSearchWV, barcodeSearch_URL, "Monounsaturated Fat");
                break;
            case(R.id.macronutrientInfoB):
                setWebView((WebView) macronutrientInfoWV, macronutrientInfo_URL, "Trans Fat");
                break;
            case(R.id.vitaminInfoB):
                setWebView((WebView) vitaminInfoWV, vitaminInfo_URL, "Vitamin A");
                break;
            case(R.id.mineralInfoB):
                setWebView((WebView) mineralInfoWV, mineralInfo_URL, "Vitamin C");
                break;
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (MealPlan.OnFragmentInteractionListener) context;
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
        void onFragmentInteraction(String title);
    }
}