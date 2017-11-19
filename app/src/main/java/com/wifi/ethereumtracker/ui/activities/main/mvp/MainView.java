package com.wifi.ethereumtracker.ui.activities.main.mvp;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.wifi.ethereumtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ViewConstructor")
public class MainView extends FrameLayout {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public MainView(AppCompatActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_main, this);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);
    }

}
