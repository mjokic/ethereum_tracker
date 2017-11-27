package com.wifi.ethereumtracker.widgets.di;

import com.wifi.ethereumtracker.app.di.AppComponent;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;
import com.wifi.ethereumtracker.widgets.AppWidget;

import dagger.Component;

@WidgetScope
@Component(modules = GsonModule.class, dependencies = AppComponent.class)
public interface AppWidgetComponent {

    void inject(AppWidget widget);

}
