package com.wifi.ethereumtracker.services.jobService;

import com.wifi.ethereumtracker.app.di.AppComponent;
import com.wifi.ethereumtracker.app.di.modules.GsonModule;

import dagger.Component;

@JobServiceScope
@Component(modules = GsonModule.class, dependencies = AppComponent.class)
public interface JobServiceComponent {

    void inject(BackgroundCheckService service);
}
