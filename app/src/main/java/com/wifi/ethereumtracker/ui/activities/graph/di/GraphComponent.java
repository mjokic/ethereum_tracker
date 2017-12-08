package com.wifi.ethereumtracker.ui.activities.graph.di;

import com.wifi.ethereumtracker.app.di.AppComponent;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;
import com.wifi.ethereumtracker.ui.activities.graph.GraphActivity;

import dagger.Component;

@GraphScope
@Component(modules = {GraphModule.class, GsonModule.class}, dependencies = AppComponent.class)
public interface GraphComponent {

    void inject(GraphActivity activity);

}
