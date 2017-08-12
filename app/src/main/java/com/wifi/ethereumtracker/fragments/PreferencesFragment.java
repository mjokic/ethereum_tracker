package com.wifi.ethereumtracker.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.activities.MainActivity;
import com.wifi.ethereumtracker.model.profiles.CexProfile;
import com.wifi.ethereumtracker.model.profiles.GeminiProfile;
import com.wifi.ethereumtracker.services.BackgroundCheckService;

public class PreferencesFragment extends PreferenceFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


        ListPreference listPreferenceSourceSettings = (ListPreference) findPreference("sourceSettings");
        final ListPreference listPreferenceCurrencySettings = (ListPreference) findPreference("currencySettings");

        SwitchPreference switchPreferenceEnableNotifications = (SwitchPreference)
                findPreference("enableNotificationsSettings");


        listPreferenceSourceSettings.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String s = (String) o;
                System.out.println(s + "<--- YOOO");


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

                Intent intent = new Intent(getActivity(), BackgroundCheckService.class);

                if(status){
                    // start service
                    getActivity().startService(intent);

                }else{
                    // stop service
                    getActivity().stopService(intent);
                    return false;
                }

                return true;
            }
        });

    }

}
