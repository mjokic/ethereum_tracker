package com.wifi.ethereumtracker.ui.activities.preferences;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wifi.ethereumtracker.ui.activities.preferences.di.DaggerPreferencesActivityComponent;
import com.wifi.ethereumtracker.ui.activities.preferences.di.PreferencesActivityModule;
import com.wifi.ethereumtracker.ui.activities.preferences.mvp.PreferencesActivityPresenter;
import com.wifi.ethereumtracker.ui.activities.preferences.mvp.PreferencesActivityView;

import javax.inject.Inject;

public class PreferencesActivity extends AppCompatActivity {

    @Inject
    PreferencesActivityView view;

    @Inject
    PreferencesActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerPreferencesActivityComponent.builder()
                .preferencesActivityModule(new PreferencesActivityModule(this))
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
