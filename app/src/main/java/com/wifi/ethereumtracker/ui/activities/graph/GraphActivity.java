package com.wifi.ethereumtracker.ui.activities.graph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.App;
import com.wifi.ethereumtracker.ui.activities.graph.di.DaggerGraphComponent;
import com.wifi.ethereumtracker.ui.activities.graph.di.GraphModule;
import com.wifi.ethereumtracker.ui.activities.graph.mvp.GraphPresenter;
import com.wifi.ethereumtracker.ui.activities.graph.mvp.GraphView;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class GraphActivity extends AppCompatActivity {

    @Inject
    GraphPresenter presenter;

    @Inject
    GraphView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerGraphComponent.builder()
                .appComponent(((App) getApplication()).getComponent())
                .graphModule(new GraphModule(this))
                .build()
                .inject(this);

        setContentView(view);
        presenter.onCreate();
    }
}
