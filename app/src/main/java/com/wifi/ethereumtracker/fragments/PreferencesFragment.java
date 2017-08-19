package com.wifi.ethereumtracker.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.activities.MainActivity;
import com.wifi.ethereumtracker.broadcastReceivers.AlarmReceiver;
import com.wifi.ethereumtracker.model.profiles.CexProfile;
import com.wifi.ethereumtracker.model.profiles.GeminiProfile;
import com.wifi.ethereumtracker.services.BackgroundCheckService;

import java.util.Calendar;
import java.util.List;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ListPreference listPreferenceSourceSettings = (ListPreference) findPreference("sourceSettings");
        final ListPreference listPreferenceCurrencySettings = (ListPreference) findPreference("currencySettings");

        SwitchPreference switchPreferenceEnableNotifications = (SwitchPreference)
                findPreference("enableNotificationsSettings");

        final ListPreference listPreferenceCheckInterval = (ListPreference) findPreference("checkInterval");
        final EditTextPreference editTextValueMinNotify = (EditTextPreference) findPreference("valueMinNotify");


        listPreferenceSourceSettings.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String s = (String) o;

                if(s.equals(CexProfile.baseUrl)){
                    listPreferenceCurrencySettings.setEntries(CexProfile.currencies);
                    listPreferenceCurrencySettings.setEntryValues(CexProfile.currencies);

                }else if(s.equals(GeminiProfile.baseUrl)){
                    listPreferenceCurrencySettings.setEntries(GeminiProfile.currencies);
                    listPreferenceCurrencySettings.setEntryValues(GeminiProfile.currencies);
                }

                listPreferenceCurrencySettings.setValue("USD");

                return true;
            }
        });


        switchPreferenceEnableNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                boolean status = (boolean) o;

                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                        intent, 0);

                AlarmManager alarmManager = (AlarmManager)
                        getActivity().getSystemService(Context.ALARM_SERVICE);

                int waitInterval = Integer.parseInt(listPreferenceCheckInterval.getValue());

                if(status){
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            Calendar.getInstance().getTimeInMillis()+waitInterval,
                            waitInterval, pendingIntent);

                }else{
                    alarmManager.cancel(pendingIntent);
                }

                return true;
            }
        });



        listPreferenceCheckInterval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                // when interval changes cancel alarm manger and start new one,
                // so new interval gets applied

                int waitInterval = Integer.parseInt((String) o);

                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                        intent, 0);

                AlarmManager alarmManager = (AlarmManager)
                        getActivity().getSystemService(Context.ALARM_SERVICE);

                alarmManager.cancel(pendingIntent);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        Calendar.getInstance().getTimeInMillis()+waitInterval,
                        waitInterval, pendingIntent);

                return true;
            }
        });

    }

}
