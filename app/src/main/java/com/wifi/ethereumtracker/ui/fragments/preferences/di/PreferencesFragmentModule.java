package com.wifi.ethereumtracker.ui.fragments.preferences.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.sqlbrite2.BriteDatabase;
import com.wifi.ethereumtracker.app.di.modules.DatabaseModule;
import com.wifi.ethereumtracker.ext.AutoValueAdapterFactory;
import com.wifi.ethereumtracker.ui.fragments.preferences.mvp.PreferencesFragmentModel;
import com.wifi.ethereumtracker.ui.fragments.preferences.mvp.PreferencesFragmentPresenter;

import dagger.Module;
import dagger.Provides;

@Module(includes = DatabaseModule.class)
public class PreferencesFragmentModule {


    @PreferencesFragmentScope
    @Provides
    PreferencesFragmentModel providesPreferencesModel(BriteDatabase briteDatabase) {
        return new PreferencesFragmentModel(briteDatabase);
    }

    @PreferencesFragmentScope
    @Provides
    PreferencesFragmentPresenter providesPreferencesPresenter(PreferencesFragmentModel model) {
        return new PreferencesFragmentPresenter(model);
    }

    @PreferencesFragmentScope
    @Provides
    Gson providesGson(){
        return new GsonBuilder()
                .registerTypeAdapterFactory(new AutoValueAdapterFactory())
                .create();
    }

}
