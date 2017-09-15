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
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.broadcastReceivers.AlarmReceiver;
import com.wifi.ethereumtracker.db.DbHelper;
import com.wifi.ethereumtracker.model.Profile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreferencesFragment extends PreferenceFragment {

    public ListPreference listPreferenceCurrencySettings;
    public EditTextPreference minNotifyValueEditTextPref;
    public EditTextPreference maxNotifyValueEditTextPref;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        DbHelper dbHelper = new DbHelper(getActivity().getApplicationContext());
        List<Profile> profiles = dbHelper.getProfiles();


        final ListPreference listPreferenceSourceSettings = (ListPreference) findPreference("sourceSettings");
        listPreferenceCurrencySettings = (ListPreference) findPreference("currencySettings");


        // checking if myValue is valid
        EditTextPreference myValueEditTextPref = (EditTextPreference) findPreference("myValue");
        myValueEditTextPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                try {
                    Double.parseDouble((String) o);
                }catch (Exception ex){
                    return false;
                }

                return true;
            }
        });


        SwitchPreference switchPreferenceEnableNotifications = (SwitchPreference)
                findPreference("enableNotificationsSettings");

        final ListPreference listPreferenceCheckInterval = (ListPreference) findPreference("checkInterval");

        minNotifyValueEditTextPref = (EditTextPreference) findPreference("valueMinNotify");
        maxNotifyValueEditTextPref = (EditTextPreference) findPreference("valueMaxNotify");

        minNotifyValueEditTextPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                try{
                    Double.parseDouble((String) o);
                }catch (Exception ex){
                    return false;
                }

                return true;
            }
        });

        maxNotifyValueEditTextPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                try{
                    Double.parseDouble((String) o);
                }catch (Exception ex){
                    return false;
                }

                return true;
            }
        });


        // load profiles list from database and send them under as parameter
        setListPrefSourceEntries(listPreferenceSourceSettings, profiles);


        listPreferenceSourceSettings.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String s = (String) o;
                System.out.println(s + "<-- CHOSEN!");

                Profile profile = new Gson().fromJson(s, Profile.class);

                setListPrefCurrencyEntries(listPreferenceCurrencySettings, profile.getCurrencies());

                return true;
            }
        });

        listPreferenceCurrencySettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                String hey = preference.getSharedPreferences().getString("currencySettings", null);
                System.out.println(hey + "<-- DOES IT WORK?!");

                return true;
            }
        });

        switchPreferenceEnableNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                int currentPrice = getPreferenceManager()
                        .getSharedPreferences().getInt("currentPrice", 0);
                int tenPercent = calculate10Percent(currentPrice);

                // set default values
                minNotifyValueEditTextPref.setText(String.valueOf(currentPrice - tenPercent));
                maxNotifyValueEditTextPref.setText(String.valueOf(currentPrice + tenPercent));

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


    private void setListPrefSourceEntries(ListPreference lp, List<Profile> profiles){

        List<String> tmp = new ArrayList<>();
        List<String> tmp2 = new ArrayList<>();

        for(Profile p : profiles){
            tmp.add(p.getSite());
            tmp2.add(new Gson().toJson(p));
        }

        CharSequence[] csEntries = tmp.toArray(new CharSequence[tmp.size()]);
        CharSequence[] csEntryVals = tmp2.toArray(new CharSequence[tmp2.size()]);

        lp.setEntries(csEntries);
        lp.setEntryValues(csEntryVals);


        if(lp.getValue() == null){
            lp.setValue(tmp2.get(0));
        }

        setListPrefCurrencyEntries(listPreferenceCurrencySettings, new Gson().fromJson(lp.getValue(), Profile.class).getCurrencies());

    }



    private void setListPrefCurrencyEntries(ListPreference lp, List<String> values){
        CharSequence[] cs = values.toArray(new CharSequence[values.size()]);
        lp.setEntries(cs);
        lp.setEntryValues(cs);

        if(lp.getValue() == null || !values.contains(lp.getValue())) {
            lp.setValue(values.get(0));
        }
    }



    private int calculate10Percent(int value){
        return value * 10 / 100;
    }
}
