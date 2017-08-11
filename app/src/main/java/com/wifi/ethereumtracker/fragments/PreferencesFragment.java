package com.wifi.ethereumtracker.fragments;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.model.profiles.CexProfile;
import com.wifi.ethereumtracker.model.profiles.GeminiProfile;

public class PreferencesFragment extends PreferenceFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


        ListPreference listPreferenceSourceSettings = (ListPreference) findPreference("sourceSettings");
        final ListPreference listPreferenceCurrencySettings = (ListPreference) findPreference("currencySettings");

//        listPreferenceSourceSettings.setEntries(new CharSequence[] {"CEX.io", "Gemini"});
//        listPreferenceSourceSettings.setEntryValues(new CharSequence[] {"cex.io", "gemini"});
//        listPreferenceSourceSettings.setDefaultValue("cex.io");

        listPreferenceSourceSettings.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String s = (String) o;
                System.out.println(s);


                if(s.equals(CexProfile.baseUrl)){
                    listPreferenceCurrencySettings.setEntries(new CharSequence[] {"USD", "EUR", "GBP"});
                    listPreferenceCurrencySettings.setEntryValues(new CharSequence[] {"USD", "EUR", "GBP"});

                }else if(s.equals(GeminiProfile.baseUrl)){
                    listPreferenceCurrencySettings.setEntries(new CharSequence[] {"USD"});
                    listPreferenceCurrencySettings.setEntryValues(new CharSequence[] {"USD"});
                }

                listPreferenceCurrencySettings.setValue("USD");

                return true;
            }
        });

    }

}
