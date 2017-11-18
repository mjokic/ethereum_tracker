package com.wifi.ethereumtracker.ui.activities.splash.mvp;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.ext.MyDbHelper;
import com.wifi.ethereumtracker.model.Profile;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashModel {

    private final Context context;
    private ApiService apiService;
    private final BriteDatabase briteDatabase;

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
                .map(sources -> {
                    ContentValues contentValues = new ContentValues();

                    for (Profile profile : sources){
                        contentValues.put(profile.getSite(), new Gson().toJson(profile).getBytes());
                    }

                    return contentValues;
                })
                .subscribe(
                        cv -> briteDatabase.update("profiles", cv,null),
                        throwable -> {
                            Timber.d(throwable);
                            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

}
