package com.wifi.ethereumtracker.activities;


import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.wifi.ethereumtracker.fragments.PreferencesFragment;

public class PreferencesActivity extends PreferenceActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferencesFragment preferencesFragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, preferencesFragment)
                .commit();
    }



}
