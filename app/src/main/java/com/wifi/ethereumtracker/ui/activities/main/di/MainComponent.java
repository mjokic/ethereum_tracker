package com.wifi.ethereumtracker.ui.activities.main.di;

import com.wifi.ethereumtracker.ui.activities.main.MainActivity;

import dagger.Component;

@MainScope
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity activity);
}
