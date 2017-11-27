package com.wifi.ethereumtracker.widgets.di;

import com.wifi.ethereumtracker.app.di.modules.GsonModule;

import dagger.Component;

@WidgetScope
@Component(modules = GsonModule.class)
public interface AppWidgetComponent {

}
