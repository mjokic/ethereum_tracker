package com.wifi.ethereumtracker.ui.activities.main.di;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.wifi.ethereumtracker.ui.activities.main.mvp.MainModel;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainPresenter;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainView;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private final AppCompatActivity activity;

    public MainModule(AppCompatActivity activity){
        this.activity = activity;
    }

    @MainScope
    @Provides
    MainView providesMainView(){
        return new MainView(activity);
    }

    @MainScope
    @Provides
    MainModel providesMainModel(){
        return new MainModel();
    }

    @MainScope
    @Provides
    MainPresenter providesMainPresenter(MainView view, MainModel model){
        return new MainPresenter(view, model);
    }

}
