package com.wifi.ethereumtracker.ui.activities.splash.di;


import android.content.Context;

import com.squareup.sqlbrite2.BriteDatabase;
import com.wifi.ethereumtracker.app.di.modules.DatabaseModule;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.ui.activities.splash.mvp.SplashModel;
import com.wifi.ethereumtracker.ui.activities.splash.mvp.SplashPresenter;
import com.wifi.ethereumtracker.ui.activities.splash.mvp.SplashView;

import dagger.Module;
import dagger.Provides;

@Module(includes = DatabaseModule.class)
public class SplashModule {

    private final Context context;

    public SplashModule(Context context) {
        this.context = context;
    }

    @Provides
    @SplashScope
    SplashModel providesSplashModel(ApiService apiService, BriteDatabase briteDatabase) {
        return new SplashModel(apiService, briteDatabase);
    }

    @Provides
    @SplashScope
    SplashView providesSplashView() {
        return new SplashView(context);
    }

    @Provides
    @SplashScope
    SplashPresenter providesSplashPresenter(SplashView view, SplashModel model) {
        return new SplashPresenter(view, model);
    }

}
