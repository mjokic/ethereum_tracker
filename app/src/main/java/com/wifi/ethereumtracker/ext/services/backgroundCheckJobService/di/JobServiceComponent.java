package com.wifi.ethereumtracker.ext.services.backgroundCheckJobService.di;

import com.wifi.ethereumtracker.app.di.AppComponent;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;
import com.wifi.ethereumtracker.ext.services.backgroundCheckJobService.BackgroundCheckService;

import dagger.Component;

@JobServiceScope
@Component(modules = GsonModule.class, dependencies = AppComponent.class)
public interface JobServiceComponent {

    void inject(BackgroundCheckService service);
}
