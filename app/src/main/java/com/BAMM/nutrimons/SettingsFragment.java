package com.BAMM.nutrimons;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Timer;
import java.util.TimerTask;

public class SettingsFragment extends PreferenceFragmentCompat {

    // vars
    private MealPlan.OnFragmentInteractionListener mListener;
    Timer timer;
    TimerTask timerTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);

        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Settings");
        }
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // notification preference change listener
        ListPreference notificationListPreference = (ListPreference) findPreference("notification");
        notificationListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // newValue is the value you choose
                if (newValue.toString().equals("5")) {
                    // Do nothing
                } else {
                    int timeInMs = 0;
                    switch (newValue.toString()) {
                        case "0": // 1 hours
                            timeInMs = (10000);
//                            timeInMs = (1 * 3600000);
                            break;
                        case "1": // 2 hours
                            timeInMs = (2 * 3600000);
                            break;
                        case "2": // 3 hours
                            timeInMs = (3 * 3600000);
                            break;
                        case "3": // 5 hours
                            timeInMs = (5 * 3600000);
                            break;
                        case "4":    // 10 hours
                            timeInMs = (10 * 3600000);
                            break;
                    }
                    NotificationService n = new NotificationService();
//                    n.setTimeInMs(timeInMs);
//                            startTimer(timeInMs);
                }
                return true;
            }
        });
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

    /*private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "This is a notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground )
                .setContentTitle("Nutrimons want you back")
                .setContentText("Get fit with nutrimons")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    }

    public void startTimer(int timeInMs) {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, timeInMs); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    final Handler handler = new Handler();

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        createNotification();
                        Log.d("Timer", "Notified");
                    }
                });
            }
        };
    }*/
}