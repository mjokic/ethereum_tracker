package com.wifi.ethereumtracker.ui.activities.main.mvp;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;

import io.reactivex.Observable;

public class MainModel {

    private final ApiService apiService;
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public MainModel(ApiService apiService, SharedPreferences sharedPreferences, Gson gson){
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }


    Observable<ResponsePojo> getPrice(){
        return apiService.getPrice(getSource(), getCurrency());
    }

    private String getSource(){
        String sourceStr = sharedPreferences.getString("sourceSettings", "cex");
        return gson.fromJson(sourceStr, Source.class).site();
    }

    private String getCurrency(){
        return sharedPreferences.getString("currencySettings", "usd");
    }

    public double getMyValue(){
        return Double.parseDouble(sharedPreferences.getString("myValue", "1"));
    }

}
