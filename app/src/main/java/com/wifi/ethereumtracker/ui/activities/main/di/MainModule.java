package com.wifi.ethereumtracker.ui.activities.main.di;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainModel;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainPresenter;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainView;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private final AppCompatActivity activity;

    public MainModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @MainScope
    @Provides
    MainView providesMainView() {
        return new MainView(activity);
    }

    @MainScope
    @Provides
    MainModel providesMainModel(ApiService apiService, SharedPreferences sharedPreferences) {
        return new MainModel(apiService, sharedPreferences);
    }

    @MainScope
    @Provides
    MainPresenter providesMainPresenter(MainView view, MainModel model) {
        return new MainPresenter(view, model);
    }

}
