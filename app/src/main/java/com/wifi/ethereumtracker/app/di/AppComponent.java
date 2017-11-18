package com.wifi.ethereumtracker.app.di;

import android.app.Application;

import com.wifi.ethereumtracker.app.di.modules.AppModule;
import com.wifi.ethereumtracker.app.di.modules.NetworkModule;
import com.wifi.ethereumtracker.services.apiCalls.ApiService;

import dagger.Component;

@AppScope
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(Application application);
    ApiService providesApiService();

}
