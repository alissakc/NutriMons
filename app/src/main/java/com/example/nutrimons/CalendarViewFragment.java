package com.example.nutrimons;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // vars
    private CalendarView mCalendarView;
    private Button today;
    private OnFragmentInteractionListener mListener;

    public CalendarViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarViewFragment newInstance(String param1, String param2) {
        CalendarViewFragment fragment = new CalendarViewFragment();
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
            mListener.onFragmentInteraction("Calendar");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar_view, container, false);

        mCalendarView = view.findViewById(R.id.calendarView);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            String date = bundle.getString("currentDateKey");
            try {
                Date currentDateVal = new SimpleDateFormat("MM/dd/yyyy").parse(date);
                long milliseconds = currentDateVal.getTime();
                mCalendarView.setDate(milliseconds);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date;
                if(month+1<10)
                    date = "0" + (month + 1) + "/";
                else
                    date = (month + 1) + "/";
                if(dayOfMonth < 10)
                    date += "0" + dayOfMonth + "/" + year;
                else
                    date += dayOfMonth + "/" + year;
                // send data to dailyInfo fragment
                Bundle bundle = new Bundle();
                bundle.putString("key", date);
                DailyInfoFragment fragment = new DailyInfoFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_calendar, fragment).addToBackStack(null).commit();
                /*
                // send data to dashboard fragment
                Bundle bundle = new Bundle();
                bundle.putString("key", date);
                Dashboard fragment = new Dashboard();
                fragment.setArguments(bundle);
                //Fragment calendarFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_calendar);
                //getActivity().getSupportFragmentManager().beginTransaction().remove(calendarFragment);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_calendar, fragment).addToBackStack(null).commit();
                //Navigation.findNavController(view).navigate(R.id.action_nav_calendar_to_nav_home);
                 */
            }
        });

        today = view.findViewById(R.id.todayButton);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long date = System.currentTimeMillis();
                mCalendarView.setDate(date);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (CalendarViewFragment.OnFragmentInteractionListener) context;
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
}