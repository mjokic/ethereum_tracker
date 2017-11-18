package com.wifi.ethereumtracker.ui.activities.splash.di;

import com.wifi.ethereumtracker.app.di.AppComponent;
import com.wifi.ethereumtracker.ui.activities.splash.SplashScreenActivity;

import dagger.Component;

@SplashScope
@Component(modules = SplashModule.class, dependencies = AppComponent.class)
public interface SplashComponent {

    void inject(SplashScreenActivity activity);

}
