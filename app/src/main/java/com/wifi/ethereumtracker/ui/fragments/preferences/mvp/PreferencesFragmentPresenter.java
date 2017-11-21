package com.wifi.ethereumtracker.ui.fragments.preferences.mvp;

import com.wifi.ethereumtracker.app.model.Source;

import java.util.List;

public class PreferencesFragmentPresenter {

    private final PreferencesFragmentModel model;

    public PreferencesFragmentPresenter(PreferencesFragmentModel model) {
        this.model = model;
    }

    public void onCreate() {

    }

    public void onDestroy() {

    }

    public List<Source> getSources() {
        return model.getSourcesFromDb()
                .blockingFirst();
    }

}
