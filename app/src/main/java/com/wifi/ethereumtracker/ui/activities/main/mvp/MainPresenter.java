package com.wifi.ethereumtracker.ui.activities.main.mvp;

import com.wifi.ethereumtracker.model.pojo.ResponsePojo;

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
    }

    public void onResume(){
        view.clickRefreshButton();
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }

    
    private Disposable refreshButtonClick(){
        return view.onRefreshButtonClick()
                .switchMap(__ -> {
                    view.startAnimation();
                    return getPriceObservable();
                })
                .subscribe(rp -> {
                            view.stopAnimation();
                            double myValue = model.getMyValue();
                            view.setTextViewValues(myValue,
                                    rp.getCurrentPrice() * myValue,
                                    rp.getChange24hour());
                        },
                        Timber::d);
    }

    private Observable<ResponsePojo> getPriceObservable(){
        return model.getPrice() // load these values from shared preferences
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
