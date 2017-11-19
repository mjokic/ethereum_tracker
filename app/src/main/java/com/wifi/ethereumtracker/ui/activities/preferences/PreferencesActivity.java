package com.wifi.ethereumtracker.ui.activities.preferences;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wifi.ethereumtracker.ui.activities.preferences.di.DaggerPreferencesComponent;
import com.wifi.ethereumtracker.ui.activities.preferences.di.PreferencesModule;
import com.wifi.ethereumtracker.ui.activities.preferences.mvp.PreferencesPresenter;
import com.wifi.ethereumtracker.ui.activities.preferences.mvp.PreferencesView;

import javax.inject.Inject;

public class PreferencesActivity extends AppCompatActivity {

    @Inject
    PreferencesView view;

    @Inject
    PreferencesPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerPreferencesComponent.builder()
                .preferencesModule(new PreferencesModule(this))
                .build()
                .inject(this);

        setContentView(view);
        presenter.onCreate();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
