package com.wifi.ethereumtracker.ui.activities.preferencesA.mvp;

import android.support.v4.app.FragmentManager;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.ui.fragments.preferencesF.PreferencesFragment;

public class PreferencesPresenter {

    private final PreferencesView view;

    public PreferencesPresenter(PreferencesView view) {
        this.view = view;
    }


    public void onCreate() {
        PreferencesFragment preferencesFragment = new PreferencesFragment();
        FragmentManager fragmentManager = view.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.preferencesFragment, preferencesFragment)
                .commit();
    }

}
