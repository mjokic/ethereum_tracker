package com.wifi.ethereumtracker.ui.activities.graph.di;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.ui.activities.graph.mvp.GraphModel;
import com.wifi.ethereumtracker.ui.activities.graph.mvp.GraphPresenter;
import com.wifi.ethereumtracker.ui.activities.graph.mvp.GraphView;

import dagger.Module;
import dagger.Provides;

@Module(includes = GsonModule.class)
public class GraphModule {

    private final Activity activity;

    public GraphModule(Activity activity){
        this.activity = activity;
    }

    @GraphScope
    @Provides
    GraphModel providesGraphModel(ApiService apiService, SharedPreferences sharedPreferences, Gson gson) {
        return new GraphModel(apiService, sharedPreferences, gson);
    }


    @GraphScope
    @Provides
    GraphView providesGraphView() {
        return new GraphView(activity);
    }


    @GraphScope
    @Provides
    GraphPresenter providesGraphPresenter(GraphView view, GraphModel model) {
        return new GraphPresenter(view, model);
    }

}
