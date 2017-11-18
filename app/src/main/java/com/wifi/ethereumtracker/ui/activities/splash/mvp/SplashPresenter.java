package com.wifi.ethereumtracker.ui.activities.splash.mvp;


import android.text.TextUtils;

import com.wifi.ethereumtracker.app.model.Source;

import java.sql.Time;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashPresenter {

    private SplashView view;
    private SplashModel model;

    public SplashPresenter(SplashView view, SplashModel model) {
        this.view = view;
        this.model = model;
    }


    public void onCreate() {
        Observable<List<Source>> observableNet = model.loadSourcesFromNet();
        Observable<List<Source>> observableDb = model.loadSourcesFromDb();

        Observable.combineLatest(observableDb, observableNet, List::equals)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val -> {
                    if (val){
                        // lists are the same, there's no new update
                        // just open main activity
                    }else {
                        // there's new update
                        // drop old tables and insert new data
                    }
                });

    }

    public void onDestroy() {

    }



    private boolean isNewUpdate(List<Source> sourcesNet, List<Source> sourcesDb){
        for (Source source : sourcesDb){
            if (!sourcesNet.contains(source)){
                return true;
            }
        }
        return false;
    }
}
