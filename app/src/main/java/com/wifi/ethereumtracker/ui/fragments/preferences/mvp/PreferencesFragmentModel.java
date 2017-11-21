package com.wifi.ethereumtracker.ui.fragments.preferences.mvp;

import com.squareup.sqlbrite2.BriteDatabase;
import com.wifi.ethereumtracker.app.model.Source;

import java.util.List;

import io.reactivex.Observable;

public class PreferencesFragmentModel {

    private final BriteDatabase briteDatabase;

    public PreferencesFragmentModel(BriteDatabase briteDatabase){
        this.briteDatabase = briteDatabase;
    }


    public Observable<List<Source>> getSourcesFromDb(){
        return briteDatabase
                .createQuery(Source.TABLE_NAME, Source.FACTORY.selectAll().statement)
                .mapToList(Source.MAPPER::map);
    }

}
