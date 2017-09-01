package com.wifi.ethereumtracker.activities;


import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.fragments.PreferencesFragment;

public class PreferencesActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);

        PreferencesFragment preferencesFragment = new PreferencesFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.removeAllViews(); // removing refresh button imageView
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.preferencesFragment, preferencesFragment)
                .commit();
    }



}
