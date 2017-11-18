package com.wifi.ethereumtracker.app;

import android.app.Application;

import com.wifi.ethereumtracker.BuildConfig;
import com.wifi.ethereumtracker.app.di.AppComponent;
import com.wifi.ethereumtracker.app.di.DaggerAppComponent;
import com.wifi.ethereumtracker.app.di.modules.NetworkModule;

import timber.log.Timber;

public class App extends Application {


    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        component = DaggerAppComponent.builder()
                .networkModule(new NetworkModule(getApplicationContext()))
                .build();

    }

    public AppComponent getComponent() {
        return component;
    }
}
