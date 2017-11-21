package com.wifi.ethereumtracker.ui.activities.preferences.di;

import com.wifi.ethereumtracker.ui.activities.preferences.PreferencesActivity;

import dagger.Component;

@PreferencesActivityScope
@Component(modules = PreferencesActivityModule.class)
public interface PreferencesActivityComponent {

    void inject(PreferencesActivity activity);

}
