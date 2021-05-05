package com.BAMM.nutrimons;

import android.os.Bundle;

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
public class NutrientInformation extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageButton caloriesButton, proteinButton, carbsButton, sugarButton, fiberButton, fatsButton,
        cholesterolButton, saturatedFatsButton, unsaturatedFatsButton, transFatsButton,
        vitaminAButton, vitaminCButton, vitaminDButton, sodiumButton, potassiumButton, calciumButton, ironButton;
    WebView caloriesWV, proteinWV, carbsWV, sugarWV, fiberWV, fatsWV, cholesterolWV, saturatedFatsWV,
        unsaturatedFats1WV, unsaturatedFats2WV, transFatsWV, vitaminAWV, vitaminCWV, vitaminDWV,
        sodiumWV, potassiumWV, calciumWV, ironWV;
    final String NUTRIENT_URL = "https://medlineplus.gov/definitions/nutritiondefinitions.html",
            VITAMIN_URL = "https://medlineplus.gov/definitions/vitaminsdefinitions.html",
            MINERAL_URL = "https://medlineplus.gov/definitions/mineralsdefinitions.html";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NutrientInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NutrientInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static NutrientInformation newInstance(String param1, String param2) {
        NutrientInformation fragment = new NutrientInformation();
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
        View v = inflater.inflate(R.layout.fragment_nutrient_information, container, false);

        caloriesButton = v.findViewById(R.id.caloriesLink);
        proteinButton = v.findViewById(R.id.proteinLink);
        carbsButton = v.findViewById(R.id.carbsLink);
        sugarButton = v.findViewById(R.id.sugarLink);
        fiberButton = v.findViewById(R.id.fiberLink);
        fatsButton = v.findViewById(R.id.fatsLink);
        cholesterolButton = v.findViewById(R.id.cholesterolLink);
        saturatedFatsButton = v.findViewById(R.id.saturatedFatsLink);
        unsaturatedFatsButton = v.findViewById(R.id.unsaturatedFatsLink);
        transFatsButton = v.findViewById(R.id.transFatsLink);
        vitaminAButton = v.findViewById(R.id.vitaminALink);
        vitaminCButton = v.findViewById(R.id.vitaminCLink);
        vitaminDButton = v.findViewById(R.id.vitaminDLink);
        sodiumButton = v.findViewById(R.id.sodiumLink);
        potassiumButton = v.findViewById(R.id.potassiumLink);
        calciumButton = v.findViewById(R.id.calciumLink);
        ironButton = v.findViewById(R.id.ironLink);

        caloriesButton.setOnClickListener(this);
        proteinButton.setOnClickListener(this);
        carbsButton.setOnClickListener(this);
        sugarButton.setOnClickListener(this);
        fiberButton.setOnClickListener(this);
        fatsButton.setOnClickListener(this);
        cholesterolButton.setOnClickListener(this);
        saturatedFatsButton.setOnClickListener(this);
        unsaturatedFatsButton.setOnClickListener(this);
        transFatsButton.setOnClickListener(this);
        vitaminAButton.setOnClickListener(this);
        vitaminCButton.setOnClickListener(this);
        vitaminDButton.setOnClickListener(this);
        sodiumButton.setOnClickListener(this);
        potassiumButton.setOnClickListener(this);
        calciumButton.setOnClickListener(this);
        ironButton.setOnClickListener(this);

        caloriesWV = v.findViewById(R.id.caloriesWV);
        proteinWV = v.findViewById(R.id.proteinWV);
        carbsWV = v.findViewById(R.id.carbsWV);
        sugarWV = v.findViewById(R.id.sugarWV);
        fiberWV = v.findViewById(R.id.fiberWV);
        fatsWV = v.findViewById(R.id.fatsWV);
        cholesterolWV = v.findViewById(R.id.cholesterolWV);
        saturatedFatsWV = v.findViewById(R.id.saturatedFatsWV);
        unsaturatedFats1WV = v.findViewById(R.id.unsaturatedFats1WV);
        unsaturatedFats2WV = v.findViewById(R.id.unsaturatedFats2WV);
        transFatsWV = v.findViewById(R.id.transFatsWV);
        vitaminAWV = v.findViewById(R.id.vitaminAWV);
        vitaminCWV = v.findViewById(R.id.vitaminCWV);
        vitaminDWV = v.findViewById(R.id.vitaminDWV);
        sodiumWV = v.findViewById(R.id.sodiumWV);
        potassiumWV = v.findViewById(R.id.potassiumWV);
        calciumWV = v.findViewById(R.id.calciumWV);
        ironWV = v.findViewById(R.id.ironWV);

        return v;
    }

    private String trimHTML(String keptText)
    {
        return "javascript:(function() { " +
                    "var article = document.getElementsByTagName('article')[0];" +
                    "var h2s = document.getElementsByTagName('article')[0].getElementsByTagName('h2');" +
                    "var ps = document.getElementsByTagName('article')[0].getElementsByTagName('p');" +

                    "document.getElementsByTagName('header')[0].style.display = 'none';" +
                    "document.getElementById('coop').style.display = 'none';" +
                    "document.getElementById('breadcrumbs').style.display = 'none';" +
                    "ps[0].style.display = 'none';" +
                    "ps[1].style.display = 'none';" +
                    "document.getElementsByClassName('page-info')[0].style.display = 'none';" +

                    "for (var i = 0; i < h2s.length; ++i) {" + //can't use int in javascript
                        "if (h2s[i].innerHTML != '" + keptText + "') {" +
                            "h2s[i].style.display = 'none'; " +
                            "ps[i + 2].style.display = 'none'; }}" +

                    "document.getElementById('page-feedback').style.display = 'none';" +
                    "document.getElementsByTagName('footer')[0].style.display = 'none';" +
                "})()";
    }

    private void setWebView(WebView wv, String url, String keptText)
    {
        if(wv.getVisibility() == View.GONE)
        {
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    wv.loadUrl(trimHTML(keptText));
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
            case(R.id.caloriesLink):
                setWebView((WebView) caloriesWV, NUTRIENT_URL, "Calories");
                break;
            case(R.id.proteinLink):
                setWebView((WebView) proteinWV, NUTRIENT_URL, "Protein");
                break;
            case(R.id.carbsLink):
                setWebView((WebView) carbsWV, NUTRIENT_URL, "Carbohydrates");
                break;
            case(R.id.sugarLink):
                setWebView((WebView) sugarWV, NUTRIENT_URL, "Sugar");
                break;
            case(R.id.fiberLink):
                setWebView((WebView) fiberWV, NUTRIENT_URL, "Fiber");
                break;
            case(R.id.fatsLink):
                setWebView((WebView) fatsWV, NUTRIENT_URL, "Total Fat");
                break;
            case(R.id.cholesterolLink):
                setWebView((WebView) cholesterolWV, NUTRIENT_URL, "Cholesterol");
                break;
            case(R.id.saturatedFatsLink):
                setWebView((WebView) saturatedFatsWV, NUTRIENT_URL, "Saturated Fat");
                break;
            case(R.id.unsaturatedFatsLink):
                setWebView((WebView) unsaturatedFats1WV, NUTRIENT_URL, "Monounsaturated Fat");
                setWebView((WebView) unsaturatedFats2WV, NUTRIENT_URL, "Polyunsaturated Fat");
                break;
            case(R.id.transFatsLink):
                setWebView((WebView) transFatsWV, NUTRIENT_URL, "Trans Fat");
                break;
            case(R.id.vitaminALink):
                setWebView((WebView) vitaminAWV, VITAMIN_URL, "Vitamin A");
                break;
            case(R.id.vitaminCLink):
                setWebView((WebView) vitaminCWV, VITAMIN_URL, "Vitamin C");
                break;
            case(R.id.vitaminDLink):
                setWebView((WebView) vitaminDWV, VITAMIN_URL, "Vitamin D");
                break;
            case(R.id.sodiumLink):
                setWebView((WebView) sodiumWV, MINERAL_URL, "Sodium");
                break;
            case(R.id.potassiumLink):
                setWebView((WebView) potassiumWV, MINERAL_URL, "Potassium");
                break;
            case(R.id.calciumLink):
                setWebView((WebView) calciumWV, MINERAL_URL, "Calcium");
                break;
            case(R.id.ironLink):
                setWebView((WebView) ironWV, MINERAL_URL, "Iron");
                break;
        }
    }
}