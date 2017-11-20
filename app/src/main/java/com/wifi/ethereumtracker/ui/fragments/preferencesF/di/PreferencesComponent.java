package com.wifi.ethereumtracker.ui.fragments.preferencesF.di;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import dagger.Component;

@PreferencesScope
@Component(modules = PreferencesModule.class)
public interface PreferencesComponent {

    void inject(PreferenceFragmentCompat fragmentCompat);
}
