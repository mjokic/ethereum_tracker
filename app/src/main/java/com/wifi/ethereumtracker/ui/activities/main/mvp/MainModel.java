package com.wifi.ethereumtracker.ui.activities.main.mvp;

import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;

import io.reactivex.Observable;

public class MainModel {

    private final ApiService apiService;

    public MainModel(ApiService apiService){
        this.apiService = apiService;
    }


    Observable<ResponsePojo> getPrice(String site, String currency){
        return apiService.getPrice(site, currency);
    }


}
