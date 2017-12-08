package com.wifi.ethereumtracker.ui.activities.graph.mvp;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.app.model.Currency;
import com.wifi.ethereumtracker.app.model.Price;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.network.ApiService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class GraphModel {

    private final ApiService apiService;
    private final SharedPreferences sharedPreferences;
    private final Gson gson;


    public GraphModel(ApiService apiService, SharedPreferences sharedPreferences, Gson gson) {
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    Observable<List<Price>> getPrices(Source source, Currency currency, int time) {
        return apiService.getPrices(source.site(), currency.getName(), time)
                .subscribeOn(Schedulers.io());
    }

    Source getSourcePreference() {
        return gson.fromJson(sharedPreferences.getString("sourceSettings", "null"),
                Source.class);
    }


    Currency getCurrencyPreference() {
        return gson.fromJson(sharedPreferences.getString("currencySettings", "null"),
                Currency.class);
    }

}
