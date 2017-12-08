package com.wifi.ethereumtracker.ui.activities.main.mvp;

import com.wifi.ethereumtracker.app.model.Price;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainPresenter {

    private final MainView view;
    private final MainModel model;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainPresenter(MainView view, MainModel model) {
        this.view = view;
        this.model = model;
    }


    public void onCreate() {
        compositeDisposable.add(refreshButtonClick());
        compositeDisposable.add(update());
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }


    private Disposable refreshButtonClick() {
        return view.onRefreshButtonClick()
                .switchMap(__ -> {
                    view.startAnimation();
                    return getPriceObservable();
                })
                .subscribe(rp -> {
                            double myValue = model.getMyValue();
                            double price = rp.getPrice();
                            double finalPrice = price * myValue;
                            view.stopAnimation();
                            model.savePrice(finalPrice);
                            view.setTextViewValues(myValue,
                                    finalPrice,
                                    rp.getChange24hr(),
                                    rp.getCurrency());
                        },
                        throwable -> {
                            view.stopAnimation();
                            view.displayErrorDialog();
                            Timber.d(throwable);
                        });
    }

    private Observable<Price> getPriceObservable() {
        return model.getPrice(view.getDefaultSource(), view.getDefaultCurrency()) // load these values from shared preferences
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private Disposable update() {
        return Observable.merge(model.getSourcePreferenceObservable(),
                model.getCurrencyPreferenceObservable(),
                model.getMyValuePreferenceObservable())
                .subscribe(__ -> view.clickRefreshButton());
    }


    public void openGraphActivity(){
        view.openGraphActivity();
    }

}
