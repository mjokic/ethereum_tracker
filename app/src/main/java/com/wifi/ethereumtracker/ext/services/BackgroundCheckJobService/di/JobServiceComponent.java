package com.wifi.ethereumtracker.ext.services.BackgroundCheckJobService.di;

import com.wifi.ethereumtracker.app.di.AppComponent;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;
import com.wifi.ethereumtracker.ext.services.BackgroundCheckJobService.BackgroundCheckService;

import dagger.Component;

@JobServiceScope
@Component(modules = GsonModule.class, dependencies = AppComponent.class)
public interface JobServiceComponent {

    void inject(BackgroundCheckService service);
}
