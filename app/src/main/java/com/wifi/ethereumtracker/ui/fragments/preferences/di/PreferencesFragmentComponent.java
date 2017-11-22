package com.wifi.ethereumtracker.ui.fragments.preferences.di;

import com.wifi.ethereumtracker.app.di.modules.DatabaseModule;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;
import com.wifi.ethereumtracker.ui.fragments.preferences.PreferencesFragment;

import dagger.Component;

@PreferencesFragmentScope
@Component(modules = {PreferencesFragmentModule.class, DatabaseModule.class, GsonModule.class})
public interface PreferencesFragmentComponent {

    void inject(PreferencesFragment fragment);
}
