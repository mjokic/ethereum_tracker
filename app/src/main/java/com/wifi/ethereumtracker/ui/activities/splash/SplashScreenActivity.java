package com.wifi.ethereumtracker.ui.activities.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wifi.ethereumtracker.app.App;
import com.wifi.ethereumtracker.ui.activities.splash.di.DaggerSplashComponent;
import com.wifi.ethereumtracker.ui.activities.splash.di.SplashModule;
import com.wifi.ethereumtracker.ui.activities.splash.mvp.SplashPresenter;
import com.wifi.ethereumtracker.ui.activities.splash.mvp.SplashView;

import javax.inject.Inject;


public class SplashScreenActivity extends AppCompatActivity {
    /**
     * This activity is the first one to start.
     * On its start get sources from API
     */


    @Inject
    SplashPresenter presenter;

    @Inject
    SplashView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerSplashComponent.builder()
                .appComponent(((App) getApplication()).getComponent())
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);


        setContentView(view);
        presenter.onCreate();
    }

}
