package com.wifi.ethereumtracker.ui.activities.preferences.di;

import com.wifi.ethereumtracker.ui.activities.preferences.PreferencesActivity;

import dagger.Component;

@PreferencesScope
@Component(modules = PreferencesModule.class)
public interface PreferencesComponent {

    void inject(PreferencesActivity activity);

}
