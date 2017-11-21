package com.wifi.ethereumtracker.ui.activities.preferences.di;

import android.support.v7.app.AppCompatActivity;

import com.wifi.ethereumtracker.ui.activities.preferences.mvp.PreferencesActivityPresenter;
import com.wifi.ethereumtracker.ui.activities.preferences.mvp.PreferencesActivityView;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesActivityModule {

    private final AppCompatActivity activity;

    public PreferencesActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @PreferencesActivityScope
    @Provides
    PreferencesActivityView providesPreferencesView() {
        return new PreferencesActivityView(activity);
    }

    @PreferencesActivityScope
    @Provides
    PreferencesActivityPresenter providesPreferencesPresenter(PreferencesActivityView view) {
        return new PreferencesActivityPresenter(view);
    }

}
