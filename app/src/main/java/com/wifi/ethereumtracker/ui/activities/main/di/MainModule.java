package com.wifi.ethereumtracker.ui.activities.main.di;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainModel;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainPresenter;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainView;

import dagger.Module;
import dagger.Provides;

@Module(includes = GsonModule.class)
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
    MainModel providesMainModel(ApiService apiService,
                                SharedPreferences sharedPreferences, Gson gson) {
        return new MainModel(apiService, sharedPreferences, gson);
    }

    @MainScope
    @Provides
    MainPresenter providesMainPresenter(MainView view, MainModel model) {
        return new MainPresenter(view, model);
    }

}
