package com.wifi.ethereumtracker.ui.activities.splash.mvp;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.QueryObservable;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.model.SourceModel;
import com.wifi.ethereumtracker.app.network.ApiService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashModel {

    private final BriteDatabase briteDatabase;
    private final ApiService apiService;

    public SplashModel(ApiService apiService, BriteDatabase briteDatabase) {
        this.apiService = apiService;
        this.briteDatabase = briteDatabase;
    }


    Observable<List<Source>> loadSourcesFromNet() {
        Timber.d("Loading sources from internet");
        return apiService.getSources()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    Observable<List<Source>> loadSourcesFromDb() {
        QueryObservable query = briteDatabase.createQuery(Source.TABLE_NAME,
                Source.FACTORY.selectAll().statement);
        return query.mapToList(Source.MAPPER::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    void deleteAllFromSourcesTable() {
        briteDatabase.delete(Source.TABLE_NAME, null);
        Timber.d("All sources has been deleted from table [%s]", Source.TABLE_NAME);
    }

    void insertSourcesToDb(List<Source> sources) {
        Source.InsertProfile insertProfile =
                new SourceModel.InsertProfile(briteDatabase.getWritableDatabase(), Source.FACTORY);

        for (Source source : sources) {
            insertProfile.bind(source.site(), source.currencies());
            insertProfile.program.execute();
        }

        Timber.d("Sources inserted into table [%s]", Source.TABLE_NAME);
    }

}
