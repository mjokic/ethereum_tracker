package com.wifi.ethereumtracker.ui.activities.preferences;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.ui.fragments.PreferencesFragment;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.removeAllViews(); // removing refresh button imageView
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        PreferencesFragment preferencesFragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.preferencesFragment, preferencesFragment)
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
