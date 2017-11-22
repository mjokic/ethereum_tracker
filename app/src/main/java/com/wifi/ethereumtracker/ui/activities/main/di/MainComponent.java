package com.wifi.ethereumtracker.ui.activities.main.di;

import com.wifi.ethereumtracker.app.di.AppComponent;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;
import com.wifi.ethereumtracker.ui.activities.main.MainActivity;

import dagger.Component;

@MainScope
@Component(modules = {MainModule.class, GsonModule.class}, dependencies = AppComponent.class)
public interface MainComponent {

    void inject(MainActivity activity);
}
