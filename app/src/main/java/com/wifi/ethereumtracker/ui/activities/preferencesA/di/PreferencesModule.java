package com.wifi.ethereumtracker.ui.activities.preferencesA.di;

import android.support.v7.app.AppCompatActivity;

import com.wifi.ethereumtracker.ui.activities.preferencesA.mvp.PreferencesPresenter;
import com.wifi.ethereumtracker.ui.activities.preferencesA.mvp.PreferencesView;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    private final AppCompatActivity activity;

    public PreferencesModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @PreferencesScope
    @Provides
    PreferencesView providesPreferencesView() {
        return new PreferencesView(activity);
    }

    @PreferencesScope
    @Provides
    PreferencesPresenter providesPreferencesPresenter(PreferencesView view) {
        return new PreferencesPresenter(view);
    }

}
