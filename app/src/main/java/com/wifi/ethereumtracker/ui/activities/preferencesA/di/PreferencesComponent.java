package com.wifi.ethereumtracker.ui.activities.preferencesA.di;

import com.wifi.ethereumtracker.ui.activities.preferencesA.PreferencesActivity;

import dagger.Component;

@PreferencesScope
@Component(modules = PreferencesModule.class)
public interface PreferencesComponent {

    void inject(PreferencesActivity activity);

}
