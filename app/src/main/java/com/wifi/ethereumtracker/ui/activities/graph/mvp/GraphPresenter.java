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
        onSpinnerItemSelected();
    }

    private void getPrices(int time) {
        model.getPrices(model.getSourcePreference(), model.getCurrencyPreference(), time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::setupGraph,
                        Timber::d);
    }


    private void onSpinnerItemSelected() {
        view.getSpinnerObservable()
                .map(o -> {
                    switch (Integer.valueOf(o.toString())) {
                        case 1:
                            return 24;
                        case 2:
                            return 7;
                        case 3:
                            return 30;
                        default:
                            return 1;
                    }
                })
                .subscribe(this::getPrices);
    }
}
