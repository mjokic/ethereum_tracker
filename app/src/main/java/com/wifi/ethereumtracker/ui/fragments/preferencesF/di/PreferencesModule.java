package com.wifi.ethereumtracker.ui.fragments.preferencesF.di;

import com.squareup.sqlbrite2.BriteDatabase;
import com.wifi.ethereumtracker.app.di.modules.DatabaseModule;
import com.wifi.ethereumtracker.ui.fragments.preferencesF.mvp.PreferencesModel;
import com.wifi.ethereumtracker.ui.fragments.preferencesF.mvp.PreferencesPresenter;

import dagger.Module;
import dagger.Provides;

@Module(includes = DatabaseModule.class)
public class PreferencesModule {


    @PreferencesScope
    @Provides
    PreferencesModel providesPreferencesModel(BriteDatabase briteDatabase){
        return new PreferencesModel(briteDatabase);
    }

    @PreferencesScope
    @Provides
    PreferencesPresenter providesPreferencesPresenter(PreferencesModel model){
        return new PreferencesPresenter(model);
    }

}
