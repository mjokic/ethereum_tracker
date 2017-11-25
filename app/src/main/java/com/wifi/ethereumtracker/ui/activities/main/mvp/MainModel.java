package com.wifi.ethereumtracker.ui.activities.main.mvp;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.app.model.Price;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.network.ApiService;

import io.reactivex.Observable;

public class MainModel {

    private final ApiService apiService;
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public MainModel(ApiService apiService, SharedPreferences sharedPreferences, Gson gson) {
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }


    Observable<Price> getPrice(String defaultSource, String defaultCurrency) {
        return apiService.getPrice(getSource(defaultSource), getCurrency(defaultCurrency));
    }

    private String getSource(String defaultSource) {
        String sourceStr = sharedPreferences.getString("sourceSettings", defaultSource);
        return gson.fromJson(sourceStr, Source.class).site();
    }

    private String getCurrency(String defaultCurrency) {
        return sharedPreferences.getString("currencySettings", defaultCurrency);
    }

    double getMyValue() {
        return Double.parseDouble(sharedPreferences.getString("myValue", "1"));
    }

    void savePrice(Double price){
        sharedPreferences.edit().putInt("currentPrice", price.intValue()).apply();
    }

}
