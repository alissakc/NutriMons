package com.example.nutrimons;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BugReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BugReportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private TextView title, bugReport;
    private EditText name, feedbackName, feedback;
    private Button send;
    private MealPlan.OnFragmentInteractionListener mListener;

    public BugReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BugReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BugReportFragment newInstance(String param1, String param2) {
        BugReportFragment fragment = new BugReportFragment();
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
            mListener.onFragmentInteraction("Report a Bug");
        }
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bug_report, container, false);

        // textviews
        title = (TextView) view.findViewById(R.id.title_bug_report);
        bugReport = (TextView) view.findViewById(R.id.bugReportTextView);

        // editviews
        feedbackName = (EditText) view.findViewById(R.id.feedback_name_bug_report);
        feedback = (EditText) view.findViewById(R.id.send_feedback_bug_report);

        // buttons
        send = (Button) view.findViewById(R.id.send_feedback_button_bug_report);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.putExtra(Intent.EXTRA_EMAIL,new String[]{"BAMM.Tamagotchi@gmail.com"});
                mail.putExtra(Intent.EXTRA_SUBJECT, feedbackName.getText().toString());
                mail.putExtra(Intent.EXTRA_TEXT, feedback.getText().toString());
                mail.setType("message/rfc822");
                startActivity(Intent.createChooser(mail, "Send email via:"));
            }
        });


        return view;
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