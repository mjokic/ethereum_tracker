package com.wifi.ethereumtracker.ui.activities.preferencesA.mvp;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.wifi.ethereumtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ViewConstructor")
public class PreferencesView extends FrameLayout {

    private final AppCompatActivity activity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public PreferencesView(AppCompatActivity activity) {
        super(activity);
        this.activity = activity;
        inflate(getContext(), R.layout.activity_preferences, this);
        ButterKnife.bind(this);

        toolbar.setTitle("Settings");
        toolbar.removeAllViews(); // removing refresh button imageView

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
    }

    public FragmentManager getFragmentManager(){
        return activity.getSupportFragmentManager();
    }

}
