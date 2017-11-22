package com.wifi.ethereumtracker.app.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wifi.ethereumtracker.ext.AutoValueAdapterFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {

    @Provides
    Gson providesGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new AutoValueAdapterFactory())
                .create();
    }

}
