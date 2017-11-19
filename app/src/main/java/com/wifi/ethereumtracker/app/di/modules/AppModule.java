package com.wifi.ethereumtracker.app.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wifi.ethereumtracker.app.di.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context){
        this.context = context;
    }

    @AppScope
    @Provides
    public SharedPreferences providesDefaultSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
