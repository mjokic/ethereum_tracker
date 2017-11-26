package com.wifi.ethereumtracker.ui.activities.main.mvp;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;
import com.wifi.ethereumtracker.app.model.Currency;
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
        String currencyStr = sharedPreferences.getString("currencySettings", defaultCurrency);
        return gson.fromJson(currencyStr, Currency.class).getName();
    }

    double getMyValue() {
        return Double.parseDouble(sharedPreferences.getString("myValue", "1"));
    }

    void savePrice(Double price) {
        sharedPreferences.edit().putString("currentPrice", String.valueOf(price)).apply();
    }


    Observable<String> getSourcePreferenceObservable() {
        RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(sharedPreferences);
        return rxSharedPreferences.getString("sourceSettings").asObservable();
    }


    Observable<String> getCurrencyPreferenceObservable() {
        RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(sharedPreferences);
        return rxSharedPreferences.getString("currencySettings").asObservable();
    }

}
