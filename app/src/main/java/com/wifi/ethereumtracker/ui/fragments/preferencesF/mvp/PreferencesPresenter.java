package com.wifi.ethereumtracker.ui.fragments.preferencesF.mvp;

import com.wifi.ethereumtracker.app.model.Source;

import java.util.List;

public class PreferencesPresenter {

    private final PreferencesModel model;

    public PreferencesPresenter(PreferencesModel model){
        this.model = model;
    }

    public void onCreate(){

    }

    public void onDestroy(){

    }

    public List<Source> test(){
        return model.getSourcesFromDb()
                .blockingFirst();
    }

}
