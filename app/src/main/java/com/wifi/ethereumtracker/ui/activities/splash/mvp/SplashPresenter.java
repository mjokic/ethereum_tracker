package com.wifi.ethereumtracker.ui.activities.splash.mvp;


import com.wifi.ethereumtracker.app.model.Source;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashPresenter {

    private final CompositeDisposable compositeDisposable;
    private final SplashView view;
    private final SplashModel model;

    public SplashPresenter(SplashView view, SplashModel model) {
        this.view = view;
        this.model = model;
        compositeDisposable = new CompositeDisposable();
    }


    public void onCreate() {
        Observable<List<Source>> observableDb = model.loadSourcesFromDb();
        Observable<List<Source>> observableNet = model.loadSourcesFromNet();

        List<Source> updateList = new ArrayList<>();

        Disposable s = Observable.combineLatest(observableDb, observableNet, (sources, sources2) -> {
            if (sources.equals(sources2)) return true;
            updateList.addAll(sources2);
            return false;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val -> {
                    if (val) {
                        // lists are the same, there's no new update
                        // just open main activity
                        view.startMainActivity();
                    } else {
                        // there's new update
                        // drop old tables and insert new data
                        // then open new activity
                        model.deleteAllFromSourcesTable();
                        model.insertSourcesToDb(updateList);
                        view.startMainActivity();
                    }
                }, throwable -> {
                    Timber.d(throwable);
                    view.displayErrorDialog();
                });
        compositeDisposable.add(s);
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }

}
