package com.example.nutrimons;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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

    Button nutButton, vitButton, minButton;
    View nuts, vits, mins;
    WebView wv;

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

        nutButton = v.findViewById(R.id.NLMnutrientInfo);
        vitButton = v.findViewById(R.id.NLMvitaminInfo);
        minButton = v.findViewById(R.id.NLMmineralInfo);

        nutButton.setOnClickListener(this);
        vitButton.setOnClickListener(this);
        minButton.setOnClickListener(this);

        nuts = v.findViewById(R.id.NLMnutrientInfoWebView);
        vits = v.findViewById(R.id.NLMvitaminInfoWebView);
        mins = v.findViewById(R.id.NLMmineralInfoWebView);



        return v;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("www.example.com".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId()){
            case(R.id.NLMnutrientInfo):
                if(nuts.getVisibility() == View.GONE)
                {
                    WebView wv = (WebView) nuts;
                    wv.loadUrl("https://medlineplus.gov/definitions/nutritiondefinitions.html");
                    wv.setWebViewClient(new MyWebViewClient());
                    nuts.setVisibility(View.VISIBLE);
                }
                else
                    nuts.setVisibility(View.GONE);
                break;
            case(R.id.NLMvitaminInfo):
                if(vits.getVisibility() == View.GONE)
                {
                    WebView wv = (WebView) vits;
                    wv.loadUrl("https://medlineplus.gov/definitions/vitaminsdefinitions.html");
                    wv.setWebViewClient(new MyWebViewClient());
                    vits.setVisibility(View.VISIBLE);
                }
                else
                    vits.setVisibility(View.GONE);
                break;
            case(R.id.NLMmineralInfo):
                if(mins.getVisibility() == View.GONE)
                {
                    WebView wv = (WebView) mins;
                    wv.loadUrl("https://medlineplus.gov/definitions/mineralsdefinitions.html");
                    wv.setWebViewClient(new MyWebViewClient());
                    mins.setVisibility(View.VISIBLE);
                }
                else
                    mins.setVisibility(View.GONE);
                break;
        }
    }
}