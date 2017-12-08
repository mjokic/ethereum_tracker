package com.wifi.ethereumtracker.ui.activities.graph.mvp;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GraphPresenter {

    private final GraphView view;
    private final GraphModel model;

    public GraphPresenter(GraphView view, GraphModel model) {
        this.view = view;
        this.model = model;
    }


    public void onCreate() {
        getPrices();
    }

    private void getPrices(){
        model.getPrices(model.getSourcePreference(), model.getCurrencyPreference())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::setupGraph,
                        Timber::d);
    }

}
