package com.wifi.ethereumtracker.ui.activities.splash.mvp;


public class SplashPresenter {

    private SplashView view;
    private SplashModel model;

    public SplashPresenter(SplashView view, SplashModel model) {
        this.view = view;
        this.model = model;
    }


    public void onCreate() {
        model.loadSourcesFromNet();
    }

    public void onDestroy() {

    }

}
