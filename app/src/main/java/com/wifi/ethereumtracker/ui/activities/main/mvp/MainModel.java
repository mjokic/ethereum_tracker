package com.wifi.ethereumtracker.ui.activities.main.mvp;

import android.content.SharedPreferences;

import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;

import io.reactivex.Observable;

public class MainModel {

    private final ApiService apiService;
    private final SharedPreferences sharedPreferences;

    public MainModel(ApiService apiService, SharedPreferences sharedPreferences){
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
    }


    Observable<ResponsePojo> getPrice(String site, String currency){
        return apiService.getPrice(site, currency);
    }


    public boolean sharedPrefsNull(){
        return sharedPreferences == null;
    }

}
