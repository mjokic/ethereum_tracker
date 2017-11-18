package com.wifi.ethereumtracker.ui.activities.splash.mvp;

import android.content.Context;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.model.SourceModel;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.ext.MyDbHelper;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashModel {

    private final Context context;
    private final BriteDatabase briteDatabase;
    private ApiService apiService;

    public SplashModel(Context context, ApiService apiService) {
        this.apiService = apiService;
        this.context = context;

        MyDbHelper myDbHelper = new MyDbHelper(this.context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        briteDatabase = sqlBrite.wrapDatabaseHelper(myDbHelper, Schedulers.io());
    }


    void loadSourcesFromNet() {
        apiService.getSources()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::insertSourcesToDb); // when loaded insert to db
    }

    private void insertSourcesToDb(List<Source> sources) {
        Source.InsertProfile insertProfile =
                new SourceModel.InsertProfile(briteDatabase.getWritableDatabase(), Source.FACTORY);

        for (Source source : sources) {
            insertProfile.bind(source.site(), source.currencies());
            insertProfile.program.execute();
        }
    }

}
